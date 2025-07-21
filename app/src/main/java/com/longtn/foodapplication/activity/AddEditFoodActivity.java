package com.longtn.foodapplication.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementFood;
import com.longtn.foodapplication.model.FoodModel;

public class AddEditFoodActivity extends AppCompatActivity {

    private EditText etTitle, etDesc, etPrice, etPicture; // <-- Thêm etPicture
    private Button btnSave;
    private TextView tvTitle;
    private ImageView ivBack;
    private ManagementFood dataSource;
    private FoodModel currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_food);

        dataSource = new ManagementFood(this);

        etTitle = findViewById(R.id.et_food_title);
        etDesc = findViewById(R.id.et_food_desc);
        etPrice = findViewById(R.id.et_food_price);
        etPicture = findViewById(R.id.et_food_picture); // <-- Ánh xạ view mới
        btnSave = findViewById(R.id.btn_save_food);
        tvTitle = findViewById(R.id.tv_add_edit_food_title);
        ivBack = findViewById(R.id.iv_back_add_food);

        ivBack.setOnClickListener(v -> finish());

        currentFood = (FoodModel) getIntent().getSerializableExtra("food_item");

        if (currentFood != null) { // Chế độ sửa
            tvTitle.setText("Edit Item");
            etTitle.setText(currentFood.getTitle());
            etDesc.setText(currentFood.getDescription());
            etPrice.setText(String.valueOf(currentFood.getPrice()));
            etPicture.setText(currentFood.getPicture()); // <-- Điền tên ảnh cũ
        } else { // Chế độ thêm
            tvTitle.setText("Add New Item");
        }

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String picture = etPicture.getText().toString().trim(); // <-- Lấy tên ảnh

        if (title.isEmpty() || priceStr.isEmpty() || picture.isEmpty()) {
            Toast.makeText(this, "Please fill in Title, Picture, and Price", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        if (currentFood == null) { // Chế độ thêm
            FoodModel newFood = new FoodModel();
            newFood.setTitle(title);
            newFood.setDescription(desc);
            newFood.setPrice(price);
            newFood.setPicture(picture); // <-- Gán tên ảnh
            // Bạn có thể gán các giá trị mặc định khác ở đây nếu muốn
            dataSource.addFood(newFood);
            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
        } else { // Chế độ sửa
            currentFood.setTitle(title);
            currentFood.setDescription(desc);
            currentFood.setPrice(price);
            currentFood.setPicture(picture); // <-- Cập nhật tên ảnh
            dataSource.updateFood(currentFood);
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish(); // Quay lại danh sách
    }
}