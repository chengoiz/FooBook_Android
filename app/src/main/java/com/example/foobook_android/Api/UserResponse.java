package com.example.foobook_android.Api;

// Represents a generic response from the server related to user operations. It contains
// a message that can indicate success, failure, or details about the operation performed.
public class UserResponse {
    // Message from the server providing details about the result of the operation.
    private String message;

    // Constructor to create a UserResponse with a specific message.
    public UserResponse(String message) {
        this.message = message;
    }

    // Returns the message associated with the user operation response.
    public String getMessage() {
        return message;
    }

    // Updates the response message.
    public void setMessage(String message) {
        this.message = message;
    }

}