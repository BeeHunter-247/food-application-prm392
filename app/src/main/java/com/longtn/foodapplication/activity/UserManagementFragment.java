package com.longtn.foodapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.activity.AddEditUserActivity;
import com.longtn.foodapplication.adapter.UserAdminAdapter;
import com.longtn.foodapplication.helper.ManagementUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManagementFragment extends Fragment implements UserAdminAdapter.OnUserListener {

    private RecyclerView recyclerView;
    private UserAdminAdapter adapter;
    private List<HashMap<String, String>> userList = new ArrayList<>();
    private ManagementUser dataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataSource = new ManagementUser(requireContext());
        recyclerView = view.findViewById(R.id.recycler_view_users);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_user);

        setupRecyclerView();
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddEditUserActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUsers();
    }

    private void setupRecyclerView() {
        adapter = new UserAdminAdapter(userList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadUsers() {
        List<HashMap<String, String>> users = dataSource.getAllUsers();
        adapter.updateData(users);
    }

    @Override
    public void onEditClick(HashMap<String, String> user) {
        Intent intent = new Intent(requireActivity(), AddEditUserActivity.class);
        intent.putExtra("user_item", user);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(HashMap<String, String> user) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.get("name") + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dataSource.deleteUser(Integer.parseInt(user.get("id")));
                    Toast.makeText(requireContext(), "User deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}