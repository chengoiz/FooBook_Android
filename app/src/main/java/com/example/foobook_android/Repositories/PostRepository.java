package com.example.foobook_android.Repositories;

import static android.content.Context.MODE_PRIVATE;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.example.foobook_android.Api.ApiResponse;
import com.example.foobook_android.Api.FeedResponse;
import com.example.foobook_android.Api.PostsResponse;
import com.example.foobook_android.Api.ToggleLikeResponse;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.daos.PostDao;
import com.example.foobook_android.database.PostDB;
import com.example.foobook_android.network.RetrofitClient;
import com.example.foobook_android.post.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Manages data operations for posts, including fetching from API, and CRUD operations with local database.
public class PostRepository {
    private final PostDao postDao; // DAO for local database operations on posts.
    private final WebServiceApi webServiceApi;
    private final LiveData<List<Post>> latestPosts; // Cached LiveData of the latest posts fetched from the database.
    private final Context context;

    // Initializes the repository, setting up the web service and local database access.
    public PostRepository(Application application) {
        PostDB db = PostDB.getInstance(application);
        postDao = db.postDao();
        latestPosts = postDao.getLatestPosts();
        webServiceApi = RetrofitClient.getClient().create(WebServiceApi.class);
        this.context = application;
    }

    // Returns the latest posts fetched from the local database.
    public LiveData<List<Post>> getLatestPosts() {
        return latestPosts;
    }

    // Inserts a post into the local database on a background thread.
    public void insert(Post post) {
        new Thread(() -> postDao.insert(post)).start();
    }

    // Deletes a specific post by its ID from the local database on a background thread.
    public void delete(Post post) {
        new Thread(() -> postDao.deleteById(post.getPostId())).start();
    }

    // Updates a post in the local database on a background thread.
    public void update(Post post) {
        new Thread(() -> postDao.update(post)).start();
    }

    // Method to delete a post by its ID, handling non-existing posts with logging.
    public void deleteByPostId(String postId) {
        new Thread(() -> {
            LiveData<Post> post = postDao.getPostById(postId); // Fetch the post by ID
            if (post != null) {
                postDao.deleteById(postId);
            } else {
                // Log or handle the case where the post doesn't exist
                Log.e("PostRepository", "Attempted to delete a non-existing post with ID: " + postId);
            }
        }).start();
    }

    // Creates a post for a user by sending a network request.
    public void createPostForUser(String userId, Post post, Context context, Callback<Post> callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        webServiceApi.createPostForUser(userId, post,"Bearer " + token).enqueue(callback);
    }

    // Updates a user's post by sending a network request.
    public void updatePostForUser(String userId, String postId, Post post, Context context, Callback<Post> callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        webServiceApi.updatePostForUser(userId, postId, post,"Bearer " + token).enqueue(callback);
    }

    // Deletes a post made by a user, and reflects the change in the local database.
    public void deletePostByUser(String userId, String postId, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("token", "");
        webServiceApi.deletePostForUser(userId, postId, "Bearer " + authToken).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    // Execute on a background thread
                    Executors.newSingleThreadExecutor().submit(() -> postDao.deleteById(postId));
                    Log.i("PostRepository", "Post deleted successfully: " + Objects.requireNonNull(response.body()).getMessage());
                } else {
                    Log.e("PostRepository", "Failed to delete post, server responded with: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e("PostRepository", "Error deleting post", t);
            }
        });
    }

    // Toggles the like status of a post for a user and handles the response via callback.
    public void toggleLike(String userId, String postId, final Callback<ToggleLikeResponse> callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("token", "");
        Call<ToggleLikeResponse> call = webServiceApi.toggleLike(userId, postId, "Bearer " + authToken);
        call.enqueue(callback);
    }

    // Fetches posts for the feed from the network and updates the local database with new data.
    public void fetchAndProcessPosts(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("token", "");
        webServiceApi.fetchFeedPosts("Bearer " + authToken).enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(@NonNull Call<FeedResponse> call, @NonNull Response<FeedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> allPosts = new ArrayList<>();
                    allPosts.addAll(response.body().getFriendsPosts());
                    allPosts.addAll(response.body().getNonFriendsPosts());

                    // Now insert these posts into Room database
                    Executors.newSingleThreadExecutor().execute(() -> {
                        postDao.deleteAll();
                        postDao.insertAll(allPosts);
                    });
                } else {
                    Log.e("PostRepository", "Failed to fetch feed posts.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeedResponse> call, @NonNull Throwable t) {
                Log.e("PostRepository", "Error fetching feed posts.", t);
            }
        });
    }


    public void getPostsByUserId(String userId, String authToken, Callback<PostsResponse> callback) {
        webServiceApi.getPostsByUserId(userId, authToken).enqueue(callback);
    }


    public LiveData<Post> getPostById(String postId) {
        return postDao.getPostById(postId);
    }
}