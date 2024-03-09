package com.example.foobook_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    private UserRepository userRepository;

    public SignUpViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<UserResponse> signUpUser(User newUser) {
        return userRepository.registerUser(newUser);
    }
}
