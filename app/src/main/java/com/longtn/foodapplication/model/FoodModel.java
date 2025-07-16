package com.longtn.foodapplication.model;

import java.io.Serializable;

public class FoodModel implements Serializable {
    private int id;
    private String title;
    private String picture;
    private String description;
    private Double price;
    private int numberInCart;
    private int star;
    private int time;
    private int calories;

    public FoodModel(int id,  String title, String picture, String description, Double price, int numberInCart, int star, int time, int calories) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.numberInCart = numberInCart;
        this.star = star;
        this.time = time;
        this.calories = calories;
    }
    public FoodModel() {
        // Constructor này không cần làm gì cả
    }

    public int getId() { // <-- ĐỔI TỪ long SANG int
        return id;
    }

    public void setId(int id) { // <-- ĐỔI TỪ long SANG int
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
