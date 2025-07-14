package com.longtn.foodapplication.Interface;

// trong package interface/LoginContract.java

public interface LoginContract {
    interface View {
        void showLoginSuccess();
        void showLoginFailure(String error);
    }
    interface Presenter {
        void handleLogin(String email, String password);
    }
}
