package com.example.foobook_android.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.models.Friendship;
import com.example.foobook_android.models.FriendshipRequest;
import com.example.foobook_android.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendshipViewModel extends AndroidViewModel {
    private MutableLiveData<List<User>> friendRequests = new MutableLiveData<>();
    private WebServiceApi webServiceApi; // Initialized elsewhere with Retrofit

    public FriendshipViewModel(@NonNull Application application) {
        super(application);
        // Initialize your WebServiceApi instance here, including the authentication token
    }

    public LiveData<List<User>> getFriendRequests(String userId) {
        String authToken = "Bearer " + retrieveAuthToken(); // Implement retrieveAuthToken to get the current user's auth token
        webServiceApi.getFriendRequests(userId, authToken).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    friendRequests.postValue(response.body());
                } else {
                    // Handle failure
                    friendRequests.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Handle network failure
                friendRequests.postValue(null);
            }
        });
        return friendRequests;
    }

    private String retrieveAuthToken() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }
}
