package com.example.grocery.model;

public class FruitItem extends GroceryItem {
    private boolean isOrganic;

    public FruitItem(Long id, String name, double price, String unit, String imageUrl, boolean isOrganic) {
        super(id, name, price, unit, imageUrl, "Fresh Fruit");
        this.isOrganic = isOrganic;
    }

    public boolean isOrganic() { return isOrganic; }
}