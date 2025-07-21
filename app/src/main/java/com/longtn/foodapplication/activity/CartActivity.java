package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.longtn.foodapplication.Api.CreateOrder;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalPriceText, taxText, deliveryText, totalText, emptyText, checkoutBtn;
    private double tax;
    private ScrollView scrollView;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);

        // Cho phép thực thi API từ main thread (tạm thời cho demo)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        // Init SDK ZaloPay
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        initView();
        initList();
        bottomNavigation();
        calculateCard();
        handleCheckout(); // xử lý thanh toán qua ZaloPay
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

        totalAmount = total;
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
            if (totalAmount <= 0) return;

            String totalString = String.format("%.0f", totalAmount * 25000); // VNĐ

            CreateOrder orderApi = new CreateOrder();
            try {
                JSONObject data = orderApi.createOrder(totalString);
                String code = data.getString("return_code");
                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");

                    ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(String transactionId, String transToken, String appTransID) {
                            // TODO: xử lý lưu đơn hàng nếu cần
                            Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                            intent.putExtra("result", "Thanh toán thành công");
                            intent.putExtra("amount", totalAmount);
                            startActivity(intent);
                        }

                        @Override
                        public void onPaymentCanceled(String transToken, String appTransID) {
                            Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                            intent.putExtra("result", "Hủy thanh toán");
                            startActivity(intent);
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String transToken, String appTransID) {
                            Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                            intent.putExtra("result", "Lỗi thanh toán");
                            startActivity(intent);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Phải override để ZaloPay nhận callback
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();      // reload lại danh sách món ăn
        calculateCard(); // cập nhật lại tổng tiền
    }
}
