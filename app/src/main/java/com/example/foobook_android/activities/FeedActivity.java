package com.example.foobook_android.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.ViewModels.UserViewModel;
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.post.PostManager;
import com.example.foobook_android.R;
import com.example.foobook_android.adapters.PostAdapter;
import com.example.foobook_android.utility.TokenManager;
import java.util.ArrayList;

/**
 * The activity for displaying the main feed of posts. It allows users to view posts, refresh them,
 * navigate to other activities like creating a post or viewing the friends list, and toggle night mode.
 */
public class FeedActivity extends AppCompatActivity implements PostAdapter.PostItemListener {
    private PostViewModel postViewModel;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private PhotoSelectorHelper photoSelectorHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserViewModel userViewModel;
    private TokenManager tokenManager; // Field to hold the TokenManager instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        recyclerView = findViewById(R.id.feedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tokenManager = new TokenManager(this); // Initialize the TokenManager
        initialize();
        // Setup UI components and their interactions
        setupButtons();
    }

    /**
     * Initializes the view model, adapter, and starts data fetch operations.
     */
    private void initialize() {
        postAdapter = new PostAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(postAdapter);

        // Initialize the ViewModels for posts and user management
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.fetchPostsFromServer(this);

        // Observe changes in the posts and user deletion status
        postViewModel.getLatestPosts().observe(this, posts -> postAdapter.setPosts(posts));
        userViewModel.getIsUserDeleted().observe(this, isDeleted -> {
            if (isDeleted) {
                Toast.makeText(FeedActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
                navigateToLoginActivity();
                finish();
            }
        });
        userViewModel.getDeleteUserError().observe(this, error -> Toast.makeText(FeedActivity.this, error, Toast.LENGTH_SHORT).show());
    }

    /**
     * Navigates the user to the login activity and clears the activity stack.
     */
    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        // Clear the task stack and start fresh.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Handle any configuration changes, such as theme changes, without recreating the activity
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save any necessary state data here
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore any necessary state data here
    }

    /**
     * Sets up button click listeners and the swipe refresh layout.
     */
    private void setupButtons() {
        ImageButton addPostBtn = findViewById(R.id.addPost);
        addPostBtn.setOnClickListener(v -> onAdd());

        Button viewFriendListButton = findViewById(R.id.FriendListButton);
        viewFriendListButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, FriendsListActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            postViewModel.fetchPostsFromServer(FeedActivity.this);
            fetchAndDisplayPosts();
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Button viewFriendRequestsButton = findViewById(R.id.FriendRequestsButton);
        viewFriendRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        });

        Button myProfileButton = findViewById(R.id.MyProfileButton);
        myProfileButton.setOnClickListener(v -> {
            postAdapter.navigateToUserPosts(getCurrentUserId());
        });

        ImageButton feedMenuBtn = findViewById(R.id.feedMenuBtn);
        feedMenuBtn.setOnClickListener(this::showFeedMenu);

        SwitchCompat nightModeSwitch = findViewById(R.id.nightMode);
        // Check the current night mode state and update the switch accordingly
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        nightModeSwitch.setChecked(nightModeFlags == Configuration.UI_MODE_NIGHT_YES);
        nightModeSwitch.setOnClickListener(this::nightMode);
    }



    private void nightMode(View v) {
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                // Default to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Refreshes the feed by fetching the latest posts and displaying them.
     */
    private void fetchAndDisplayPosts() {
        // Observe the LiveData of posts from PostViewModel
        postViewModel.getLatestPosts().observe(this, posts -> {
            if (postAdapter == null) {
                postAdapter = new PostAdapter(FeedActivity.this, posts, FeedActivity.this);
                recyclerView.setAdapter(postAdapter);
            } else {
                postAdapter.setPosts(posts);
            }
            swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
        });
    }

    /**
     * Displays a menu for the feed with options like logout and edit profile.
     *
     * @param view The view that triggers the menu.
     */
    private void showFeedMenu(View view) {
        PopupMenu feedMenu = new PopupMenu(this, view);
        feedMenu.inflate(R.menu.feed_menu);
        feedMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.Logout) {
                // Handle logout action
                Intent logoutIntent = new Intent(this, LogInActivity.class);
                Toast.makeText(FeedActivity.this, "Logged-out successfully", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(logoutIntent);
                finish(); // Close the current activity
                return true;
            } else if (itemId == R.id.EditProfile) {
                // Handle edit profile action
                Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
                // Navigate to the edit profile activity
                startActivity(editProfileIntent);
                return true;
            }
            else if (itemId == R.id.DeleteUser) {
                // Show confirmation dialog for account deletion
                showDeleteConfirmationDialog();
            }
            return false;
        });
        feedMenu.show();
    }

    /**
     * Displays a confirmation dialog for account deletion.
     */
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                // Set the positive Yes button and its logic
                .setPositiveButton(android.R.string.yes, (dialog, which) -> userViewModel.deleteUserAccount())
                // Set the negative No button and do nothing when it's clicked
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Starts the activity to add a new post.
     */
    public void onAdd() {
        Intent createPostIntent = new Intent(this, CreatePostActivity.class);
        startActivity(createPostIntent);
    }

    /**
     * Starts the activity to edit a selected post.
     *
     * @param post The post to edit.
     */
    public void onEdit(Post post) {
        Intent editIntent = new Intent(FeedActivity.this, EditPostActivity.class);
        editIntent.putExtra("postId", post.getPostId()); // Pass the post ID to the edit activity
        startActivity(editIntent);
    }

    /**
     * Deletes a post by its ID.
     *
     * @param postId The ID of the post to delete.
     */
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
    public void onLike(String postId) {
        if (postId == null) {
            Toast.makeText(this, "Error liking post.", Toast.LENGTH_SHORT).show();
            return;
        }
        postViewModel.toggleLike(getCurrentUserId(), postId);
    }


    @SuppressLint("NotifyDataSetChanged")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        postAdapter.notifyDataSetChanged(); // Refresh the adapter's data
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photoSelectorHelper.openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Retrieves the current user ID from shared preferences.
     *
     * @return The current user ID.
     */
    private String getCurrentUserId() {
        return tokenManager.getUserId();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void updateUI() {
        if (postAdapter == null) {
            postAdapter = new PostAdapter(this, PostManager.getPosts(), this);
            recyclerView.setAdapter(postAdapter);
        } else {
            postAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        postViewModel.fetchPostsFromServer(this);
        fetchAndDisplayPosts(); // Fetches and displays the latest posts from the database
        updateUI();
    }
}
