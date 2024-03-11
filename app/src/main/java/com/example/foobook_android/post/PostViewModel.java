package com.example.foobook_android.post;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PostViewModel extends AndroidViewModel {
    private final PostRepository repository;
    private final LiveData<List<Post>> latestPosts;

    public PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);
        latestPosts = repository.getLatestPosts();
    }

    public LiveData<List<Post>> getLatestPosts() {
        return latestPosts;
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
    public LiveData<Post> getPostById(long postId) {
        return repository.getPostById(postId);
    }
}