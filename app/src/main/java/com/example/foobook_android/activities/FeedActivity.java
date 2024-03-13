package com.example.foobook_android.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.post.PostManager;
import com.example.foobook_android.R;
import com.example.foobook_android.adapters.PostAdapter;

import java.util.ArrayList;


public class FeedActivity extends AppCompatActivity implements PostAdapter.PostItemListener {
    private PostViewModel postViewModel;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private PhotoSelectorHelper photoSelectorHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        recyclerView = findViewById(R.id.feedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(postAdapter);


        // Initialize PostViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getLatestPosts().observe(this, posts -> {
            postAdapter.setPosts(posts);
        });

        // Setup buttons and other UI components
        setupButtons();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Handle the dark mode change here if necessary, without recreating the activity
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save necessary state data
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state data
    }

    private void setupButtons() {
        ImageButton addPostBtn = findViewById(R.id.addPost);
        addPostBtn.setOnClickListener(v -> onAdd());

        Button viewFriendRequestsButton = findViewById(R.id.viewFriendRequestsButton);
        viewFriendRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        });

        ImageButton feedMenuBtn = findViewById(R.id.feedMenuBtn);
        feedMenuBtn.setOnClickListener(this::showFeedMenu);

        SwitchCompat nightModeSwitch = findViewById(R.id.nightMode);
        // Set the switch to reflect the current night mode state
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


    private void fetchAndDisplayPosts() {
        // Observe the LiveData of posts from PostViewModel
        postViewModel.getLatestPosts().observe(this, posts -> {
            if (postAdapter == null) {
                postAdapter = new PostAdapter(FeedActivity.this, posts, FeedActivity.this);
                recyclerView.setAdapter(postAdapter);
            } else {
                postAdapter.setPosts(posts);
            }
        });
    }


    private void showFeedMenu(View view) {
        PopupMenu feedMenu = new PopupMenu(this, view);
        feedMenu.inflate(R.menu.feed_menu);
        feedMenu.setOnMenuItemClickListener(item -> {
            Intent LOGOUT = new Intent(this, LogInActivity.class);
            Toast.makeText(FeedActivity.this, "Logged-out successfully", Toast.LENGTH_SHORT).show();
            startActivity(LOGOUT);
            return true;
        });
        feedMenu.show();
    }


    public void onAdd() {
        Intent createPostIntent = new Intent(this, CreatePostActivity.class);
        startActivity(createPostIntent);
    }

    public void onEdit(Post post) {
        Intent editIntent = new Intent(FeedActivity.this, EditPostActivity.class);
        editIntent.putExtra("postId", post.getId()); // Pass the post's ID
        startActivity(editIntent);
    }

    public void onDelete(long postId) {
        if (postId < 0) {
            Toast.makeText(this, "Error deleting post.", Toast.LENGTH_SHORT).show();
            return;
        }
            // Now delete using the post's ID
            postViewModel.deleteByPostId(postId);
            postAdapter.removePostById(postId);
        Toast.makeText(this, "Post deleted successfully.", Toast.LENGTH_SHORT).show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        postAdapter.notifyDataSetChanged();
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
        fetchAndDisplayPosts(); // Fetches and displays the latest posts from the database
        updateUI();
    }
}
