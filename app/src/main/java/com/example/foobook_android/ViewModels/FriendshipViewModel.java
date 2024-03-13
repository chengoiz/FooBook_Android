package com.example.foobook_android.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobook_android.Api.FriendRequestResponse;

import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.models.Friendship;
import com.example.foobook_android.models.FriendshipRequest;

import com.example.foobook_android.models.User;
import com.example.foobook_android.Repositories.FriendshipRepository;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendshipViewModel extends AndroidViewModel {
    private FriendshipRepository friendshipRepository;
    private MutableLiveData<FriendRequestResponse> friendRequests = new MutableLiveData<>();

    public FriendshipViewModel(@NonNull Application application) {
        super(application);
        friendshipRepository = new FriendshipRepository(application.getApplicationContext());
    }

    public LiveData<FriendRequestResponse> getFriendRequests(String userId) {
        friendshipRepository.getFriendRequests(userId, new Callback<FriendRequestResponse>() {
            @Override
            public void onResponse(Call<FriendRequestResponse> call, Response<FriendRequestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendRequests.postValue(response.body());
                } else {
                    friendRequests.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<FriendRequestResponse> call, Throwable t) {
                friendRequests.postValue(null);
            }
        });
        return friendRequests;
    }

    public void acceptFriendRequest(String userId, String friendId) {
        friendshipRepository.acceptFriendRequest(userId, friendId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Optionally, update LiveData or take other actions on success
                //
                //
                //
                ///

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void declineFriendRequest(String userId, String friendId) {
        friendshipRepository.declineFriendRequest(userId, friendId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Optionally, update LiveData or take other actions on success
                //
                //
                //
                //
                //
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

}
