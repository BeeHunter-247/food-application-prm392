package com.longtn.foodapplication.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longtn.foodapplication.R;
import com.longtn.foodapplication.adapter.UserAdminAdapter;
import com.longtn.foodapplication.helper.ManagementUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity implements UserAdminAdapter.OnUserListener {

    private RecyclerView recyclerView;
    private UserAdminAdapter adapter;
    private List<HashMap<String, String>> userList = new ArrayList<>();
    private ManagementUser dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        dataSource = new ManagementUser(this);
        recyclerView = findViewById(R.id.recycler_view_users);
        FloatingActionButton fab = findViewById(R.id.fab_add_user);

        setupRecyclerView();
        loadUsers();

        fab.setOnClickListener(v -> showAddEditUserDialog(null));
    }

    private void setupRecyclerView() {
        adapter = new UserAdminAdapter(userList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadUsers() {
        List<HashMap<String, String>> users = dataSource.getAllUsers();
        adapter.updateData(users);
    }

    @Override
    public void onEditClick(HashMap<String, String> user) {
        showAddEditUserDialog(user);
    }

    @Override
    public void onDeleteClick(HashMap<String, String> user) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa người dùng")
                .setMessage("Bạn có chắc chắn muốn xóa " + user.get("name") + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dataSource.deleteUser(Integer.parseInt(user.get("id")));
                    Toast.makeText(this, "Đã xóa người dùng", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showAddEditUserDialog(final HashMap<String, String> user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_edit_user, null);

        final EditText etName = view.findViewById(R.id.et_user_name);
        final EditText etEmail = view.findViewById(R.id.et_user_email);
        final EditText etPassword = view.findViewById(R.id.et_user_password);
        Button btnSave = view.findViewById(R.id.btn_save_user);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        if (user != null) { // Chế độ sửa
            etName.setText(user.get("name"));
            etEmail.setText(user.get("email"));
            etPassword.setHint("Để trống nếu không muốn đổi mật khẩu");
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên và email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user == null) { // Chế độ thêm mới
                if (password.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập mật khẩu cho người dùng mới", Toast.LENGTH_SHORT).show();
                    return;
                }
                dataSource.addUser(name, email, password);
                Toast.makeText(this, "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
            } else { // Chế độ sửa
                int userId = Integer.parseInt(user.get("id"));
                dataSource.updateUser(userId, name, email, password);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
            loadUsers();
            dialog.dismiss();
        });

        dialog.show();
    }
}