package com.example.grocery.controller;

import com.example.grocery.model.FruitItem;
import com.example.grocery.model.GroceryItem;
import com.example.grocery.model.Order;
import com.example.grocery.model.SpiceItem;
import com.example.grocery.service.FileDatabaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class GroceryController {

    private final List<GroceryItem> inventory = new ArrayList<>();

    @Autowired
    private FileDatabaseService dbService;

    public GroceryController() {
        // FRUITS (FruitItem extends GroceryItem)
        inventory.add(new FruitItem(1L, "Fresh Red Apples", 3.99, "1 Kg", "/images/apples.jpg", true));
        inventory.add(new FruitItem(2L, "Ripe Hass Avocados", 4.50, "3 Pack", "/images/avocados.jpg", true));
        inventory.add(new FruitItem(3L, "Sweet Dark Cherries", 5.20, "500g", "/images/cherries.jpg", false));
        inventory.add(new FruitItem(4L, "Ruby Red Grapefruit", 2.80, "Each", "/images/grapefruit.jpg", true));
        inventory.add(new FruitItem(5L, "Seedless Green Grapes", 3.50, "1 Bunch", "/images/grapes.jpg", false));
        inventory.add(new FruitItem(6L, "Organic Bananas", 1.99, "1 Bundle", "/images/bananas.jpg", true));

        // SPICES (SpiceItem extends GroceryItem)
        inventory.add(new SpiceItem(7L, "Ground Cumin Powder", 2.49, "100g Jar", "/images/cumin.jpg", "Mild"));
        inventory.add(new SpiceItem(8L, "Ceylon Cinnamon Sticks", 3.10, "50g Pack", "/images/cinnamon.jpg", "Aromatic"));
        inventory.add(new SpiceItem(9L, "Whole Coriander Seeds", 2.20, "100g", "/images/coriander.jpg", "Mild"));
        inventory.add(new SpiceItem(10L, "Smoky Chili Spice Rub", 2.99, "75g Jar", "/images/spice-mix.jpg", "Spicy"));
    }

    // --- AUTHENTICATION ---
    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/";
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        if (dbService.authenticate(username, password)) {
            session.setAttribute("user", username);
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid username or password!");
        return "login";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String fullName,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {
        if (dbService.userExists(username)) {
            model.addAttribute("error", "Username is already taken. Please choose another.");
            return "register";
        }
        dbService.registerUser(username.trim(), password, fullName.trim());
        return "redirect:/login?registered=true";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- STORE CATALOG ---
    @GetMapping("/")
    public String showCatalog(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("items", inventory);
        return "index";
    }

    // --- CHECKOUT WITH QUANTITIES ---
    @PostMapping("/checkout")
    public String processOrder(
            @RequestParam String customerName,
            @RequestParam String customerEmail,
            @RequestParam String address,
            @RequestParam(required = false) List<Long> itemIds,
            @RequestParam Map<String, String> allParams,
            HttpSession session,
            Model model) {

        if (session.getAttribute("user") == null) return "redirect:/login";
        if (itemIds == null || itemIds.isEmpty()) return "redirect:/?error=noitems";

        List<String> orderedItemNames = new ArrayList<>();
        double total = 0.0;

        for (Long id : itemIds) {
            for (GroceryItem item : inventory) {
                if (item.getId().equals(id)) {
                    int quantity = 1;
                    String qtyStr = allParams.get("quantity_" + id);
                    if (qtyStr != null && !qtyStr.isEmpty()) {
                        try {
                            quantity = Math.max(1, Integer.parseInt(qtyStr));
                        } catch (NumberFormatException ignored) {}
                    }

                    double lineTotal = item.getPrice() * quantity;
                    total += lineTotal;
                    orderedItemNames.add(item.getName() + " x" + quantity + " ($" + String.format(Locale.US, "%.2f", lineTotal) + ")");
                }
            }
        }

        Order order = new Order();
        order.setOrderId("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomerName(customerName);
        order.setCustomerEmail(customerEmail);
        order.setAddress(address);
        order.setItems(orderedItemNames);
        order.setTotalAmount(total);

        // SAVE TO LOCAL PC FILE DATABASE
        dbService.saveOrderToFile(order);

        model.addAttribute("order", order);
        return "confirmation";
    }

    // --- ADMIN ORDER CRUD ---
    @GetMapping("/admin/orders")
    public String viewAdminOrders(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        model.addAttribute("orders", dbService.getAllOrders());
        model.addAttribute("user", session.getAttribute("user"));
        return "admin-orders";
    }

    @PostMapping("/admin/orders/update")
    public String updateOrderAddress(@RequestParam String orderId,
                                     @RequestParam String newAddress,
                                     HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        dbService.updateOrderAddress(orderId, newAddress);
        return "redirect:/admin/orders?updated=true";
    }

    @PostMapping("/admin/orders/delete")
    public String deleteOrder(@RequestParam String orderId, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        dbService.deleteOrder(orderId);
        return "redirect:/admin/orders?deleted=true";
    }
}