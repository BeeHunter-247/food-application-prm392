package com.longtn.foodapplication.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.adapter.FoodAdminAdapter;
import com.longtn.foodapplication.helper.ManagementFood;
import com.longtn.foodapplication.model.FoodModel;
import java.util.ArrayList;
import java.util.List;

public class FoodManagementActivity extends AppCompatActivity implements FoodAdminAdapter.OnFoodListener {

    private RecyclerView recyclerView;
    private FoodAdminAdapter adapter;
    private List<FoodModel> foodList = new ArrayList<>();
    private ManagementFood dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_management);

        dataSource = new ManagementFood(this);
        recyclerView = findViewById(R.id.recycler_view_foods);
        FloatingActionButton fab = findViewById(R.id.fab_add_food);

        setupRecyclerView();
        loadFoods();

        fab.setOnClickListener(v -> showAddEditFoodDialog(null));
    }

    private void setupRecyclerView() {
        adapter = new FoodAdminAdapter(foodList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadFoods() {
        List<FoodModel> foods = dataSource.getAllFoods();
        adapter.updateData(foods);
    }

    @Override
    public void onEditClick(FoodModel food) {
        showAddEditFoodDialog(food);
    }

    @Override
    public void onDeleteClick(FoodModel food) {
        new AlertDialog.Builder(this)
                .setTitle("Delete food")
                .setMessage("Are you sure you want to delete " + food.getTitle() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dataSource.deleteFood(food.getId());
                    Toast.makeText(this, "Deleted food item", Toast.LENGTH_SHORT).show();
                    loadFoods();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAddEditFoodDialog(final FoodModel food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_edit_food, null);

        // Ánh xạ tất cả các EditText
        final EditText etTitle = view.findViewById(R.id.et_food_title);
        final EditText etDesc = view.findViewById(R.id.et_food_desc);
        final EditText etPicture = view.findViewById(R.id.et_food_picture);
        final EditText etPrice = view.findViewById(R.id.et_food_price);
        final EditText etStar = view.findViewById(R.id.et_food_star);
        final EditText etTime = view.findViewById(R.id.et_food_time);
        final EditText etCalories = view.findViewById(R.id.et_food_calories);
        Button btnSave = view.findViewById(R.id.btn_save_food);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        if (food != null) { // Chế độ sửa: điền thông tin cũ vào các ô
            etTitle.setText(food.getTitle());
            etDesc.setText(food.getDescription());
            etPicture.setText(food.getPicture());
            etPrice.setText(String.valueOf(food.getPrice()));
            etStar.setText(String.valueOf(food.getStar()));
            etTime.setText(String.valueOf(food.getTime()));
            etCalories.setText(String.valueOf(food.getCalories()));
        }

        btnSave.setOnClickListener(v -> {
            // Lấy dữ liệu từ tất cả các ô
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String picture = etPicture.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String starStr = etStar.getText().toString().trim();
            String timeStr = etTime.getText().toString().trim();
            String caloriesStr = etCalories.getText().toString().trim();

            // Kiểm tra các trường bắt buộc
            if (title.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please enter at least the Name and Price", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển đổi kiểu dữ liệu
            double price = Double.parseDouble(priceStr);
            double star = starStr.isEmpty() ? 0 : Double.parseDouble(starStr);
            int time = timeStr.isEmpty() ? 0 : Integer.parseInt(timeStr);
            int calories = caloriesStr.isEmpty() ? 0 : Integer.parseInt(caloriesStr);

            // Tạo hoặc cập nhật đối tượng FoodModel
            FoodModel newOrUpdatedFood = (food != null) ? food : new FoodModel();
            newOrUpdatedFood.setTitle(title);
            newOrUpdatedFood.setDescription(desc);
            newOrUpdatedFood.setPicture(picture);
            newOrUpdatedFood.setPrice(price);
            newOrUpdatedFood.setStar((int) star); // Giả sử star là int trong model
            newOrUpdatedFood.setTime(time);
            newOrUpdatedFood.setCalories(calories);

            // Lưu vào database
            if (food == null) { // Chế độ thêm mới
                dataSource.addFood(newOrUpdatedFood);
                Toast.makeText(this, "Added food successfully.", Toast.LENGTH_SHORT).show();
            } else { // Chế độ sửa
                dataSource.updateFood(newOrUpdatedFood);
                Toast.makeText(this, "Successfully updated.", Toast.LENGTH_SHORT).show();
            }

            loadFoods(); // Tải lại danh sách để hiển thị thay đổi
            dialog.dismiss(); // Đóng hộp thoại
        });

        dialog.show();
    }
}
