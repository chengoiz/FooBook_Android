package com.example.foobook_android.Repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.foobook_android.Api.FriendListResponse;
import com.example.foobook_android.Api.FriendRequestResponse;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.models.FriendshipRequest;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendshipRepository {
    private Retrofit retrofit;
    private final WebServiceApi webServiceApi;
    private final Context context;

    public FriendshipRepository(Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    private String retrieveAuthToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
    public void getFriendList(String userId, Callback<FriendListResponse> callback) {
        webServiceApi.fetchFriendList(userId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }

    public void sendFriendRequest(String currentUserId, String receiverUserId, Callback<Void> callback) {
        FriendshipRequest friendshipRequest = new FriendshipRequest(currentUserId, receiverUserId);
        Gson gson = new Gson();
        String json = gson.toJson(friendshipRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        webServiceApi.sendFriendRequest(receiverUserId, requestBody, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }

    public void getFriendRequests(String userId, String authToken, Callback<FriendRequestResponse> callback) {
        webServiceApi.getFriendRequests(userId, authToken).enqueue(callback);
    }

    public void acceptFriendRequest(String userId, String friendId, Callback<Void> callback) {
        webServiceApi.acceptFriendRequest(userId, friendId, "Bearer " + retrieveAuthToken()).enqueue(callback);
        Log.e("FriendRequestAccept", "Accepted friend request from " + friendId + " into the friendlist of: " + userId);
    }

    public void declineFriendRequest(String userId, String friendId, Callback<Void> callback) {
        webServiceApi.declineOrRemoveFriend(userId, friendId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }
}
