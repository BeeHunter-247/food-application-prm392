package com.longtn.foodapplication.helper;

import android.content.Context;
import android.widget.Toast;

import com.longtn.foodapplication.Interface.ChangeNumberItemsListener;
import com.longtn.foodapplication.model.FoodModel;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(FoodModel item) {
        ArrayList<FoodModel> listFood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listFood.size(); i++) {
            if(listFood.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }

        if(existAlready) {
            listFood.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listFood.add(item);
        }

        tinyDB.putListObject("CardList", listFood);
        Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FoodModel> getListCart() {
        return tinyDB.getListObject("CardList");
    }

    public void minusNumberFood(ArrayList<FoodModel> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if(listFood.get(position).getNumberInCart() == 1) {
            listFood.remove(position);
        } else {
            listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() - 1);
        }

        tinyDB.putListObject("CardList", listFood);
        changeNumberItemsListener.changed();
    }

    public void plusNumberFood(ArrayList<FoodModel> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CardList", listFood);
        changeNumberItemsListener.changed();
    }

    public Double getTotalPrice() {
        ArrayList<FoodModel> listFood = getListCart();
        double price = 0;
        for (int i = 0; i < listFood.size(); i++) {
            price = price + (listFood.get(i).getPrice() * listFood.get(i).getNumberInCart());
        }

        return price;
    }
}
