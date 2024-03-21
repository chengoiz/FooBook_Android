package com.example.foobook_android.Repositories;

import static android.content.Context.MODE_PRIVATE;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import okhttp3.logging.HttpLoggingInterceptor;

import com.example.foobook_android.Api.ApiResponse;
import com.example.foobook_android.Api.FeedResponse;
import com.example.foobook_android.Api.PostsResponse;
import com.example.foobook_android.Api.ToggleLikeResponse;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.daos.PostDao;
import com.example.foobook_android.database.PostDB;
import com.example.foobook_android.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostRepository {
    private final PostDao postDao;
    private final WebServiceApi webServiceApi;
    private final LiveData<List<Post>> latestPosts;
    private final Context context;

    public PostRepository(Application application) {
        PostDB db = PostDB.getInstance(application);
        postDao = db.postDao();
        latestPosts = postDao.getLatestPosts();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                build(); // change the baseUrl later
        webServiceApi = retrofit.create(WebServiceApi.class);
        this.context = application;
    }

    public LiveData<List<Post>> getLatestPosts() {
        return latestPosts;
    }

    // Method to insert a post
    public void insert(Post post) {
        new Thread(() -> postDao.insert(post)).start();
    }

    // Method to delete a post
    public void delete(Post post) {
        new Thread(() -> postDao.deleteById(post.getPostId())).start();
    }
    public void update(Post post) {
        // Run database operation in a background thread
        new Thread(() -> postDao.update(post)).start();
    }
    // Method to delete a post by its ID
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

    public void createPostForUser(String userId, Post post, Context context, Callback<Post> callback) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        WebServiceApi webServiceApi = retrofit.create(WebServiceApi.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        webServiceApi.createPostForUser(userId, post,"Bearer " + token).enqueue(callback);
    }

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

    public void toggleLike(String postId, final Callback<ToggleLikeResponse> callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("token", "");

        Call<ToggleLikeResponse> call = webServiceApi.toggleLike(postId, "Bearer " + authToken);
        call.enqueue(callback);
    }

    public void fetchAndProcessPosts(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("token", "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServiceApi webServiceApi = retrofit.create(WebServiceApi.class);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServiceApi webServiceApi = retrofit.create(WebServiceApi.class);
        webServiceApi.getPostsByUserId(userId, authToken).enqueue(callback);
    }


    public LiveData<Post> getPostById(String postId) {
        return postDao.getPostById(postId);
    }
}