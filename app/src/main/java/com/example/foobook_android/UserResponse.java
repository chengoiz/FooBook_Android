package com.example.foobook_android;

public class UserResponse {
    private String message;
    private int code;

    // Constructor
    public UserResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Getter for success
    public int getCode() {
        return code;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Setter for success
    public void setCode(int code) {
        this.code = code;
    }
}

