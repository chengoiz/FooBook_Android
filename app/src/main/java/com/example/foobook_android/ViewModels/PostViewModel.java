package com.example.foobook_android.ViewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobook_android.Api.PostsResponse;
import com.example.foobook_android.Api.ToggleLikeResponse;
import com.example.foobook_android.Repositories.UserRepository;
import com.example.foobook_android.activities.EditPostActivity;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.Repositories.PostRepository;
import com.example.foobook_android.utility.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends AndroidViewModel {
    private final PostRepository repository;
    private final LiveData<List<Post>> latestPosts;
    private MutableLiveData<String> displayNameLiveData = new MutableLiveData<>();
    private MutableLiveData<String> profilePicLiveData = new MutableLiveData<>();
    private MutableLiveData<PostsResponse> postsLiveData = new MutableLiveData<>();
    private MutableLiveData<ToggleLikeResponse> toggleLikeResponse = new MutableLiveData<>();
    private String token;
    private Context context;
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>(); // For error messages




    public PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);
        latestPosts = repository.getLatestPosts();
        this.context = application;
    }
    public LiveData<List<Post>> getLatestPosts() {
        return latestPosts;
    }
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
    public MutableLiveData<ToggleLikeResponse> getToggleLikeResponse() {
        return toggleLikeResponse;
    }

    public LiveData<PostsResponse> getPostsLiveData() {
        return postsLiveData;
    }

    public void fetchPostsFromServer(Context context) {
        repository.fetchAndProcessPosts(context);
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void toggleLike(String postId) {
        repository.toggleLike(postId, new Callback<ToggleLikeResponse>() {
            @Override
            public void onResponse(Call<ToggleLikeResponse> call, Response<ToggleLikeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update LiveData upon successful toggle
                    toggleLikeResponse.setValue(response.body());
                } else {
                    // Handle error scenario, update error LiveData
                    errorLiveData.setValue("Failed to toggle like. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<ToggleLikeResponse> call, Throwable t) {
                // Handle failure of the call, such as network errors, update error LiveData
                errorLiveData.setValue("Network error occurred. Please check your connection and try again.");
            }
        });
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
    public void deleteByPostId(String postId) {
        repository.deleteByPostId(postId);
    }


    public void fetchPostsByUserId(String userId, String authToken) {
        repository.getPostsByUserId(userId, authToken, new Callback<PostsResponse>() {
            @Override
            public void onResponse(Call<PostsResponse> call, Response<PostsResponse> response) {
                if (response.isSuccessful()) {
                    postsLiveData.postValue(response.body());
                } else {
                    // Handle API error response
                    postsLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<PostsResponse> call, Throwable t) {
                // Handle call failure
                postsLiveData.postValue(null);
            }
        });
    }

    public LiveData<String> getDisplayNameLiveData() {
        return displayNameLiveData;
    }
    public LiveData<String> getProfilePicLiveData() {
        return profilePicLiveData;
    }
    public void fetchDisplayName(Context context, String sentUserId) {
        UserRepository userRepository = new UserRepository(context);
        userRepository.fetchUserDetails(sentUserId, new UserRepository.UserDetailsCallback() {
            @Override
            public void onSuccess(UserDetails userDetails) {
                displayNameLiveData.postValue(userDetails.getDisplayName());
                profilePicLiveData.postValue(userDetails.getProfilePic());
                String title = userDetails.getDisplayName();
            }

            @Override
            public void onError(Throwable throwable) {
                displayNameLiveData.postValue(null);
                profilePicLiveData.postValue(null);
            }
        });
    }


    public void createPostForUser(String userId, Post post, Context context) {
        repository.createPostForUser(userId, post, context, new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.d("PostViewModel", "Post created successfully");
                    Post newPost = response.body();

                    if (newPost != null) {
                        if (newPost.getImageUrl() != null) {
                            newPost.setIsPhotoPicked(Post.PHOTO_PICKED);
                        }
                        repository.insert(newPost);


                    }
                } else {
                    // Handle request failure (e.g., validation error)
                    Log.e("PostViewModel", "Failed to update post");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // Handle failure to make the request (e.g., no internet connection)
                Log.e("PostViewModel", "Error updating post", t);
            }
        });
    }

    public void updatePost(String userId, String postId, Post updatedPost, Context context) {
        repository.updatePostForUser(userId, postId, updatedPost, context, new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.d("PostViewModel", "Post updated successfully");
                    Post updatedPost = response.body();

                    if (updatedPost != null) {
                        repository.update(updatedPost);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }


    public void deletePostByUser(String userId, String postId, Context context) {
        repository.deletePostByUser(userId, postId, context);
    }


    public LiveData<Post> getPostById(String postId) {
        return repository.getPostById(postId);
    }
}