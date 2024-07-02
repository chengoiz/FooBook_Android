package com.example.foobook_android.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.example.foobook_android.Repositories.LoginRepository;
import com.example.foobook_android.Api.LoginRequest;
import com.example.foobook_android.Api.LoginResponse;
import com.example.foobook_android.utility.UserDetails;

// View model for managing login-related data.
public class  LoginViewModel extends ViewModel {
    // Repository for handling login-related operations.
    private final LoginRepository loginRepository;

    // Mutable live data objects for observing data changes.
    private final MutableLiveData<LoginResponse> loginResponseLiveData;
    private final MutableLiveData<UserDetails> userDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    // Constructor
    public LoginViewModel() {
        this.loginRepository = new LoginRepository();
        this.loginResponseLiveData = new MutableLiveData<>();
    }

    // Initiates the login process
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

    // Fetches user details after successful login
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

    // Getter for observing login response
    public LiveData<LoginResponse> getloginResponseLiveData() {
        return loginResponseLiveData;
    }

    // Getter for observing user details
    public LiveData<UserDetails> getUserDetailsLiveData() {
        return userDetailsLiveData;
    }

    // Getter for observing errors
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
