package com.example.foobook_android.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foobook_android.Api.FriendListResponse;
import com.example.foobook_android.Api.FriendRequestResponse;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.models.User;
import com.example.foobook_android.utility.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class FriendshipRepository {
    private final WebServiceApi webServiceApi;
    private final Context context;

    public FriendshipRepository(Context context) {
        this.context = context;
        String token = retrieveAuthToken();
        this.webServiceApi = RetrofitClient.getClient("http://10.0.2.2:8080/", "Bearer " + token).create(WebServiceApi.class);
    }

    private String retrieveAuthToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
    public void getFriendList(String userId, Callback<FriendListResponse> callback) {
        webServiceApi.fetchFriendList(userId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }

    public void getFriendRequests(String userId, Callback<FriendRequestResponse> callback) {
        webServiceApi.getFriendRequests(userId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }

    public void acceptFriendRequest(String userId, String friendId, Callback<Void> callback) {
        webServiceApi.acceptFriendRequest(userId, friendId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }

    public void declineFriendRequest(String userId, String friendId, Callback<Void> callback) {
        webServiceApi.declineOrRemoveFriend(userId, friendId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }
}
