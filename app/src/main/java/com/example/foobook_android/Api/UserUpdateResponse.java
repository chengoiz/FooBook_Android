package com.example.foobook_android.Api;

import com.example.foobook_android.models.User;

public class UserUpdateResponse {
    String message;
    User user;
    public User getUser () {
        return this.user;
    }
    public String getMessage () {
        return this.message;
    }
}