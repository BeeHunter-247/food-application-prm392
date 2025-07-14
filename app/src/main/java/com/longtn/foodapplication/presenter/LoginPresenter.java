package com.longtn.foodapplication.presenter;

// trong package presenter/LoginPresenter.java

import android.content.Context;
import com.longtn.foodapplication.Interface.LoginContract;
import com.longtn.foodapplication.helper.ManagementUser;
import com.longtn.foodapplication.helper.SessionManager;

import java.util.HashMap;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private ManagementUser managementUser;

    private SessionManager sessionManager;

    private Context _context;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this._context = context;
        this.managementUser = new ManagementUser(context);
        this.sessionManager = new SessionManager(context);
    }



    @Override
    public void handleLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.showLoginFailure("Please enter email and password");
            return;
        }

        if (managementUser.checkUser(email, password)) {
            // Lấy thông tin người dùng
            HashMap<String, String> userDetails = managementUser.getUserByEmail(email);
            String name = userDetails.get("name");

            // Dùng sessionManager đã được khởi tạo để tạo session
            sessionManager.createLoginSession(name, email);

            view.showLoginSuccess();
        } else {
            view.showLoginFailure("Email or Password is not correct");
        }
    }

}