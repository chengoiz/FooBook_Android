package com.example.foobook_android.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobook_android.Repositories.LoginRepository;
import com.example.foobook_android.Api.LoginRequest;
import com.example.foobook_android.Api.LoginResponse;

public class  LoginViewModel extends ViewModel {
    private final LoginRepository loginRepository;
    private final MutableLiveData<LoginResponse> loginResponseLiveData;

    public LoginViewModel() {
        this.loginRepository = new LoginRepository();
        this.loginResponseLiveData = new MutableLiveData<>();
    }
    public void login(LoginRequest loginRequest){
        loginRepository.login(loginRequest).observeForever(loginResponseLiveData::postValue);
    }
    public LiveData<LoginResponse> getloginResponseLiveData() {
        return loginResponseLiveData;
    }
}
