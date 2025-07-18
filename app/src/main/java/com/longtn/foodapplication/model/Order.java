package com.longtn.foodapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    public int id;
    public double totalPrice;
    public long createdAt;
    public ArrayList<OrderItem> items;

    public Order(int id, double totalPrice, long createdAt) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.items = new ArrayList<>();
    }
}
