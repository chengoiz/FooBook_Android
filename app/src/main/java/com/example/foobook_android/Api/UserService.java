package com.example.foobook_android.Api;

import com.example.foobook_android.utility.UserDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    @GET("api/users/{id}")
    Call<UserDetails> getUserDetails(@Path("id") String userId);
}