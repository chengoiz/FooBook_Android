package com.example.foobook_android.Repositories;

import androidx.lifecycle.LiveData;
import com.example.foobook_android.Api.SignUpApi;
import com.example.foobook_android.Api.SignUpRequest;
import com.example.foobook_android.Api.UserResponse;

// Handles the registration of new users by interacting with the SignUpApi.
public class SignUpRepository {
    // Instance of SignUpApi to make API calls for user registration.
    private final SignUpApi userApi;

    // Constructor initializes the SignUpApi.
    public SignUpRepository() {
        userApi = new SignUpApi();
    }

    // Registers a new user with the provided SignUpRequest information.
    // Returns LiveData containing the response from the server.
    public LiveData<UserResponse> registerUser(SignUpRequest signUpRequest) {
        return userApi.addUser(signUpRequest);
    }
}