package com.longtn.foodapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.longtn.foodapplication.Interface.ChangeNumberItemsListener;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.helper.ManagementCart;
import com.longtn.foodapplication.model.FoodModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    ArrayList<FoodModel> listFoodSelected;
    private ManagementCart managementCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<FoodModel> listFoodSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listFoodSelected = listFoodSelected;
        managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, int position) {
        holder.title.setText(listFoodSelected.get(position).getTitle());
        holder.priceEachItem.setText("$" + listFoodSelected.get(position).getPrice());
        holder.totalEachItem.setText("$" + Math.round((listFoodSelected.get(position).getNumberInCart() * listFoodSelected.get(position).getPrice())));
        holder.num.setText(String.valueOf(listFoodSelected.get(position).getNumberInCart()));

        int drawableResouceId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(listFoodSelected.get(position).getPicture(),
                        "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResouceId).into(holder.pic);

        holder.plusItem.setOnClickListener(v -> {
            managementCart.plusNumberFood(listFoodSelected, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.changed();
            });
        });

        holder.minusItem.setOnClickListener(v -> {
            managementCart.minusNumberFood(listFoodSelected, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.changed();
            });
        });
    }

    @Override
    public int getItemCount() {
        return listFoodSelected.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, priceEachItem;
        ImageView pic, minusItem, plusItem;
        TextView totalEachItem, num;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            pic = itemView.findViewById(R.id.pic);
            priceEachItem = itemView.findViewById(R.id.priceEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            plusItem = itemView.findViewById(R.id.plusCardBtn);
            minusItem = itemView.findViewById(R.id.minusCardBtn);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }
}
