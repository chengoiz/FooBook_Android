package com.example.foobook_android.Repositories;

import android.content.Context;
import android.util.Log;
import com.example.foobook_android.Api.FriendListResponse;
import com.example.foobook_android.Api.FriendRequestResponse;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.models.FriendshipRequest;
import com.example.foobook_android.network.RetrofitClient;
import com.example.foobook_android.utility.TokenManager;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;

// Handles all network operations related to friendships, such as fetching friends list,
// sending friend requests, and responding to them.
public class FriendshipRepository {
    private final WebServiceApi webServiceApi; // API interface for making network requests.
    private final TokenManager tokenManager; // Manages access tokens for authenticated requests.

    // Initializes the repository with a context, setting up the web service API and token manager.
    public FriendshipRepository(Context context) {
        tokenManager = new TokenManager(context);
        webServiceApi = RetrofitClient.getClient().create(WebServiceApi.class);
    }

    // Retrieves the current user's authentication token.
    private String retrieveAuthToken() {
        return tokenManager.getToken();
    }

    // Fetches the user's friends list from the server.
    public void getFriendList(String userId, Callback<FriendListResponse> callback) {
        webServiceApi.fetchFriendList(userId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }

    // Sends a friend request to another user.
    public void sendFriendRequest(String currentUserId, String receiverUserId, Callback<Void> callback) {
        FriendshipRequest friendshipRequest = new FriendshipRequest(currentUserId, receiverUserId);
        Gson gson = new Gson();
        String json = gson.toJson(friendshipRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        webServiceApi.sendFriendRequest(receiverUserId, requestBody, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }

    // Fetches incoming friend requests for the user.
    public void getFriendRequests(String userId, String authToken, Callback<FriendRequestResponse> callback) {
        webServiceApi.getFriendRequests(userId, authToken).enqueue(callback);
    }

    // Accepts a friend request from another user.
    public void acceptFriendRequest(String userId, String friendId, Callback<Void> callback) {
        webServiceApi.acceptFriendRequest(userId, friendId, "Bearer " + retrieveAuthToken()).enqueue(callback);
        Log.e("FriendRequestAccept", "Accepted friend request from " + friendId + " into the friendlist of: " + userId);
    }

    // Declines a friend request or removes a friend from the user's friends list.
    public void declineFriendRequest(String userId, String friendId, Callback<Void> callback) {
        webServiceApi.declineOrRemoveFriend(userId, friendId, "Bearer " + retrieveAuthToken()).enqueue(callback);
    }
}
