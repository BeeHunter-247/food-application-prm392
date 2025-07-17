package com.longtn.foodapplication.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementUser;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etEmail;
    private Button btnSave;
    private ImageView ivBack;
    private ManagementUser managementUser;
    private HashMap<String, String> currentUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFullName = findViewById(R.id.et_edit_name);
        etEmail = findViewById(R.id.et_edit_email);
        btnSave = findViewById(R.id.btn_save_profile);
        ivBack = findViewById(R.id.iv_edit_back);
        managementUser = new ManagementUser(this);

        loadCurrentData();

        ivBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentData() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", null);
        if (userEmail != null) {
            currentUserDetails = managementUser.getUserByEmail(userEmail);
            etFullName.setText(currentUserDetails.get("name"));
            etEmail.setText(currentUserDetails.get("email"));
        }
    }

    private void saveProfile() {
        String newName = etFullName.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please do not leave this field blank", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = Integer.parseInt(currentUserDetails.get("id"));
        int rowsAffected = managementUser.updateUserProfile(userId, newName, newEmail);

        if (rowsAffected > 0) {
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userEmail", newEmail);
            editor.putString("username", newName);
            editor.apply();

            Toast.makeText(this, "Successfully updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }
}