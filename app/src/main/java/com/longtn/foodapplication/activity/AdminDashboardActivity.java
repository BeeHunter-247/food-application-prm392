package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // <-- Thêm import cho Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.activity.FoodManagementFragment;
import com.longtn.foodapplication.activity.UserManagementFragment;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvAdminTitle;
    private ImageView ivLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvAdminTitle = findViewById(R.id.tv_admin_title);
        ivLogout = findViewById(R.id.iv_logout);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_admin);

        bottomNav.setOnItemSelectedListener(navListener);

        // --- THÊM BƯỚC KIỂM TRA ---
        ivLogout.setOnClickListener(v -> {
            // Hiển thị một thông báo để xác nhận nút đã được nhấn
            Toast.makeText(AdminDashboardActivity.this, "Logout successes !", Toast.LENGTH_SHORT).show();
            logout();
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,
                    new FoodManagementFragment()).commit();
            tvAdminTitle.setText("Manage Foods");
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                String title = "";

                int itemId = item.getItemId();
                if (itemId == R.id.navigation_manage_food) {
                    selectedFragment = new FoodManagementFragment();
                    title = "Manage Foods";
                } else if (itemId == R.id.navigation_manage_user) {
                    selectedFragment = new UserManagementFragment();
                    title = "Manage Users";
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,
                            selectedFragment).commit();
                    tvAdminTitle.setText(title);
                }
                return true;
            };

    private void logout() {
        // Xóa dữ liệu SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Chuyển hướng về MainActivity
        Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}