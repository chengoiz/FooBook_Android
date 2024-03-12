package com.example.foobook_android.Api;



import com.example.foobook_android.utility.UserDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceApi {

    // signUp
    @POST("api/users")
    Call<UserResponse> registerUser(@Body SignUpRequest user);

    // login
    @POST("api/tokens")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("api/users/{id}")
    Call<UserDetails> getUserDetails(@Path("id") String userId, @Header("Authorization") String authToken);
}

