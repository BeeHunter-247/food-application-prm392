package com.longtn.foodapplication.helper;

// trong package helper/DatabaseHelper.java

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.longtn.foodapplication.model.Order;
import com.longtn.foodapplication.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

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

    // BẢNG ĐƠN HÀNG
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "id";
    public static final String COLUMN_ORDER_USER_EMAIL = "user_email";
    public static final String COLUMN_ORDER_TOTAL = "total_price";
    public static final String COLUMN_ORDER_TIME = "created_at";

    // BẢNG CHI TIẾT MÓN ĂN TRONG ĐƠN
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_ITEM_ORDER_ID = "order_id";
    public static final String COLUMN_ITEM_FOOD_TITLE = "food_title";
    public static final String COLUMN_ITEM_QUANTITY = "quantity";
    public static final String COLUMN_ITEM_PRICE = "price";


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
        db.execSQL(CREATE_TABLE_ORDERS);         // ✅ THÊM DÒNG NÀY
        db.execSQL(CREATE_TABLE_ORDER_ITEMS);    // ✅ THÊM DÒNG NÀY


        db.execSQL("INSERT INTO " + TABLE_FOODS + " (" +
                COLUMN_FOOD_TITLE + ", " +
                COLUMN_FOOD_PICTURE + ", " +
                COLUMN_FOOD_DESCRIPTION + ", " +
                COLUMN_FOOD_PRICE + ", " +
                COLUMN_FOOD_STAR + ", " +
                COLUMN_FOOD_TIME + ", " +
                COLUMN_FOOD_CALORIES + ") VALUES " +

                "('Pizza Hải sản', 'pizza', 'Pizza với tôm và mực, phô mai béo ngậy', 12, 5, 20, 600)," +
                "('Burger Bò Mỹ', 'burger', 'Burger thịt bò nhập khẩu Mỹ, nướng than thơm ngon', 25, 4, 15, 800)," +
                "('Xúc xích Đức', 'hotdog', 'Xúc xích Đức truyền thống, ăn kèm bánh mì giòn tan', 7, 4, 10, 550)," +
                "('Trà đào cam sả', 'drink', 'Trà đào tươi mát, thanh lọc cơ thể', 5, 5, 5, 120)," +
                "('Bánh Donut Dâu', 'donut', 'Donut vị dâu ngọt ngào, phủ kem hấp dẫn', 4, 4, 7, 350);");
    }

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_USER_EMAIL + " TEXT,"
            + COLUMN_ORDER_TOTAL + " REAL,"
            + COLUMN_ORDER_TIME + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_ORDER_ITEMS = "CREATE TABLE " + TABLE_ORDER_ITEMS + "("
            + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ITEM_ORDER_ID + " INTEGER,"
            + COLUMN_ITEM_FOOD_TITLE + " TEXT,"
            + COLUMN_ITEM_QUANTITY + " INTEGER,"
            + COLUMN_ITEM_PRICE + " REAL"
            + ")";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nếu nâng cấp từ phiên bản cũ, hãy tạo bảng foods
        if (oldVersion < 2) {
            db.execSQL(CREATE_TABLE_FOODS);
        } else {
            // Trường hợp khác, xóa hết và tạo lại để đơn giản
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
            onCreate(db);
        }
    }

    public List<Order> getOrdersByEmail(String email) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE user_email = ? ORDER BY created_at DESC", new String[]{email});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
            long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow("created_at"));

            Order order = new Order(id, total, createdAt);

            // ✅ Load items cho order
            Cursor itemCursor = db.rawQuery(
                    "SELECT oi.food_title, oi.quantity, oi.price, f.picture " +
                            "FROM order_items oi " +
                            "LEFT JOIN foods f ON oi.food_title = f.title " +
                            "WHERE oi.order_id = ?",
                    new String[]{String.valueOf(id)}
            );
            while (itemCursor.moveToNext()) {
                String foodTitle = itemCursor.getString(0);
                int quantity = itemCursor.getInt(1);
                double price = itemCursor.getDouble(2);
                String picture = itemCursor.getString(3); // tên ảnh, ví dụ: "pizza"

                OrderItem item = new OrderItem(foodTitle, quantity, price, picture);
                order.items.add(item);
            }
            itemCursor.close();

            orderList.add(order);
        }
        cursor.close();
        db.close();
        return orderList;
    }
}