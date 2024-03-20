package com.example.foobook_android.post;

import java.util.ArrayList;
import java.util.List;

public class PostManager {
    private static final List<Post> posts = new ArrayList<>();

    // Fetches the relevant posts.
    public static List<Post> getPosts() {
        return posts;
    }

}

