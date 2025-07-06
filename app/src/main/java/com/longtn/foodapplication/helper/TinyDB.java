package com.longtn.foodapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longtn.foodapplication.model.FoodModel;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TinyDB {
    private SharedPreferences preferences;
    private String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    private String lastImagePath = "";

    public TinyDB(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Bitmap getImage(String path) {
        Bitmap bitmapFromPath = null;
        try {
            bitmapFromPath = BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapFromPath;
    }

    public String getSavedImagePath() { return lastImagePath; }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void putListString(String key, ArrayList<String> list) {
        Set<String> set = new HashSet<>(list);
        preferences.edit().putStringSet(key, set).apply();
    }

    public ArrayList<String> getListString(String key) {
        Set<String> set = preferences.getStringSet(key, null);
        return set != null ? new ArrayList<>(set) : new ArrayList<>();
    }

    public void putImage(String key, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageBase64 = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT);
        putString(key, imageBase64);
    }

    public String saveImageToInternalStorage(Context context, Bitmap image, String imageName) {
        try {
            java.io.File directory = context.getDir("images", Context.MODE_PRIVATE);
            java.io.File imageFile = new java.io.File(directory, imageName + ".png");
            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            lastImagePath = imageFile.getAbsolutePath();
            return lastImagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void putListObject(String key, ArrayList<?> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        preferences.edit().putString(key, json).apply();
    }

    public ArrayList<FoodModel> getListObject(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        ArrayList<FoodModel> list = new ArrayList<>();
        if (json != null) {
            Type type = new TypeToken<ArrayList<FoodModel>>(){}.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }
}
