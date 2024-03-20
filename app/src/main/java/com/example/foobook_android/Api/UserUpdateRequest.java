package com.example.foobook_android.Api;

public class UserUpdateRequest {
    String displayname;
    String profilepic;
    public UserUpdateRequest(String displayName, String profilePic) {
        this.displayname = displayName;
        this.profilepic = profilePic;
    }
}
