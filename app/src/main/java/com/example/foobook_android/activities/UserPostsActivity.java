package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.foobook_android.R;
import com.example.foobook_android.ViewModels.FriendshipViewModel;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.ViewModels.UserViewModel;
import com.example.foobook_android.adapters.PostAdapter;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.utility.TokenManager;
import com.example.foobook_android.utility.UserDetails;
import java.util.ArrayList;
import java.util.Set;

/**
 * UserPostsActivity displays the posts of a specific user. It supports viewing, editing, and
 * deleting posts if the current user has the appropriate permissions. Additionally, this activity
 * allows sending friend requests if the viewed user is not already a friend and if the viewer is
 * not viewing their own profile. It leverages PostViewModel for post operations and
 * FriendshipViewModel for friend-related actions. Profile information, including the user's display
 * name and profile picture, is set at the top of the activity.
 */
public class UserPostsActivity extends AppCompatActivity implements PostAdapter.PostItemListener {

    private PostViewModel postViewModel;
    private FriendshipViewModel friendshipViewModel;
    private PostAdapter postAdapter;
    String displayName;
    String profilePic;
    String userId;
    private TextView displayNameTextView;
    private ImageView profileImageView;
    private TokenManager tokenManager; // Field to hold the TokenManager instance
    private UserViewModel userViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);
        initializeViewModels(); // Initialize all ViewModels
        initialize(); // Set up UI components
        fetchAndDisplayUserDetails(); // Then, fetch and display user details
    }

    private void initialize() {
        // UI component setup without data fetching
        postAdapter = new PostAdapter(this, new ArrayList<>(), this);
        RecyclerView postsRecyclerView = findViewById(R.id.feedRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setAdapter(postAdapter);
        displayNameTextView = findViewById(R.id.userNameInProfile);
        profileImageView = findViewById(R.id.profileImageInProfile);
    }

    private void initializeViewModels() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        friendshipViewModel = new ViewModelProvider(this).get(FriendshipViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        tokenManager = new TokenManager(this);
        // Initialize RecyclerView and other UI components.
    }

    private void fetchAndDisplayUserDetails() {
        userId = getIntent().getStringExtra("VIEWED_USER_ID");
        if (userId != null && !userId.isEmpty()) {
            userViewModel.fetchUserDetails(userId, new UserViewModel.UserDetailsCallback() {
                @Override
                public void onSuccess(UserDetails userDetails) {
                    runOnUiThread(() -> {
                        // Now update UI components with the fetched user details
                        displayName = userDetails.getDisplayName();
                        profilePic = userDetails.getProfilePic();

                        TextView displayNameTextView = findViewById(R.id.userNameInProfile);
                        displayNameTextView.setText(displayName + "'s Profile");

                        ImageView profileImageView = findViewById(R.id.profileImageInProfile);
                        Glide.with(UserPostsActivity.this).load(profilePic).into(profileImageView);

                        setButtons();
                        setObservers();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> Toast.makeText(UserPostsActivity.this, "Failed to fetch user details: " + error, Toast.LENGTH_SHORT).show());
                }
            });
        } else {
            Toast.makeText(this, "User ID is missing.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtons() {
        // Handling friend request button visibility and action
        if (!isFriend() && !userId.equals(getCurrentUserId())) {
            Button friendRequestsButton = findViewById(R.id.FriendRequestsButton);
            friendRequestsButton.setVisibility(View.VISIBLE);
            displayNameTextView.setText(displayName + "'s Profile");

            friendRequestsButton.setOnClickListener(v -> {
                friendshipViewModel.sendFriendRequest(getCurrentUserId(), userId);
                friendshipViewModel.getFriendRequestResponse().observe(UserPostsActivity.this, response -> {
                    Toast.makeText(UserPostsActivity.this, response, Toast.LENGTH_SHORT).show();
                    friendRequestsButton.setEnabled(false);
                });
            });
        } else {
            displayNameTextView.setText(displayName + "'s Posts");
        }
    }

    private void setObservers() {
        // Fetching and displaying the user's posts
        postViewModel.getPostsLiveData().observe(this, postsResponse -> {
            if (postsResponse != null && postsResponse.getPosts() != null) {
                postAdapter.setPosts(postsResponse.getPosts());
            } else {
                Log.e("ProfileActivity", "Failed to fetch posts or no posts available");
            }
        });
        postViewModel.fetchPostsByUserId(userId, "Bearer " + getAuthToken());
    }

    @Override
    public void onEdit(Post post) {
        // Handling post edit action
        Intent editIntent = new Intent(UserPostsActivity.this, EditPostActivity.class);
        editIntent.putExtra("postId", post.getPostId());
        startActivity(editIntent);
    }

    private Set<String> getFriendList() {
        // Retrieving the current user's friends list from SharedPreferences
        return tokenManager.getFriendList();
    }

    private boolean isFriend() {
        // Checking if the viewed user is in the current user's friends list
        Set<String> friendsList = getFriendList();
        return friendsList.contains(userId);
    }

    @Override
    public void onDelete(String postId) {
        // Handling post delete action
        if (postId == null) {
            Toast.makeText(this, "Error deleting post.", Toast.LENGTH_SHORT).show();
            return;
        }
        postViewModel.deleteByPostId(postId);
        postViewModel.deletePostByUser(getCurrentUserId(), postId, this);
        postAdapter.removePostById(postId);
        Toast.makeText(this, "Post deleted successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLike(String postId) {
        if (postId == null) {
            Toast.makeText(this, "Error liking post.", Toast.LENGTH_SHORT).show();
            return;
        }
        postViewModel.toggleLike(getCurrentUserId(), postId);
    }

    private String getCurrentUserId() {
        // Retrieving the current user's ID from SharedPreferences
        return tokenManager.getUserId();
    }

    private String getAuthToken() {
        // Retrieve the auth token from SharedPreferences.
        return tokenManager.getToken();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        postViewModel.fetchPostsByUserId(userId, "Bearer " + getAuthToken());
        if (postAdapter != null) {
            postAdapter.notifyDataSetChanged();
        } else {
            Log.e("UserPostsActivity", "postAdapter is null in onResume");
        }
    }
}
