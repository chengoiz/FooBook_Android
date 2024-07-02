package com.example.foobook_android.Repositories;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foobook_android.Api.UserUpdateRequest;
import com.example.foobook_android.Api.UserUpdateResponse;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.models.User;
import com.example.foobook_android.network.RetrofitClient;
import com.example.foobook_android.utility.TokenManager;
import com.example.foobook_android.utility.UserDetails;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Manages user data operations, including fetching, updating, and deleting user details.
public class UserRepository {
    // API interface for network requests.
    WebServiceApi webServiceApi;
    private final Context context;
    private final TokenManager tokenManager; // Manages access tokens for authenticated requests.

    // Constructor initializes context, token manager, and web service API.
    public UserRepository(Context context) {
        this.context = context.getApplicationContext();
        tokenManager = new TokenManager(context);
        webServiceApi = RetrofitClient.getClient().create(WebServiceApi.class);
    }

    // Callback interfaces for async operations results.
    public interface UserDetailsCallback {
        void onSuccess(UserDetails userDetails);
        void onError(Throwable throwable);
    }

    public interface DeleteUserCallback {
        void onSuccess();
        void onError(String message);
    }

    // Retrieves the current user's authentication token.
    private String getAuthToken() {
        return tokenManager.getToken();
    }

    // Retrieves the current user's ID.
    public String getUserId() {
        return tokenManager.getUserId();
    }

    // Sends a request to update user details and handles the response or failure.
    public void updateUserDetails(String userId, String displayName, String profilePicUri, UserDetailsCallback callback) {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(displayName, profilePicUri);

        webServiceApi.editUserDetails(userId, userUpdateRequest, "Bearer " + getAuthToken()).enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserUpdateResponse> call, @NonNull Response<UserUpdateResponse> response) {
                if (response.isSuccessful()) {
                    User user = Objects.requireNonNull(response.body()).getUser();
                    UserDetails userDetails = new UserDetails(user.getDisplayName(), user.getProfilePic(), null);
                    callback.onSuccess(userDetails);
                } else {
                    callback.onError(new RuntimeException("Error: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserUpdateResponse> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Sends a request to delete the current user and handles the response or failure.
    public void deleteUser(DeleteUserCallback callback) {
        webServiceApi.deleteUser("Bearer " + getAuthToken()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to delete user: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Fetches detailed information for a user based on their ID and handles the response or failure.
    public void fetchUserDetails(String Id, UserDetailsCallback callback) {

        if (Id == null || getAuthToken() == null) {
            Log.e("UserRepository", "User ID or Token is not available.");
            callback.onError(new IllegalStateException("User ID or Token is not available."));
            return;
        }

        Call<UserDetails> call = webServiceApi.getUserDetails(Id, "Bearer " + getAuthToken());
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(@NonNull Call<UserDetails> call, @NonNull Response<UserDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e("UserRepository", "Failed to fetch user details, HTTP error code: " + response.code());
                    callback.onError(new Exception("Failed to fetch user details"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDetails> call, @NonNull Throwable t) {
                Log.e("UserRepository", "Network error while fetching user details", t);
                callback.onError(t);
            }
        });
    }
}
