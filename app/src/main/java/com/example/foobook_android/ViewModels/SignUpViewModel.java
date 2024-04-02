package com.example.foobook_android.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.foobook_android.Api.SignUpRequest;
import com.example.foobook_android.Api.UserResponse;
import com.example.foobook_android.Repositories.SignUpRepository;

// View model for handling sign-up operations.
public class SignUpViewModel extends ViewModel {

    // Repository for handling sign-up operations
    private final SignUpRepository signUpRepository;

    // Mutable live data object for observing user response
    private final MutableLiveData<UserResponse> userResponseLiveData;

    // Constructor
    public SignUpViewModel() {
        signUpRepository = new SignUpRepository();
        userResponseLiveData = new MutableLiveData<>();
    }

    // Signs up a user
    public void signUpUser(SignUpRequest newUser) {
        signUpRepository.registerUser(newUser).observeForever(userResponseLiveData::postValue);
    }

    // Getter for observing user response
    public LiveData<UserResponse> getUserResponseLiveData() {
        return userResponseLiveData;
    }

}
