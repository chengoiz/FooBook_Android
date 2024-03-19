package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foobook_android.R;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.adapters.PostAdapter;
import com.example.foobook_android.post.Post;

import java.util.ArrayList;
import java.util.HashSet;

public class UserPostsActivity extends AppCompatActivity implements PostAdapter.PostItemListener {

    private PostViewModel postViewModel;
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private TextView displayNameTextView;
    private ImageView profileImageView;
    String displayName;
    String ProfilePic;
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
        displayName = getIntent().getStringExtra("VIEWED_USER_DISPLAY_NAME");
        ProfilePic = getIntent().getStringExtra("VIEWED_USER_PROFILE_PIC");


        String authToken = getAuthToken();

        Glide.with(this).load(ProfilePic).into(profileImageView);




        if (!isFriend() && !(userId.equals(getCurrentUserId()))) {
            Button friendRequestsButton = findViewById(R.id.FriendRequestsButton);
            // Set the button to be visible
            friendRequestsButton.setVisibility(View.VISIBLE);
            displayNameTextView.setText(displayName + "'s Profile");
            friendRequestsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postAdapter.sendFriendRequest(userId);
                    // Optionally, provide feedback or disable the button to prevent multiple requests
                    friendRequestsButton.setEnabled(false); // Disable the button
                    Toast.makeText(UserPostsActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            displayNameTextView.setText(displayName + "'s Posts");
        }


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

    private HashSet<String> getFriendList() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return (HashSet<String>) sharedPreferences.getStringSet("friendList", new HashSet<String>());
        // Retrieve the friends list from SharedPreferences.
    }

    private boolean isFriend() {
        HashSet<String> friendsList = getFriendList();
        return friendsList.contains(userId);
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
