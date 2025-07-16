package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.longtn.foodapplication.R;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button btnManageUsers = findViewById(R.id.btn_manage_users);
        Button btnManageFoods = findViewById(R.id.btn_manage_foods);

        // Hiện tại chúng ta chưa làm màn hình quản lý user, có thể thêm sau
        btnManageUsers.setOnClickListener(v -> {
             startActivity(new Intent(AdminDashboardActivity.this, UserManagementActivity.class));
        });

        btnManageFoods.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, FoodManagementActivity.class));
        });
    }
}
