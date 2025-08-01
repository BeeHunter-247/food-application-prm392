package com.longtn.foodapplication.activity;

// trong package activity/LoginActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.textfield.TextInputEditText;
import com.longtn.foodapplication.Interface.LoginContract;
import com.longtn.foodapplication.activity.MainActivity;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementUser;
import com.longtn.foodapplication.presenter.LoginPresenter;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private TextInputEditText edtEmail, edtPassword;
    private AppCompatButton btnLogin;
    private TextView tvSignUp;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Đảm bảo bạn có file layout này
        presenter = new LoginPresenter(this, this);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        presenter = new LoginPresenter(this, this);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            presenter.handleLogin(email, password);
        });

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    @Override
    public void showLoginSuccess() {
        String userEmail = edtEmail.getText().toString().trim();

        ManagementUser managementUser = new ManagementUser(this);
        HashMap<String, String> userDetails = managementUser.getUserByEmail(userEmail);
        String userName = userDetails.get("name");

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("username", userName);
        editor.putString("userEmail", userEmail);
        editor.apply();

        Intent intent;
        if ("admin@gmail.com".equalsIgnoreCase(userEmail)) {
            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
