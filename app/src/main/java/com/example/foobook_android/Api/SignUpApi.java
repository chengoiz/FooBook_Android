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

public class SignUpApi {
    WebServiceApi webServiceApi;

    public SignUpApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public LiveData<UserResponse> addUser(SignUpRequest signUpRequest) {
        final MutableLiveData<UserResponse> userResponseData = new MutableLiveData<>();
        Call<UserResponse> call = webServiceApi.registerUser(signUpRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userResponseData.postValue(response.body());
                } else {
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
        return userResponseData;
    }
}
