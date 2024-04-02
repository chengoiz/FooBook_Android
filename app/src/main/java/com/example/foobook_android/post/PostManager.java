package com.example.foobook_android.post;

import java.util.ArrayList;
import java.util.List;

// Manages a static list of posts for the application. Currently, it's a simple in-memory storage.
public class PostManager {
    private static final List<Post> posts = new ArrayList<>(); // Container for all posts.

    // Retrieves the list of all posts.
    public static List<Post> getPosts() {
        return posts;
    }

}

