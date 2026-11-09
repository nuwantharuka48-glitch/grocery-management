package com.example.grocery.model;

public class GroceryItem {
    protected Long id;
    protected String name;
    protected double price;
    protected String unit;
    protected String imageUrl;
    protected String category;

    public GroceryItem(Long id, String name, double price, String unit, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getUnit() { return unit; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
}