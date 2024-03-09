package com.example.foobook_android;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private UserApi userApi;

    public UserRepository() {
        userApi = new UserApi();
    }

    public LiveData<UserResponse> registerUser(User newUser) {
       return userApi.addUser(newUser);
    }
}
