package com.example.foobook_android.Repositories;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.utility.RetrofitClient;
import com.example.foobook_android.utility.UserDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private WebServiceApi webServiceApi;
    private String token;
    private String userId;
    private Context context; // Add this

    public UserRepository(Context context) {
        this.context = context.getApplicationContext();
        token = getTokenFromSharedPreferences();
        userId = getIdFromSharedPreferences();
        this.webServiceApi = RetrofitClient.getClient("http://10.0.2.2:8080/", token).create(WebServiceApi.class);
    }

    public interface UserDetailsCallback {
        void onSuccess(UserDetails userDetails);

        void onError(Throwable throwable);
    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }

    public String getIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    public void fetchUserDetails(String Id, UserDetailsCallback callback) {

        if (Id == null || token == null) {
            Log.e("UserRepository", "User ID or Token is not available.");
            callback.onError(new IllegalStateException("User ID or Token is not available."));
            return;
        }

        Call<UserDetails> call = webServiceApi.getUserDetails(Id, "Bearer " + getTokenFromSharedPreferences());
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e("UserRepository", "Failed to fetch user details, HTTP error code: " + response.code());
                    callback.onError(new Exception("Failed to fetch user details"));
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e("UserRepository", "Network error while fetching user details", t);
                callback.onError(t);
            }
        });
    }
}
