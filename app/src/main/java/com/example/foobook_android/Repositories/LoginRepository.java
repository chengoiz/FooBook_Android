package com.example.foobook_android.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobook_android.Api.LoginResponse;
import com.example.foobook_android.Api.LoginRequest;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.utility.UserDetails;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository {
    WebServiceApi webServiceApi;

    public LoginRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public LiveData<LoginResponse> login(LoginRequest loginRequest) {
        final MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();
        Call<LoginResponse> call = webServiceApi.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    loginResponseMutableLiveData.setValue(response.body());
                } else {
                    // Parse error response
                    String errorMessage = "Login failed"; // Fallback error message
                    if (response.errorBody() != null) {
                        try {
                            String errorBodyStr = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBodyStr);
                            errorMessage = jsonObject.optString("reason", "Unknown error occurred");
                        } catch (Exception e) {
                            e.printStackTrace(); // Log parsing error
                        }
                    }
                    loginResponseMutableLiveData.setValue(new LoginResponse("Failure", null, null, errorMessage));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginResponseMutableLiveData.setValue(new LoginResponse("Failure", null, null, t.getMessage()));
            }
        });

        return loginResponseMutableLiveData;
    }

    public LiveData<UserDetails> fetchUserDetails(String userId, String token) {
        final MutableLiveData<UserDetails> userDetailsLiveData = new MutableLiveData<>();
        webServiceApi.getUserDetails(userId, "Bearer " + token).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(@NonNull Call<UserDetails> call, @NonNull Response<UserDetails> response) {
                if (response.isSuccessful()) {
                    userDetailsLiveData.postValue(response.body());
                } else {
                    userDetailsLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDetails> call, @NonNull Throwable t) {
                userDetailsLiveData.postValue(null);
            }
        });
        return userDetailsLiveData;
    }
}
