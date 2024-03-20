package com.example.foobook_android.Api;

public class LoginResponse {
    private String result;
    private String token;
    private String userId;
    private String reason;

    public LoginResponse(String result, String token, String userId, String reason) {
        this.result = result;
        this.token = token;
        this.userId = userId;
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

