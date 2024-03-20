package com.example.foobook_android.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobook_android.Api.FriendListResponse;
import com.example.foobook_android.Api.FriendRequestResponse;

import com.example.foobook_android.Repositories.FriendshipRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendshipViewModel extends AndroidViewModel {
    private final FriendshipRepository friendshipRepository;
    private final MutableLiveData<FriendRequestResponse> friendRequests = new MutableLiveData<>();
    private final MutableLiveData<FriendListResponse> friendList = new MutableLiveData<>();
    private MutableLiveData<Boolean> acceptRequestResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> declineRequestResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteFriend = new MutableLiveData<>();
    private MutableLiveData<String> friendRequestResponse = new MutableLiveData<>();


    public LiveData<Boolean> getAcceptRequestResult() {
        return acceptRequestResult;
    }

    public LiveData<Boolean> getDeclineRequestResult() {
        return declineRequestResult;
    }


    public FriendshipViewModel(@NonNull Application application) {
        super(application);
        friendshipRepository = new FriendshipRepository(application.getApplicationContext());
    }



    public LiveData<FriendRequestResponse> getFriendRequests(String userId, String authToken) {
        friendshipRepository.getFriendRequests(userId, authToken, new Callback<FriendRequestResponse>() {
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
                if (response.isSuccessful()) {
                    // Now fetch the updated friend list
                    acceptRequestResult.postValue(response.isSuccessful());
                    getFriendList(userId);
                } else {
                    Log.e("FriendRequestAccept", "Failed to accept friend request.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                acceptRequestResult.postValue(null);
                Log.e("FriendRequestAccept", "Error on accepting friend request: " + t.getMessage());
            }
        });
    }

    public LiveData<FriendRequestResponse> getFriendRequestsLiveData() {
        return friendRequests;
    }

    public LiveData<String> getFriendRequestResponse() {
        return friendRequestResponse;
    }

    public void sendFriendRequest(String currentUserId, String receiverUserId) {
        friendshipRepository.sendFriendRequest(currentUserId, receiverUserId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    friendRequestResponse.postValue("Friend request sent successfully!");
                } else {
                    friendRequestResponse.postValue("Failed to send friend request. HTTP status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                friendRequestResponse.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void declineFriendRequest(String userId, String friendId) {
        friendshipRepository.declineFriendRequest(userId, friendId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                declineRequestResult.postValue(response.isSuccessful());
                deleteFriend.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                declineRequestResult.postValue(false);
                deleteFriend.postValue(false);
            }
        });
    }


    public LiveData<FriendListResponse> getFriendList(String userId) {
        friendshipRepository.getFriendList(userId, new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendList.postValue(response.body());
                } else {
                    friendList.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
                friendList.postValue(null);
            }
        });
        return friendList;
    }

}
