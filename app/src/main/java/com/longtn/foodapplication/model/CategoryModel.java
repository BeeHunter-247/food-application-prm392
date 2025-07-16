package com.longtn.foodapplication.model;

public class CategoryModel {

    private String title;
    private String picture;

    public CategoryModel(String picture, String title) {
        this.picture = picture;
        this.title = title;
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
}
