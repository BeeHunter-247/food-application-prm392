package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementCart;

public class VnPayWebViewActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnpay_webview);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        String paymentUrl = getIntent().getStringExtra("paymentUrl");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("payment_success")) {
                    Toast.makeText(VnPayWebViewActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                    // Lưu đơn hàng
                    SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    String userEmail = prefs.getString("userEmail", null);

                    ManagementCart cart = new ManagementCart(VnPayWebViewActivity.this);
                    double totalAmount = getIntent().getDoubleExtra("amount", 0.0);
                    cart.saveOrder(userEmail, totalAmount);

                    // ✅ Chuyển về CartActivity và xóa toàn bộ back stack
                    Intent intent = new Intent(VnPayWebViewActivity.this, CartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // xoá toàn bộ stack
                    intent.putExtra("payment_success", true); // gửi flag báo thành công
                    startActivity(intent);
                    finish(); // kết thúc VnPayWebViewActivity

                }
                return true;
            }

        });

        webView.loadUrl(paymentUrl);
    }
}

