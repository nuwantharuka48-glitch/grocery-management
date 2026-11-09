package com.example.grocery.model;

public class SpiceItem extends GroceryItem {
    private String spiceLevel;

    public SpiceItem(Long id, String name, double price, String unit, String imageUrl, String spiceLevel) {
        super(id, name, price, unit, imageUrl, "Seasoning & Spices");
        this.spiceLevel = spiceLevel;
    }

    public String getSpiceLevel() { return spiceLevel; }
}