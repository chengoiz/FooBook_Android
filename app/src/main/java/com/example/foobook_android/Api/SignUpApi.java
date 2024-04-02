package com.example.foobook_android.Api;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Manages sign-up operations by communicating with the server API. Uses Retrofit for network calls.
public class SignUpApi {
    WebServiceApi webServiceApi;

    // Constructor initializes Retrofit and creates an instance of the WebServiceApi interface.
    public SignUpApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    // Sends a user sign-up request to the server and returns a LiveData object containing the response.
    public LiveData<UserResponse> addUser(SignUpRequest signUpRequest) {
        final MutableLiveData<UserResponse> userResponseData = new MutableLiveData<>();

        // Enqueue the sign-up request asynchronously.
        Call<UserResponse> call = webServiceApi.registerUser(signUpRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                // Handle successful response from the server.
                if (response.isSuccessful() && response.body() != null) {
                    // Post the successful response back to the LiveData.
                    userResponseData.postValue(response.body());
                } else {
                    // Handle errors and post a custom error message back to the LiveData.
                    try {
                        String errorString = response.errorBody() != null ? response.errorBody().string() : "{}";
                        JSONObject errorObject = new JSONObject(errorString);
                        String errorMessage = errorObject.getString("message");
                        userResponseData.postValue(new UserResponse(errorMessage));
                    } catch (Exception e) {
                        userResponseData.postValue(new UserResponse("Error parsing error response"));
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                userResponseData.setValue(new UserResponse(t.getMessage()));
            }
        });
        return userResponseData; // Return the LiveData object to the caller.
    }
}
