package com.longtn.foodapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.activity.DetailActivity;
import com.longtn.foodapplication.model.FoodModel;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private ArrayList<FoodModel> searchResultList;

    public SearchResultAdapter(ArrayList<FoodModel> searchResultList) {
        this.searchResultList = searchResultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommended, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodModel food = searchResultList.get(position);
        holder.title.setText(food.getTitle());
        holder.price.setText("$" + food.getPrice());

        int drawableResourceId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(food.getPicture(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.pic);

        holder.addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("object", food);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic, addBtn;
        TextView title, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            addBtn = itemView.findViewById(R.id.addBtn);
        }
    }
}