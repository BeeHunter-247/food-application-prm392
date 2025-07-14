package com.longtn.foodapplication.Interface;

public interface SignUpContract {
    interface View {
        void showSignUpSuccess(String message);
        void showSignUpFailure(String error);
        void showValidationError(String field, String message);
    }

    interface Presenter {
        void handleSignUp(String name, String email, String password, String rePassword);
    }
}
