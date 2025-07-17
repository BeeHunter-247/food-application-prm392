package com.longtn.foodapplication.helper;

// trong package helper/ManagementUser.java

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagementUser {
    private DatabaseHelper dbHelper;

    public ManagementUser(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID},
                DatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?",
                new String[]{email, password}, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID},
                DatabaseHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{email}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean addUser(String name, String email, String password) {
        if (checkEmailExists(email)) {
            return false; // Email đã tồn tại
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password); // Chú ý: Nên mã hóa mật khẩu trong thực tế

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }
    public HashMap<String, String> getUserByEmail(String email) {
        HashMap<String, String> user = new HashMap<>();
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        // Thêm cột ID vào danh sách cột cần lấy
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_NAME, DatabaseHelper.COLUMN_USER_EMAIL},
                DatabaseHelper.COLUMN_USER_EMAIL + "=?",
                new String[]{email}, null, null, null);

        if (cursor.moveToFirst()) {
            user.put("id", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            user.put("name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
            user.put("email", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
        }
        cursor.close();
        db.close();
        return user;
    }
    public int updateUserProfile(int userId, String newName, String newEmail) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, newName);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, newEmail);

        int rowsAffected = db.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected;
    }
    public List<HashMap<String, String>> getAllUsers() {
        List<HashMap<String, String>> userList = new ArrayList<>();
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();

        // Câu lệnh WHERE để loại trừ email của admin
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " != ?";
        String[] selectionArgs = {"admin@gmail.com"};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_NAME, DatabaseHelper.COLUMN_USER_EMAIL},
                selection,
                selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> user = new HashMap<>();
                user.put("id", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
                user.put("name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
                user.put("email", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    /**
     * Cập nhật thông tin của một người dùng dựa vào ID.
     * @param userId ID của người dùng cần cập nhật.
     * @param name Tên mới.
     * @param email Email mới.
     * @param password Mật khẩu mới (có thể để trống để không đổi).
     * @return Số dòng bị ảnh hưởng (thường là 1 nếu thành công).
     */
    public int updateUser(int userId, String name, String email, String password) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);

        // Chỉ cập nhật mật khẩu nếu người dùng nhập mật khẩu mới
        if (password != null && !password.isEmpty()) {
            values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);
        }

        int rowsAffected = db.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected;
    }

    /**
     * Xóa một người dùng khỏi database dựa vào ID.
     * @param userId ID của người dùng cần xóa.
     */
    public void deleteUser(int userId) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_USERS, DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        db.close();
    }


}
