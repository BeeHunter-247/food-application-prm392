package com.longtn.foodapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.model.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    ArrayList<CategoryModel> categoryList;

    public CategoryAdapter(ArrayList<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
        holder.categoryName.setText(categoryList.get(position).getTitle());
        String picUrl = "";
        switch (position) {
            case 0: {
                picUrl = "pizza";
                break;
            }
            case 1: {
                picUrl = "burger";
                break;
            }
            case 2: {
                picUrl = "hotdog";
                break;
            }
            case 3: {
                picUrl = "drink";
                break;
            }
            case 4: {
                picUrl = "donut";
                break;
            }
        }

        int drawableResouceId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResouceId).into(holder.categoryPicture);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPicture;
        ConstraintLayout mainLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPicture = itemView.findViewById(R.id.categoryPic);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
