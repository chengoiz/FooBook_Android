package com.example.foobook_android.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobook_android.Api.SignUpRequest;
import com.example.foobook_android.Api.UserResponse;
import com.example.foobook_android.Repositories.signUpRepository;

public class SignUpViewModel extends ViewModel {
    private final com.example.foobook_android.Repositories.signUpRepository signUpRepository;
    private final MutableLiveData<UserResponse> userResponseLiveData;

    public SignUpViewModel() {
        signUpRepository = new signUpRepository();
        userResponseLiveData = new MutableLiveData<>();
    }

    public void signUpUser(SignUpRequest newUser) {
        signUpRepository.registerUser(newUser).observeForever(userResponseLiveData::postValue);
    }
    public LiveData<UserResponse> getUserResponseLiveData() {
        return userResponseLiveData;
    }

}
