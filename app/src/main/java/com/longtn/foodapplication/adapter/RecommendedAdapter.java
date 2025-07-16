package com.longtn.foodapplication.adapter;

import android.annotation.SuppressLint;
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

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.Viewholder> {
    ArrayList<FoodModel> recommendedList;

    public RecommendedAdapter(ArrayList<FoodModel> recommendedList) {
        this.recommendedList = recommendedList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommended, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(recommendedList.get(position).getTitle());
        holder.price.setText(String.valueOf(recommendedList.get(position).getPrice()));

        int drawableResouceId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(recommendedList.get(position).getPicture(),
                        "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResouceId).into(holder.pic);

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("object", recommendedList.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView pic, addBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            pic = itemView.findViewById(R.id.pic);
            price = itemView.findViewById(R.id.price);
            addBtn = itemView.findViewById(R.id.addBtn);
        }
    }
    public void updateData(ArrayList<FoodModel> newFoodList) {
        this.recommendedList.clear();
        this.recommendedList.addAll(newFoodList);
        notifyDataSetChanged(); // Báo cho RecyclerView biết dữ liệu đã thay đổi để vẽ lại
    }
}
