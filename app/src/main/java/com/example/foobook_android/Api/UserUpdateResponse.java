package com.example.foobook_android.Api;

import com.example.foobook_android.models.User;

// Represents the response from the server following a request to update user details.
public class UserUpdateResponse {

    // A message from the server about the outcome of the update request.
    String message;
    // The updated user object, reflecting the changes made.
    User user;

    // Returns the updated user object.
    public User getUser () {
        return this.user;
    }

    // Returns the server's response message.
    public String getMessage () {
        return this.message;
    }
}