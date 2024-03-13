package com.example.foobook_android.Api;



import com.example.foobook_android.models.User;
import com.example.foobook_android.utility.UserDetails;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
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

    // Fetch friend requests for a user
    @GET("users/{userId}/friend-requests")
    Call<FriendRequestResponse> getFriendRequests(@Path("userId") String userId, @Header("Authorization") String authToken);

    // Send a friend request
    @POST("users/{userId}/friends")
    Call<User> sendFriendRequest(@Path("userId") String receiverId, @Body RequestBody requestBody, @Header("Authorization") String authToken);

    // Accept a friend request
    @PATCH("users/{userId}/friends/{fid}")
    Call<Void> acceptFriendRequest(@Path("userId") String userId, @Path("fid") String friendId, @Header("Authorization") String authToken);

    // Decline or remove a friend request/friend
    @DELETE("users/{userId}/friends/{fid}")
    Call<Void> declineOrRemoveFriend(@Path("userId") String userId, @Path("fid") String friendId, @Header("Authorization") String authToken);
}
