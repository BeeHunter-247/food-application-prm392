package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.activity.AddEditFoodActivity;
import com.longtn.foodapplication.adapter.FoodAdminAdapter;
import com.longtn.foodapplication.helper.ManagementFood;
import com.longtn.foodapplication.model.FoodModel;
import java.util.ArrayList;
import java.util.List;

public class FoodManagementFragment extends Fragment implements FoodAdminAdapter.OnFoodListener {

    private RecyclerView recyclerView;
    private FoodAdminAdapter adapter;
    private List<FoodModel> foodList = new ArrayList<>();
    private ManagementFood dataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Trả về layout cho fragment này
        return inflater.inflate(R.layout.fragment_food_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataSource = new ManagementFood(requireContext());
        recyclerView = view.findViewById(R.id.recycler_view_foods);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_food);

        setupRecyclerView();

        // --- SỬA LỖI LOGIC Ở ĐÂY ---
        // Khi nhấn nút FAB, mở AddEditFoodActivity để THÊM MỚI
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddEditFoodActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tải lại dữ liệu mỗi khi fragment này được hiển thị
        loadFoods();
    }

    private void setupRecyclerView() {
        adapter = new FoodAdminAdapter(foodList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadFoods() {
        List<FoodModel> foods = dataSource.getAllFoods();
        adapter.updateData(foods);
    }

    @Override
    public void onEditClick(FoodModel food) {
        // Khi nhấn vào một item, mở AddEditFoodActivity để SỬA
        Intent intent = new Intent(requireActivity(), AddEditFoodActivity.class);
        // Gửi theo đối tượng FoodModel cần sửa
        intent.putExtra("food_item", food);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(FoodModel food) {
        // Logic xóa vẫn dùng AlertDialog để xác nhận
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Food")
                .setMessage("Are you sure you want to delete " + food.getTitle() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dataSource.deleteFood(food.getId());
                    Toast.makeText(requireContext(), "Food deleted", Toast.LENGTH_SHORT).show();
                    loadFoods();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // --- PHƯƠNG THỨC showAddEditFoodDialog ĐÃ ĐƯỢC XÓA HOÀN TOÀN ---
}