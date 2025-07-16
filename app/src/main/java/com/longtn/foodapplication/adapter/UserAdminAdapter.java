package com.longtn.foodapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.longtn.foodapplication.R;
import java.util.HashMap;
import java.util.List;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserViewHolder> {

    private List<HashMap<String, String>> userList;
    private OnUserListener listener;

    public interface OnUserListener {
        void onEditClick(HashMap<String, String> user);
        void onDeleteClick(HashMap<String, String> user);
    }

    public UserAdminAdapter(List<HashMap<String, String>> userList, OnUserListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_admin, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        HashMap<String, String> user = userList.get(position);
        holder.tvUserName.setText(user.get("name"));
        holder.tvUserEmail.setText(user.get("email"));

        holder.itemView.setOnClickListener(v -> listener.onEditClick(user));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDeleteClick(user);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(List<HashMap<String, String>> newUsers) {
        this.userList.clear();
        this.userList.addAll(newUsers);
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
        }
    }
}
