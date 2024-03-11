package com.example.foobook_android.Repositories;

import androidx.lifecycle.LiveData;

import com.example.foobook_android.Api.SignUpApi;
import com.example.foobook_android.Api.SignUpRequest;
import com.example.foobook_android.Api.UserResponse;

public class signUpRepository {
    private SignUpApi userApi;

    public signUpRepository() {
        userApi = new SignUpApi();
    }

    public LiveData<UserResponse> registerUser(SignUpRequest signUpRequest) {
       return userApi.addUser(signUpRequest);
    }
}
