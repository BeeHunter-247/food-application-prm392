package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementCart;

public class PaymentNotification extends AppCompatActivity {

    private TextView tvPaymentResult, tvPaymentMessage;
    private ImageView ivStatusIcon;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_notification);

        tvPaymentResult = findViewById(R.id.tv_payment_result);
        tvPaymentMessage = findViewById(R.id.tv_payment_message);
        ivStatusIcon = findViewById(R.id.iv_status_icon);

        result = getIntent().getStringExtra("result");

        switch (result) {
            case "Thanh toán thành công":
                showSuccessUI();
                handleSaveOrderAndReturn();
                break;
            case "Hủy thanh toán":
                showCancelUI();
                returnToCartLater();
                break;
            case "Lỗi thanh toán":
            default:
                showErrorUI();
                returnToCartLater();
                break;
        }
    }

    private void showSuccessUI() {
        tvPaymentResult.setText("✅ Thanh toán thành công!");
        tvPaymentResult.setTextColor(Color.parseColor("#388E3C"));
        ivStatusIcon.setImageResource(R.drawable.ic_success);
        tvPaymentMessage.setText("Đơn hàng của bạn đã được lưu.");
    }

    private void showCancelUI() {
        tvPaymentResult.setText("❌ Hủy thanh toán");
        tvPaymentResult.setTextColor(Color.parseColor("#F44336"));
        ivStatusIcon.setImageResource(R.drawable.ic_cancel);
        tvPaymentMessage.setText("Bạn đã hủy quá trình thanh toán.");
    }

    private void showErrorUI() {
        tvPaymentResult.setText("⚠️ Lỗi thanh toán");
        tvPaymentResult.setTextColor(Color.parseColor("#FF9800"));
        ivStatusIcon.setImageResource(R.drawable.ic_error);
        tvPaymentMessage.setText("Đã xảy ra lỗi trong quá trình thanh toán.");
    }

    private void handleSaveOrderAndReturn() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", null);


        if (userEmail != null) {
            ManagementCart cart = new ManagementCart(this);
            double totalAmount = getIntent().getDoubleExtra("amount", 0.0);
            cart.saveOrder(userEmail, totalAmount); // sẽ tự động xóa giỏ hàng

            Toast.makeText(this, "Đã lưu đơn hàng", Toast.LENGTH_SHORT).show();

            tvPaymentResult.postDelayed(() -> {
                Intent intent = new Intent(this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }, 2000);
        } else {
            Toast.makeText(this, "Không tìm thấy email người dùng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void returnToCartLater() {
        tvPaymentResult.postDelayed(() -> {
            Intent intent = new Intent(PaymentNotification.this, CartActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
