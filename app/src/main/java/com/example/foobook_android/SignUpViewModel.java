package com.example.foobook_android;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<UserResponse> userResponseLiveData;

    public SignUpViewModel() {
        userRepository = new UserRepository();
        userResponseLiveData = new MutableLiveData<>();
    }

    public void signUpUser(User newUser) {
        userRepository.registerUser(newUser).observeForever(userResponse -> {
            userResponseLiveData.postValue(userResponse);
        });
    }
    public LiveData<UserResponse> getUserResponseLiveData() {
        return userResponseLiveData;
    }

}
