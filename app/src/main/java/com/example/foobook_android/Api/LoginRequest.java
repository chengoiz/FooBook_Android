package com.example.foobook_android.Api;

// Represents a request for logging in. This class encapsulates the username and password
// used for login attempts.
public class LoginRequest {
    private String username;
    private String password;

    // Constructor to create a new login request with a username and password.
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Gets the username associated with this login request.
    public String getUsername() {
        return username;
    }

    // Sets the username for this login request.
    public void setUsername(String username) {
        this.username = username;
    }

    // Gets the password associated with this login request.
    public String getPassword() {
        return password;
    }

    // Sets the password for this login request.
    public void setPassword(String password) {
        this.password = password;
    }
}
