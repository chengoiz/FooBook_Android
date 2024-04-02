package com.example.foobook_android.ViewModels;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.utility.UserDetails;

// View model for handling user-related operations.
public class UserViewModel extends AndroidViewModel {
    // Repository for handling user-related operations
    private final UserRepository userRepository;

    // Mutable live data objects for observing user details, errors, and user deletion status
    private final MutableLiveData<UserDetails> userDetailsLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<String> deleteUserError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserDeleted = new MutableLiveData<>();

    // Constructor
    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userDetailsLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    // Getter for observing user details
    public MutableLiveData<UserDetails> getUserDetailsLiveData() {
        return userDetailsLiveData;
    }

    // Getter for observing errors
    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public interface UserDetailsCallback {
        void onSuccess(UserDetails userDetails);
        void onError(String error);
    }


    // Method to delete user account
    public void deleteUserAccount() {
        userRepository.deleteUser(new UserRepository.DeleteUserCallback() {
            @Override
            public void onSuccess() {
                isUserDeleted.postValue(true);
            }

            @Override
            public void onError(String message) {
                deleteUserError.postValue(message);
            }
        });
    }

    // Getter for observing user deletion status
    public LiveData<Boolean> getIsUserDeleted() {
        return isUserDeleted;
    }

    // Getter for observing user deletion errors
    public LiveData<String> getDeleteUserError() {
        return deleteUserError;
    }

    public void fetchUserDetails(String userId, UserDetailsCallback callback) {
        userRepository.fetchUserDetails(userId, new UserRepository.UserDetailsCallback() {
            @Override
            public void onSuccess(UserDetails userDetails) {
                // Pass the details to the callback
                callback.onSuccess(userDetails);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("UserViewModel", "Error fetching user details", throwable);
                // Handle error, e.g., by notifying the user
            }
        });
    }

    // Method to update user details
    public void updateUserDetails(String displayName, String profilePicUri) {
        // The userId is retrieved from the repository's stored data.
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