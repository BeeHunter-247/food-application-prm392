package com.longtn.foodapplication.presenter;

// trong package presenter/SignUpPresenter.java

import android.content.Context;
import android.util.Patterns;
import com.longtn.foodapplication.Interface.SignUpContract;
import com.longtn.foodapplication.helper.ManagementUser;

public class SignUpPresenter implements SignUpContract.Presenter {

    private SignUpContract.View view;
    private ManagementUser managementUser;

    public SignUpPresenter(SignUpContract.View view, Context context) {
        this.view = view;
        this.managementUser = new ManagementUser(context);
    }

    @Override
    public void handleSignUp(String name, String email, String password, String rePassword) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.showSignUpFailure("Please fill in all fields");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showSignUpFailure("Invalid email address");
            return;
        }
        if (password.length() < 6) {
            view.showSignUpFailure("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(rePassword)) {
            view.showSignUpFailure("Passwords do not match");
            return;
        }

        if (managementUser.addUser(name, email, password)) {
            view.showSignUpSuccess("Sign Up Successful! Please log in.");
        } else {
            view.showSignUpFailure("Email already exists");
        }
    }
}