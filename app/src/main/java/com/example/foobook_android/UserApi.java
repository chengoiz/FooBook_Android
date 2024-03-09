package com.example.foobook_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    public static final int USERNAME_TAKEN = 400;
    public static final int SERVER_ERROR = 500;
    private Retrofit retrofit;
    WebServiceUserApi webServiceUserApi;

    public UserApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                build(); // change the baseUrl later
        webServiceUserApi = retrofit.create(WebServiceUserApi.class);
    }

    public LiveData<UserResponse> addUser(User newUser) {
        final MutableLiveData<UserResponse> userResponseData = new MutableLiveData<>();
        Call<UserResponse> call = webServiceUserApi.registerUser(newUser);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse responseBody;

                if (response.isSuccessful()) {
                    responseBody = response.body();
                    responseBody.setCode(response.code());
                    responseBody.setMessage(response.message());
                    userResponseData.setValue(responseBody);
                } else if (response.code() == USERNAME_TAKEN) {
                    try {
                        String errorString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorString);
                        String errorMessage = errorObject.getString("message");
                        userResponseData.setValue(new UserResponse(errorMessage, response.code()));
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                userResponseData.setValue(new UserResponse(t.getMessage(), SERVER_ERROR));
            }


        });
        return userResponseData;
    }


}
