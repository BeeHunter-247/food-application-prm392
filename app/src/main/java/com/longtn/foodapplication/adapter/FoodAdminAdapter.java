package com.longtn.foodapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.model.FoodModel;
import java.util.List;

public class FoodAdminAdapter extends RecyclerView.Adapter<FoodAdminAdapter.FoodViewHolder> {

    private List<FoodModel> foodList;
    private OnFoodListener listener;

    public interface OnFoodListener {
        void onEditClick(FoodModel food);
        void onDeleteClick(FoodModel food);
    }

    public FoodAdminAdapter(List<FoodModel> foodList, OnFoodListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_admin, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodModel food = foodList.get(position);
        holder.tvFoodTitle.setText(food.getTitle());
        holder.tvFoodPrice.setText(String.format("%,.0f VND", food.getPrice()));

        // Nhấn vào item để Sửa
        holder.itemView.setOnClickListener(v -> listener.onEditClick(food));
        // Nhấn giữ item để Xóa
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDeleteClick(food);
            return true;
        });
    }

    @Override public int getItemCount() { return foodList.size(); }

    public void updateData(List<FoodModel> newFoods) {
        this.foodList.clear();
        this.foodList.addAll(newFoods);
        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodTitle, tvFoodPrice;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodTitle = itemView.findViewById(R.id.tv_food_title);
            tvFoodPrice = itemView.findViewById(R.id.tv_food_price);
        }
    }
}