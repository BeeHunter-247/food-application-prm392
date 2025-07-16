package com.longtn.foodapplication.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.longtn.foodapplication.model.FoodModel;
import java.util.ArrayList;
import java.util.List;

public class ManagementFood {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public ManagementFood(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addFood(FoodModel food) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FOOD_TITLE, food.getTitle());
        values.put(DatabaseHelper.COLUMN_FOOD_PICTURE, food.getPicture());
        values.put(DatabaseHelper.COLUMN_FOOD_DESCRIPTION, food.getDescription());
        values.put(DatabaseHelper.COLUMN_FOOD_PRICE, food.getPrice());
        values.put(DatabaseHelper.COLUMN_FOOD_STAR, food.getStar());
        values.put(DatabaseHelper.COLUMN_FOOD_TIME, food.getTime());
        values.put(DatabaseHelper.COLUMN_FOOD_CALORIES, food.getCalories());
        long id = database.insert(DatabaseHelper.TABLE_FOODS, null, values);
        database.close();
        return id;
    }

    public List<FoodModel> getAllFoods() {
        List<FoodModel> foodList = new ArrayList<>();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DatabaseHelper.TABLE_FOODS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                FoodModel food = new FoodModel();
                food.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_ID)));
                food.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_TITLE)));
                food.setPicture(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_PICTURE)));
                food.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_DESCRIPTION)));
                food.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_PRICE)));
                food.setStar(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_STAR)));
                food.setTime(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_TIME)));
                food.setCalories(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOOD_CALORIES)));
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return foodList;
    }

    public int updateFood(FoodModel food) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FOOD_TITLE, food.getTitle());
        values.put(DatabaseHelper.COLUMN_FOOD_PICTURE, food.getPicture());
        values.put(DatabaseHelper.COLUMN_FOOD_DESCRIPTION, food.getDescription());
        values.put(DatabaseHelper.COLUMN_FOOD_PRICE, food.getPrice());
        values.put(DatabaseHelper.COLUMN_FOOD_STAR, food.getStar());
        values.put(DatabaseHelper.COLUMN_FOOD_TIME, food.getTime());
        values.put(DatabaseHelper.COLUMN_FOOD_CALORIES, food.getCalories());

        int rowsAffected = database.update(DatabaseHelper.TABLE_FOODS, values, DatabaseHelper.COLUMN_FOOD_ID + " = ?",
                new String[]{String.valueOf(food.getId())});
        database.close();
        return rowsAffected;
    }

    public void deleteFood(int foodId) {
        database = dbHelper.getWritableDatabase();
        database.delete(DatabaseHelper.TABLE_FOODS, DatabaseHelper.COLUMN_FOOD_ID + " = ?",
                new String[]{String.valueOf(foodId)});
        database.close();
    }
}

