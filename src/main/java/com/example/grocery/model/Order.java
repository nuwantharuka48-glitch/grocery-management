package com.example.grocery.model;

import java.util.List;

public class Order {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private String address;
    private List<String> items;
    private double totalAmount;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}