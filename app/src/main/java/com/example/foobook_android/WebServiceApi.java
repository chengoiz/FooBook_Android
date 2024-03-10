package com.example.foobook_android;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceApi {

    // signUp
    @POST("api/users")
    Call<UserResponse> registerUser(@Body User user);
/*
    // login
    @POST("api/tokens")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

 */
}

