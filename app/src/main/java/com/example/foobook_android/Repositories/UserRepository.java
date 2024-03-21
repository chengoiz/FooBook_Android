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
import com.example.foobook_android.utility.UserDetails;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {
    WebServiceApi webServiceApi;
    private final Context context;

    public UserRepository(Context context) {
        this.context = context.getApplicationContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public interface UserDetailsCallback {
        void onSuccess(UserDetails userDetails);

        void onError(Throwable throwable);
    }

    public interface DeleteUserCallback {
        void onSuccess();
        void onError(String message);
    }

    private String getAuthToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

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
