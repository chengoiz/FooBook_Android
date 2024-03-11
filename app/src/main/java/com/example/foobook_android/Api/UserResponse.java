package com.example.foobook_android.Api;

public class UserResponse {
    private String message;

    public UserResponse(String message) {
        this.message = message;

    }
    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

}

