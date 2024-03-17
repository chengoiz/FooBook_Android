package com.example.foobook_android.Api;

import com.example.foobook_android.post.Post;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostsResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("posts")
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public String getMessage() {
        return message;
    }

    // Constructor, getters, and setters
}
