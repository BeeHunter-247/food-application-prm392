package com.longtn.foodapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtn.foodapplication.R;
import com.longtn.foodapplication.adapter.SearchResultAdapter;
import com.longtn.foodapplication.helper.ManagementFood;
import com.longtn.foodapplication.model.FoodModel;

import java.util.ArrayList;

public class ActivityListAll extends AppCompatActivity {
    private RecyclerView recyclerViewAllFoods;
    private SearchResultAdapter foodAdapter;
    private TextView titleText;
    private ImageView backBtn;
    private ManagementFood foodDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);

        initViews();
        loadAllFoods();
        setupBackButton();
    }

    private void initViews() {
        recyclerViewAllFoods = findViewById(R.id.recyclerViewAllFoods);
        titleText = findViewById(R.id.titleText);
        backBtn = findViewById(R.id.backBtn);

        foodDataSource = new ManagementFood(this);

        titleText.setText("All Foods");
    }

    private void loadAllFoods() {
        ArrayList<FoodModel> allFoods = (ArrayList<FoodModel>) foodDataSource.getAllFoods();

        recyclerViewAllFoods.setLayoutManager(new GridLayoutManager(this, 2));
        foodAdapter = new SearchResultAdapter(allFoods);
        recyclerViewAllFoods.setAdapter(foodAdapter);
    }

    private void setupBackButton() {
        backBtn.setOnClickListener(v -> finish());
    }
}