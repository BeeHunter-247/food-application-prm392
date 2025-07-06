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
import com.longtn.foodapplication.model.FoodModel;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSearchResult;
    private SearchResultAdapter searchAdapter;
    private TextView searchTitle, noResultText;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        recyclerViewSearchResult = findViewById(R.id.recyclerViewSearchResult);
        searchTitle = findViewById(R.id.searchTitle);
        backBtn = findViewById(R.id.backBtn);
        noResultText = findViewById(R.id.noResultText);


        // Lấy dữ liệu từ Intent
        String query = getIntent().getStringExtra("searchQuery");
        ArrayList<FoodModel> searchResults = (ArrayList<FoodModel>) getIntent().getSerializableExtra("searchResults");

        // Cập nhật tiêu đề
        searchTitle.setText("Search Results for \"" + query + "\"");

        if(searchResults.isEmpty() || searchResults == null) {
            noResultText.setVisibility(View.VISIBLE);
            recyclerViewSearchResult.setVisibility(View.GONE);
        } else {
            noResultText.setVisibility(View.GONE);
            recyclerViewSearchResult.setVisibility(View.VISIBLE);

            recyclerViewSearchResult.setLayoutManager(new GridLayoutManager(this, 2));
            searchAdapter = new SearchResultAdapter(searchResults);
            recyclerViewSearchResult.setAdapter(searchAdapter);
        }

        backBtn.setOnClickListener(v -> finish());
    }
}