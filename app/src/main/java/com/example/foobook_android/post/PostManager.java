package com.example.foobook_android.post;


import android.content.Context;
import android.util.Log;

import com.example.foobook_android.utility.PostListWrapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PostManager {
    private static final List<Post> posts = new ArrayList<>();

    // Load posts from a JSON asset
    public static void loadPostsFromJson(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();
            reader.close();
            is.close();

            // Parse the JSON to a list of Posts
            Gson gson = new Gson();
            PostListWrapper wrapper = gson.fromJson(json, PostListWrapper.class);
            List<Post> loadedPosts = wrapper.getPosts();
            posts.clear();
            posts.addAll(loadedPosts);
            for (Post post : posts) {
                post.setIsPhotoPicked(Post.PHOTO_PICKED);
                post.setIsJsonFile(Post.JSON_FILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PostManager", "Error loading JSON", e);
        }
    }

    public static List<Post> getPosts() {
        return posts;
    }

    public static void addPost(Post post) {
        posts.add(0, post);
    }

    public static void updatePost(int position, Post updatedPost) {
        if (position >= 0 && position < posts.size()) {
            posts.set(position, updatedPost);
        }
    }

    public static void deletePost(int position) {
        if (position >= 0 && position < posts.size()) {
            posts.remove(position);
        }
    }
}

