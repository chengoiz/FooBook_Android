package com.example.foobook_android;

public class User {
    private String username;
    private String password;
    private String displayName;
    private String profilePicturePath;

    public User(String username, String password, String displayName, String profilePicturePath) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePicturePath = profilePicturePath;
    }

    public User(String username, String password, String displayName) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePicturePath = "drawable/defaultpic.png";
    }
}
