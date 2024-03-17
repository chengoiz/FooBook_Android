package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.foobook_android.R;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.adapters.PostAdapter;
import com.example.foobook_android.post.Post;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private PostViewModel postViewModel;
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        postsRecyclerView = findViewById(R.id.feedRecyclerView); // Assuming this is your RecyclerView ID
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAdapter = new PostAdapter(this, new ArrayList<>(), new PostAdapter.PostItemListener() {
            @Override
            public void onEdit(Post post) {
                // Implement edit functionality
            }

            @Override
            public void onDelete(String postId) {
                // Implement delete functionality
            }
        });

        postsRecyclerView.setAdapter(postAdapter);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        String userId = getCurrentUserId();
        String authToken = getAuthToken();

        postViewModel.getPostsLiveData().observe(this, postsResponse -> {
            if (postsResponse != null && postsResponse.getPosts() != null) {
                postAdapter.setPosts(postsResponse.getPosts());
            } else {
                Log.e("ProfileActivity", "Failed to fetch posts or no posts available");
            }
        });

        postViewModel.fetchPostsByUserId(userId, "Bearer " + authToken);
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", ""); // Replace "userId" with the actual key you used to store the user's ID
    }

    private String getAuthToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
        // Retrieve the auth token from SharedPreferences.
    }
}
