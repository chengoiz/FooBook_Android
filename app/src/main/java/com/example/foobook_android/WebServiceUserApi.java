package com.example.foobook_android;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceUserApi {
    @POST("api/users")
    Call<UserResponse> registerUser(@Body User user);
}
