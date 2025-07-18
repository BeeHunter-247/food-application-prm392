package com.longtn.foodapplication.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtn.foodapplication.R;
import com.longtn.foodapplication.adapter.OrderItemAdapter;
import com.longtn.foodapplication.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderTotal, tvOrderDate;
    private RecyclerView rvOrderItems;
    private ImageView ivBack;
    private Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Khởi tạo view
        tvOrderId = findViewById(R.id.tv_order_detail_id);
        tvOrderTotal = findViewById(R.id.tv_order_detail_total);
        tvOrderDate = findViewById(R.id.tv_order_detail_date);
        rvOrderItems = findViewById(R.id.rv_order_items);
        ivBack = findViewById(R.id.iv_back);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));

        // Nhận dữ liệu Order từ Intent
        order = (Order) getIntent().getSerializableExtra("order"); // ✅ giữ nguyên

        if (order != null && order.items != null) {
            OrderItemAdapter adapter = new OrderItemAdapter(order.items);
            rvOrderItems.setAdapter(adapter);
        }


        if (order != null) {
            tvOrderId.setText("Order #" + order.id);
            tvOrderTotal.setText("Total: $" + order.totalPrice);
            tvOrderDate.setText("Date: " + formatDate(order.createdAt));

            OrderItemAdapter adapter = new OrderItemAdapter(order.items);
            rvOrderItems.setAdapter(adapter);
        }

        ivBack.setOnClickListener(v -> finish());
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
