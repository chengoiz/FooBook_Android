package com.example.foobook_android.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.Repositories.PostRepository;
import com.example.foobook_android.utility.UserDetails;

import java.util.List;

public class PostViewModel extends AndroidViewModel {
    private final PostRepository repository;
    private final LiveData<List<Post>> latestPosts;
    private MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
    private MutableLiveData<String> profilePicLiveData = new MutableLiveData<>();

    private String token;


    public PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);
        latestPosts = repository.getLatestPosts();
    }
    public LiveData<List<Post>> getLatestPosts() {
        return latestPosts;
    }
    public void setToken(String token) {
        this.token = token;
    }


    // Method for adding a new post
    public void insert(Post post) {
        repository.insert(post);
    }

    // Method for deleting a post
    public void delete(Post post) {
        repository.delete(post);
    }

    // Method to delete a post by its ID
    public void deleteByPostId(long postId) {
        repository.deleteByPostId(postId);
    }

    public void update(Post post) {
        repository.update(post);
    }

    public LiveData<String> getUsernameLiveData() {
        return usernameLiveData;
    }
    public LiveData<String> getProfilePicLiveData() {
        return profilePicLiveData;
    }
    public void fetchUsername(String token, Context context) {
        UserRepository userRepository = new UserRepository(token, context);
        userRepository.fetchUserDetails(token, new UserRepository.UserDetailsCallback() {
            @Override
            public void onSuccess(UserDetails userDetails) {
                usernameLiveData.postValue(userDetails.getName());
                profilePicLiveData.postValue(userDetails.getProfilePic()); // Update profile pic live data

            }

            @Override
            public void onError(Throwable throwable) {
                // Handle error, maybe post a default value or an error message
                usernameLiveData.postValue(null);
                profilePicLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<Post>> getPostsByUserId(String userId) {
        // Implement fetching posts by userId
        // This could involve setting userPosts to a LiveData returned from a Repository method
        return latestPosts; // Need to fix it to return the specific user's posts.
    }
    public LiveData<Post> getPostById(long postId) {
        return repository.getPostById(postId);
    }
}