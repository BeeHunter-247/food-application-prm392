package com.longtn.foodapplication.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementUser;
import java.util.HashMap;

public class AddEditUserActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnSave;
    private TextView tvTitle;
    private ImageView ivBack;
    private ManagementUser dataSource;
    private HashMap<String, String> currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        dataSource = new ManagementUser(this);

        etName = findViewById(R.id.et_user_name);
        etEmail = findViewById(R.id.et_user_email);
        etPassword = findViewById(R.id.et_user_password);
        btnSave = findViewById(R.id.btn_save_user);
        tvTitle = findViewById(R.id.tv_add_edit_user_title);
        ivBack = findViewById(R.id.iv_back_add_user);

        ivBack.setOnClickListener(v -> finish());

        currentUser = (HashMap<String, String>) getIntent().getSerializableExtra("user_item");

        if (currentUser != null) { // Edit Mode
            tvTitle.setText("Edit User");
            etName.setText(currentUser.get("name"));
            etEmail.setText(currentUser.get("email"));
            etPassword.setHint("Leave blank to keep password");
        } else { // Add Mode
            tvTitle.setText("Add New User");
        }

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter name and email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser == null) { // Add Mode
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter a password for the new user", Toast.LENGTH_SHORT).show();
                return;
            }
            dataSource.addUser(name, email, password);
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
        } else { // Edit Mode
            int userId = Integer.parseInt(currentUser.get("id"));
            dataSource.updateUser(userId, name, email, password);
            Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}