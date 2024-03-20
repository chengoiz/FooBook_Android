package com.example.foobook_android.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.foobook_android.Repositories.LoginRepository;
import com.example.foobook_android.Api.LoginRequest;
import com.example.foobook_android.Api.LoginResponse;
import com.example.foobook_android.utility.UserDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  LoginViewModel extends ViewModel {
    private final LoginRepository loginRepository;
    private final MutableLiveData<LoginResponse> loginResponseLiveData;
    private final MutableLiveData<UserDetails> userDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LoginViewModel() {
        this.loginRepository = new LoginRepository();
        this.loginResponseLiveData = new MutableLiveData<>();
    }
    public void login(LoginRequest loginRequest) {
        loginRepository.login(loginRequest).observeForever(response -> {
            if (response != null && "Success".equals(response.getResult())) {
                loginResponseLiveData.postValue(response);
                fetchUserDetails(response.getUserId(), response.getToken());
            } else if (response != null) {
                errorLiveData.postValue(response.getReason());
            }
        });
    }

    private void fetchUserDetails(String userId, String token) {
        LiveData<UserDetails> repoLiveData = loginRepository.fetchUserDetails(userId, token);
        repoLiveData.observeForever(new Observer<UserDetails>() {
            @Override
            public void onChanged(UserDetails userDetails) {
                if (userDetails != null) {
                    userDetailsLiveData.postValue(userDetails);
                } else {
                    errorLiveData.postValue("Failed to load user details.");
                }
                // This is a one-time observation, we should remove it after receiving a response
                repoLiveData.removeObserver(this);
            }
        });
    }

    public LiveData<LoginResponse> getloginResponseLiveData() {
        return loginResponseLiveData;
    }

    public LiveData<UserDetails> getUserDetailsLiveData() {
        return userDetailsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
