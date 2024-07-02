package com.example.foobook_android.Api;

// Encapsulates data for a request to update user details, such as display name and profile picture.
public class UserUpdateRequest {
    // The user's new display name.
    String displayname;
    // Path to the user's new profile picture.
    String profilepic;

    // Constructs a new request with specified display name and profile picture.
    public UserUpdateRequest(String displayName, String profilePic) {
        this.displayname = displayName;
        this.profilepic = profilePic;
    }
}
