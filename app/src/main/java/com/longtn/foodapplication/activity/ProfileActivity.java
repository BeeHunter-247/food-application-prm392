package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtn.foodapplication.R;
import com.longtn.foodapplication.adapter.OrderAdapter;
import com.longtn.foodapplication.helper.DatabaseHelper;
import com.longtn.foodapplication.helper.ManagementUser;
import com.longtn.foodapplication.model.Order;

import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileNameMain, tvProfileNameCard, tvProfileEmailCard, tvEditProfile;
    private ImageView ivBack;
    private ManagementUser managementUser;
    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileNameMain = findViewById(R.id.tv_profile_name_main);
        tvProfileNameCard = findViewById(R.id.tv_profile_name_card);
        tvProfileEmailCard = findViewById(R.id.tv_profile_email_card);
        tvEditProfile = findViewById(R.id.tv_edit_profile);
        ivBack = findViewById(R.id.iv_back);
        rvOrders = findViewById(R.id.rv_orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        managementUser = new ManagementUser(this);


        ivBack.setOnClickListener(v -> finish());
        tvEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();// Tải lại dữ liệu mỗi khi quay lại màn hình này
        loadOrders();
    }

    private void loadOrders() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", null);
        if (userEmail != null) {
            List<Order> orders = dbHelper.getOrdersByEmail(userEmail);
            for (Order order : orders) {
                Log.d("DEBUG", "Order #" + order.id + " has " + order.items.size() + " items");
            }
            orderAdapter = new OrderAdapter(orders, order -> {
                Intent intent = new Intent(ProfileActivity.this, OrderDetailActivity.class);
                intent.putExtra("order", order); // ✅ truyền toàn bộ đối tượng Order
                startActivity(intent);
            });
            rvOrders.setAdapter(orderAdapter);
        }
    }


    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", null);

        if (userEmail != null) {
            HashMap<String, String> userDetails = managementUser.getUserByEmail(userEmail);
            String name = userDetails.get("name");
            String email = userDetails.get("email");

            tvProfileNameMain.setText(name);
            tvProfileNameCard.setText(name);
            tvProfileEmailCard.setText(email);
        } else {
            Toast.makeText(this, "Your session has expired", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        // Xóa tất cả các activity cũ khỏi stack và tạo một task mới cho LoginActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Đóng ProfileActivity
    }
}
