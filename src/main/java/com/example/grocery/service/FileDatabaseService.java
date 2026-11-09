package com.example.grocery.service;

import com.example.grocery.model.Order;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FileDatabaseService {

    private final Path dbFolder = Paths.get("local_database");
    private final Path ordersFolder = dbFolder.resolve("orders");
    private final Path usersFile = dbFolder.resolve("users.txt");

    public FileDatabaseService() {
        try {
            Files.createDirectories(ordersFolder);
            if (!Files.exists(usersFile)) {
                Files.createFile(usersFile);
                registerUser("user", "1234", "John Doe");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- USER CRUD ---
    public boolean authenticate(String username, String password) {
        try {
            if (!Files.exists(usersFile)) return false;
            for (String line : Files.readAllLines(usersFile)) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(username.trim()) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean userExists(String username) {
        try {
            if (!Files.exists(usersFile)) return false;
            for (String line : Files.readAllLines(usersFile)) {
                String[] parts = line.split("\\|");
                if (parts.length >= 1 && parts[0].equalsIgnoreCase(username.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerUser(String username, String password, String fullName) {
        try {
            String record = username.trim() + "|" + password + "|" + fullName.trim() + System.lineSeparator();
            Files.write(usersFile, record.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- ORDER CRUD (FILE-BASED) ---
    // 1. CREATE: Write order .txt to disk
    public void saveOrderToFile(Order order) {
        try {
            Path orderFilePath = ordersFolder.resolve(order.getOrderId() + ".txt");
            StringBuilder sb = new StringBuilder();
            sb.append("=========================================\n");
            sb.append("           FRESHMART RECEIPT             \n");
            sb.append("=========================================\n");
            sb.append("Order ID: ").append(order.getOrderId()).append("\n");
            sb.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
            sb.append("Customer: ").append(order.getCustomerName()).append("\n");
            sb.append("Email: ").append(order.getCustomerEmail()).append("\n");
            sb.append("Delivery Address: ").append(order.getAddress()).append("\n");
            sb.append("-----------------------------------------\n");
            sb.append("Items Ordered:\n");
            for (String item : order.getItems()) {
                sb.append(" - ").append(item).append("\n");
            }
            sb.append("-----------------------------------------\n");
            sb.append("TOTAL AMOUNT: $").append(String.format(Locale.US, "%.2f", order.getTotalAmount())).append("\n");
            sb.append("=========================================\n");

            Files.write(orderFilePath, sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. READ: Read all orders from the local PC folder
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            if (!Files.exists(ordersFolder)) return orders;
            try (var stream = Files.list(ordersFolder)) {
                for (Path filePath : stream.filter(Files::isRegularFile).toList()) {
                    List<String> lines = Files.readAllLines(filePath);
                    Order order = new Order();
                    List<String> items = new ArrayList<>();
                    boolean readingItems = false;

                    for (String line : lines) {
                        if (line.startsWith("Order ID: ")) order.setOrderId(line.replace("Order ID: ", "").trim());
                        else if (line.startsWith("Customer: ")) order.setCustomerName(line.replace("Customer: ", "").trim());
                        else if (line.startsWith("Email: ")) order.setCustomerEmail(line.replace("Email: ", "").trim());
                        else if (line.startsWith("Delivery Address: ")) order.setAddress(line.replace("Delivery Address: ", "").trim());
                        else if (line.startsWith("TOTAL AMOUNT: $")) {
                            try {
                                order.setTotalAmount(Double.parseDouble(line.replace("TOTAL AMOUNT: $", "").trim()));
                            } catch (NumberFormatException ignored) {}
                            readingItems = false;
                        } else if (line.startsWith("Items Ordered:")) {
                            readingItems = true;
                        } else if (readingItems && line.startsWith(" - ")) {
                            items.add(line.replace(" - ", "").trim());
                        }
                    }
                    order.setItems(items);
                    orders.add(order);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // 3. UPDATE: Edit address inside the local text file
    public boolean updateOrderAddress(String orderId, String newAddress) {
        try {
            Path filePath = ordersFolder.resolve(orderId + ".txt");
            if (!Files.exists(filePath)) return false;

            List<String> lines = Files.readAllLines(filePath);
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                if (line.startsWith("Delivery Address: ")) {
                    updatedLines.add("Delivery Address: " + newAddress.trim());
                } else {
                    updatedLines.add(line);
                }
            }
            Files.write(filePath, updatedLines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE: Physically delete the .txt file from your PC folder
    public boolean deleteOrder(String orderId) {
        try {
            Path filePath = ordersFolder.resolve(orderId + ".txt");
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}