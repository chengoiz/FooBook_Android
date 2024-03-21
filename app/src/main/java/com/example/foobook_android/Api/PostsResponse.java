package com.example.foobook_android.Api;

import com.example.foobook_android.post.Post;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostsResponse {
    @SerializedName("message")
    private final String message;
    @SerializedName("posts")
    private List<Post> posts;

    public PostsResponse(String message) {
        this.message = message;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public String getMessage() {
        return message;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    // Constructor, getters, and setters
}
