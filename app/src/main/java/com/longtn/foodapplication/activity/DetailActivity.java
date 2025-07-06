package com.longtn.foodapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementCart;
import com.longtn.foodapplication.model.FoodModel;

public class DetailActivity extends AppCompatActivity {
    private TextView addToCartBtn, titleTxt, priceTxt, descriptionTxt, numberOrderTxt, totalPriceTxt, startTxt, caloriesTxt, timeTxt;
    private ImageView plusBtn, minusBtn, picFood;
    private FoodModel object;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        managementCart = new ManagementCart(this);

        initView();
        getBundle();
    }

    private void getBundle() {
        object = (FoodModel)getIntent().getSerializableExtra("object");

        int drawableResourceId = this.getResources().getIdentifier(object.getPicture(), "drawable", this.getPackageName());
        Glide.with(this)
                .load(drawableResourceId)
                .into(picFood);

        titleTxt.setText(object.getTitle());
        priceTxt.setText("$" + object.getPrice());
        descriptionTxt.setText(object.getDescription());
        numberOrderTxt.setText(String.valueOf(numberOrder));
        caloriesTxt.setText(String.valueOf(object.getCalories()));
        startTxt.setText(String.valueOf(object.getStar()));
        timeTxt.setText(object.getTime() + "minutes");
        totalPriceTxt.setText("$" + Math.round(numberOrder * object.getPrice()));

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOrder = numberOrder + 1;
                numberOrderTxt.setText(String.valueOf(numberOrder));
                totalPriceTxt.setText("$" + Math.round(numberOrder * object.getPrice()));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOrder > 1) {
                    numberOrder = numberOrder - 1;
                }
                numberOrderTxt.setText(String.valueOf(numberOrder));
                totalPriceTxt.setText("$" + Math.round(numberOrder * object.getPrice()));
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(numberOrder);
                managementCart.insertFood(object);
            }
        });
    }

    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn);
        titleTxt = findViewById(R.id.timeTxt);
        priceTxt = findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        numberOrderTxt = findViewById(R.id.numberItemTxt);
        plusBtn = findViewById(R.id.plusCardBtn);
        minusBtn = findViewById(R.id.minusCardBtn);
        picFood = findViewById(R.id.foodDetailPic);
        startTxt = findViewById(R.id.starTxt);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        caloriesTxt = findViewById(R.id.caloriesTxt);
        timeTxt = findViewById(R.id.timeTxt);
    }
}
