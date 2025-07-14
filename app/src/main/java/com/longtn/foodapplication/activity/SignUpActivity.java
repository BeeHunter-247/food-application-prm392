package com.longtn.foodapplication.activity;

// trong package activity/SignUpActivity.java

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.textfield.TextInputEditText;
import com.longtn.foodapplication.Interface.SignUpContract;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.presenter.SignUpPresenter;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private TextInputEditText edtName, edtEmail, edtPassword, edtRePassword;
    private AppCompatButton btnSignUp;
    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Đảm bảo bạn có file layout này

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        presenter = new SignUpPresenter(this, this);

        btnSignUp.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String rePassword = edtRePassword.getText().toString().trim();
            presenter.handleSignUp(name, email, password, rePassword);
        });
    }

    @Override
    public void showSignUpSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish(); // Quay lại màn hình Login
    }

    @Override
    public void showSignUpFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationError(String field, String message) {
        if (field.equals("name")) {
            edtName.setError(message);
        } else if (field.equals("email")) {
            edtEmail.setError(message);
        } else if (field.equals("password")) {
            edtPassword.setError(message);
        } else if (field.equals("rePassword")) {
            edtRePassword.setError(message);
        }
    }
}
