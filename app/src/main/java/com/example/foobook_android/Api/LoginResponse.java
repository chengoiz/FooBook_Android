package com.example.foobook_android.Api;

// Represents the server's response to a login request. Contains the outcome of the login attempt,
// a token if successful, the user's ID, and a reason for failure if applicable.
public class LoginResponse {
    private final String result;
    private String token;
    private String userId;
    private final String reason;

    // Constructs a new LoginResponse with the given details
    public LoginResponse(String result, String token, String userId, String reason) {
        this.result = result;
        this.token = token;
        this.userId = userId;
        this.reason = reason;
    }

    // Gets the result of the login attempt
    public String getResult() {
        return result;
    }

    // Gets the authentication token provided on successful login
    public String getToken() {
        return token;
    }

    // Sets the authentication token
    public void setToken(String token) {
        this.token = token;
    }

    // Gets the unique identifier for the user
    public String getUserId() {
        return userId;
    }

    // Sets the user's unique identifier
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Gets the reason for login failure
    public String getReason() {
        return reason;
    }

}

