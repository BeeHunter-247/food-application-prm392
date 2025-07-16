package com.longtn.foodapplication.helper;

// trong package helper/DatabaseHelper.java

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "foodapp.db";
    private static final int DATABASE_VERSION = 2;

    // --- BẢNG USERS ---
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    // --- BẢNG FOODS ---
    public static final String TABLE_FOODS = "foods";
    public static final String COLUMN_FOOD_ID = "id";
    public static final String COLUMN_FOOD_TITLE = "title";
    public static final String COLUMN_FOOD_PICTURE = "picture";
    public static final String COLUMN_FOOD_DESCRIPTION = "description";
    public static final String COLUMN_FOOD_PRICE = "price";
    public static final String COLUMN_FOOD_STAR = "star";
    public static final String COLUMN_FOOD_TIME = "time";
    public static final String COLUMN_FOOD_CALORIES = "calories";


    // Câu lệnh tạo bảng Users
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";
    // --- CÂU LỆNH TẠO BẢNG FOODS  ---
    private static final String CREATE_TABLE_FOODS = "CREATE TABLE " + TABLE_FOODS + " ("
            + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FOOD_TITLE + " TEXT NOT NULL, "
            + COLUMN_FOOD_PICTURE + " TEXT, "
            + COLUMN_FOOD_DESCRIPTION + " TEXT, "
            + COLUMN_FOOD_PRICE + " REAL NOT NULL, "
            + COLUMN_FOOD_STAR + " REAL, "
            + COLUMN_FOOD_TIME + " INTEGER, "
            + COLUMN_FOOD_CALORIES + " INTEGER"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_FOODS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nếu nâng cấp từ phiên bản cũ, hãy tạo bảng foods
        if (oldVersion < 2) {
            db.execSQL(CREATE_TABLE_FOODS);
        } else {
            // Trường hợp khác, xóa hết và tạo lại để đơn giản
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
            onCreate(db);
        }
    }
}