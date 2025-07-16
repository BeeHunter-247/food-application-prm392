package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtn.foodapplication.R;
import com.longtn.foodapplication.adapter.CategoryAdapter;
import com.longtn.foodapplication.adapter.RecommendedAdapter;
import com.longtn.foodapplication.helper.ManagementFood;
import com.longtn.foodapplication.model.CategoryModel;
import com.longtn.foodapplication.model.FoodModel;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CategoryAdapter adapter; // Giữ nguyên adapter cho Category
    private RecommendedAdapter adapter2; // <-- THAY ĐỔI QUAN TRỌNG: Đổi kiểu thành RecommendedAdapter
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    private ArrayList<FoodModel> foodList = new ArrayList<>();
    private EditText searchView;
    private TextView greetingText;
    private ImageView avatarImage;
    private Button loginButton;
    private ManagementFood foodDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        foodDataSource = new ManagementFood(this);
        // Ánh xạ các view
        greetingText = findViewById(R.id.textView5);
        avatarImage = findViewById(R.id.imageView4);
        loginButton = findViewById(R.id.loginBtn);

        // Kiểm tra trạng thái đăng nhập và cập nhật UI
        checkLoginStatus();

        recyclerViewCategory();

        bottomNavigation();
        setupSearchEditText();
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadPopularFoods();
    }
    private void setupSearchEditText() {
        searchView = findViewById(R.id.searchView);

        searchView.setOnKeyListener((v, keyCode, event) -> {
            if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String query = searchView.getText().toString().trim();
                if(!query.isEmpty()) {
                    startSearchResultActivity(query);
                }
                return true;
            }
            return false;
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkLoginStatus() {
        // --- GIẢ LẬP TRẠNG THÁI ĐĂNG NHẬP ---
        // Trong một ứng dụng thực tế, bạn sẽ kiểm tra từ SharedPreferences, Firebase Auth, etc.
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isUserLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Thay đổi thành true để xem trạng thái đã đăng nhập

        if (isUserLoggedIn) {
            // Nếu người dùng đã đăng nhập
            String username = sharedPreferences.getString("username", "User"); // Đọc username, mặc định là "User"
            greetingText.setText("Hi " + username);

            greetingText.setVisibility(View.VISIBLE);
            avatarImage.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            avatarImage.setOnClickListener(v -> {
                logout();
            });
        } else {
            // Nếu người dùng chưa đăng nhập
            greetingText.setVisibility(View.GONE);
            avatarImage.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);

            // Kích hoạt nút Login để mở LoginActivity
            loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }
    private void logout() {
        // Xóa dữ liệu trong SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Xóa tất cả dữ liệu đã lưu
        editor.apply();

        // Khởi động lại MainActivity để cập nhật UI
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void startSearchResultActivity(String query) {
        ArrayList<FoodModel> filterList = new ArrayList<>();
        for (FoodModel food : foodList) {
            if(food.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    food.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filterList.add(food);
            }
        }

        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("searchQuery", query);
        intent.putExtra("searchResults", filterList);
        startActivity(intent);
    }

    private void bottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout carBtnMain = findViewById(R.id.cardBtnMain);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        carBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }

    private void loadPopularFoods(){
        if(recyclerViewPopularList == null){
            recyclerViewPopularList = findViewById(R.id.RecyclePopular);
            recyclerViewPopularList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            adapter2 = new RecommendedAdapter(foodList);
            recyclerViewPopularList.setAdapter(adapter2);
        }

        ArrayList<FoodModel> foodFromDb = (ArrayList<FoodModel>) foodDataSource.getAllFoods();

        adapter2.updateData(foodFromDb);
    }
    private void recyclerViewPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularList = findViewById(R.id.RecyclePopular);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);

        foodList = (ArrayList<FoodModel>) foodDataSource.getAllFoods();
        adapter2 = new RecommendedAdapter(foodList);
        recyclerViewPopularList.setAdapter(adapter2);
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.RecycleCategory);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryModel> categoryList = new ArrayList<>();
        categoryList.add(new CategoryModel("pizza", "Pizza"));
        categoryList.add(new CategoryModel("burger", "Burger"));
        categoryList.add(new CategoryModel("hotdog", "Hotdog"));
        categoryList.add(new CategoryModel("drink", "Drink"));
        categoryList.add(new CategoryModel("donut", "Donut"));

        adapter = new CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(adapter);
    }
}