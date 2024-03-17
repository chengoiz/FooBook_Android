package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foobook_android.R;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.adapters.PostAdapter;
import com.example.foobook_android.post.Post;

import java.util.ArrayList;

public class UserPostsActivity extends AppCompatActivity implements PostAdapter.PostItemListener {

    private PostViewModel postViewModel;
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private TextView displayNameTextView;
    private ImageView profileImageView;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        postsRecyclerView = findViewById(R.id.feedRecyclerView); // Assuming this is your RecyclerView ID
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayNameTextView = findViewById(R.id.userNameInProfile);
        profileImageView = findViewById(R.id.profileImageInProfile);



        postAdapter = new PostAdapter(this, new ArrayList<>(), this);

        postsRecyclerView.setAdapter(postAdapter);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        userId = getIntent().getStringExtra("VIEWED_USER_ID");

        postViewModel.fetchDisplayName(this, userId);

        String authToken = getAuthToken();

        postViewModel.getProfilePicLiveData().observe(this, profilePicUrl -> {
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                Glide.with(this).load(profilePicUrl).into(profileImageView);
            } else {
                // Set default or placeholder image
                Glide.with(this).load(R.drawable.defaultpic).into(profileImageView);
            }
        });

        postViewModel.getDisplayNameLiveData().observe(this, displayName -> {
            if (displayName != null && !displayName.isEmpty()) {
                displayNameTextView.setText(displayName + "'s posts");
            } else {
                Log.e("ProfileActivity", "Failed to fetch user details or display name is empty");
                displayNameTextView.setText(getString(R.string.default_username)); // Set to a default value or prompt
            }
        });

        postViewModel.getPostsLiveData().observe(this, postsResponse -> {
            if (postsResponse != null && postsResponse.getPosts() != null) {
                postAdapter.setPosts(postsResponse.getPosts());
            } else {
                Log.e("ProfileActivity", "Failed to fetch posts or no posts available");
            }
        });

        postViewModel.fetchPostsByUserId(userId, "Bearer " + authToken);
    }

    @Override
    public void onEdit(Post post) {
        Intent editIntent = new Intent(UserPostsActivity.this, EditPostActivity.class);
        editIntent.putExtra("postId", post.getPostId()); // Pass the post's ID
        startActivity(editIntent);
    }

    @Override
    public void onDelete(String postId) {
        if (postId == null) {
            Toast.makeText(this, "Error deleting post.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Now delete using the post's ID
        postViewModel.deleteByPostId(postId);
        postViewModel.deletePostByUser(getCurrentUserId(), postId, this);

        postAdapter.removePostById(postId);
        Toast.makeText(this, "Post deleted successfully.", Toast.LENGTH_SHORT).show();
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
