package com.longtn.foodapplication.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    public String foodTitle;
    public int quantity;
    public double price;
    public String foodImage; // ✅ thêm

    public OrderItem(String foodTitle, int quantity, double price, String foodImage) {
        this.foodTitle = foodTitle;
        this.quantity = quantity;
        this.price = price;
        this.foodImage = foodImage;
    }
}
