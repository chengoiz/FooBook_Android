package com.example.foobook_android.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foobook_android.daos.PostDao;
import com.example.foobook_android.database.PostDB;
import com.example.foobook_android.post.Post;

import java.util.List;

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
    public LiveData<Post> getPostById(long postId) {
        return postDao.getPostById(postId);
    }
    //Todo: We need to add methods for insert, delete, etc, similar to PostDao.
}