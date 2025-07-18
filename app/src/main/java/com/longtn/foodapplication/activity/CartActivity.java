package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtn.foodapplication.Interface.ChangeNumberItemsListener;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.adapter.CartAdapter;
import com.longtn.foodapplication.helper.ManagementCart;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalPriceText, taxText, deliveryText, totalText, emptyText, checkoutBtn;
    private double tax;
    private ScrollView scrollView;
    private double totalAmount = 0.0; // dùng để truyền cho VNPAY giả lập

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);

        initView();
        initList();
        bottomNavigation();
        calculateCard();
        handleCheckout(); // thêm dòng này để gắn sự kiện thanh toán
    }

    private void bottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout carBtnMain = findViewById(R.id.cartBtnMain);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, MainActivity.class)));

        carBtnMain.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, CartActivity.class)));
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managementCart.getListCart(), this, this::calculateCard);
        recyclerViewList.setAdapter(adapter);

        if (managementCart.getListCart().isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calculateCard() {
        double percentTax = 0.02;
        double delivery = 10.0;
        tax = Math.round(((managementCart.getTotalPrice() * percentTax) * 100.0)) / 100.0;
        double total = Math.round((managementCart.getTotalPrice() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((managementCart.getTotalPrice() * 100.0)) / 100.0;

        totalPriceText.setText("$" + itemTotal);
        taxText.setText("$" + tax);
        deliveryText.setText("$" + delivery);
        totalText.setText("$" + total);

        totalAmount = total; // lưu lại để dùng khi thanh toán
    }

    private void initView() {
        totalPriceText = findViewById(R.id.totalPriceText);
        taxText = findViewById(R.id.taxText);
        deliveryText = findViewById(R.id.deliveryText);
        totalText = findViewById(R.id.totalText);
        recyclerViewList = findViewById(R.id.view);
        scrollView = findViewById(R.id.scrollView);
        emptyText = findViewById(R.id.emptyText);
        checkoutBtn = findViewById(R.id.textView16); // ID của TextView "CheckOut"
    }

    private void handleCheckout() {
        checkoutBtn.setOnClickListener(v -> {
            if (totalAmount <= 0) {
                // Đơn hàng trống
                return;
            }

            Intent intent = new Intent(CartActivity.this, VnPayWebViewActivity.class);
            intent.putExtra("amount", totalAmount);
            intent.putExtra("paymentUrl", "file:///android_asset/vnpay_demo.html"); // đường dẫn HTML giả lập
            startActivity(intent);
        });
    }
}
