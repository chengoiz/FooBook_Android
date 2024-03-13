package com.example.foobook_android.Repositories;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.daos.PostDao;
import com.example.foobook_android.database.PostDB;
import com.example.foobook_android.post.Post;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostRepository {
    private final PostDao postDao;
    private final LiveData<List<Post>> latestPosts;

    public PostRepository(Application application) {
        PostDB db = PostDB.getInstance(application);
        postDao = db.postDao();
        latestPosts = postDao.getLatestPosts();
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
        new Thread(() -> postDao.deleteById(post.getId())).start();
    }
    public void update(Post post) {
        // Run database operation in a background thread
        new Thread(() -> postDao.update(post)).start();
    }
    // Method to delete a post by its ID
    public void deleteByPostId(long postId) {
        new Thread(() -> {
            Post post = postDao.get(postId); // Fetch the post by ID
            if (post != null) {
                postDao.deleteById(postId);
            } else {
                // Log or handle the case where the post doesn't exist
                Log.e("PostRepository", "Attempted to delete a non-existing post with ID: " + postId);
            }
        }).start();
    }

    public void createPostForUser(String userId, Post post, Context context, Callback<Post> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebServiceApi webServiceApi = retrofit.create(WebServiceApi.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        webServiceApi.createPostForUser(userId, post,"Bearer " + token).enqueue(callback);
    }
    public LiveData<Post> getPostById(long postId) {
        return postDao.getPostById(postId);
    }
    //Todo: We need to add methods for insert, delete, etc, similar to PostDao.
}