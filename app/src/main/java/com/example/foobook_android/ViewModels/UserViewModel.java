package com.example.foobook_android.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.utility.UserDetails;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private MutableLiveData<UserDetails> userDetailsLiveData;
    private MutableLiveData<String> errorLiveData;

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userDetailsLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<UserDetails> getUserDetailsLiveData() {
        return userDetailsLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void updateUserDetails(String displayName, String profilePicUri) {
        // The userId should be retrieved from the repository or ViewModel's stored data.
        String userId = userRepository.getUserId();

        userRepository.updateUserDetails(userId, displayName, profilePicUri, new UserRepository.UserDetailsCallback() {
            @Override
            public void onSuccess(UserDetails userDetails) {
                // Post the user details to the live data which the activity observes
                userDetailsLiveData.postValue(userDetails);
            }

            @Override
            public void onError(Throwable throwable) {
                // Post the error to be observed by the activity
                errorLiveData.postValue(throwable.getMessage());
            }
        });
    }

}