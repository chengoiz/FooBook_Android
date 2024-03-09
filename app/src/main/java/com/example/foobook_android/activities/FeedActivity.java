package com.example.foobook_android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.database.PostDB;
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.post.PostManager;
import com.example.foobook_android.R;
import com.example.foobook_android.adapters.PostAdapter;

import java.util.List;


public class FeedActivity extends AppCompatActivity implements PostAdapter.PostItemListener {
    private PostDB db;

    private static final int CAMERA_PERMISSION_CODE = 101;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private PhotoSelectorHelper photoSelectorHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        db = PostDB.getInstance(this);

        setupRecyclerView();
        setupButtons();

        // To handle the Dark mode reopening the app
        if (PostManager.getPosts().isEmpty()) {
            PostManager.loadPostsFromJson(this, "posts.json");

        }

    }

    private void setupButtons() {
        ImageButton addPostBtn = findViewById(R.id.addPost);
        addPostBtn.setOnClickListener(v -> onAdd());

        ImageButton feedMenuBtn = findViewById(R.id.feedMenuBtn);
        feedMenuBtn.setOnClickListener(this::showFeedMenu);

        SwitchCompat nightMode = findViewById(R.id.nightMode);
        nightMode.setOnClickListener(this::nightMode);
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


    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.feedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(this, PostManager.getPosts(), this);
        recyclerView.setAdapter(postAdapter);
    }

    private void fetchAndDisplayPosts() {
        new Thread(() -> {
            List<Post> posts = db.postDao().getLatestPosts();
            runOnUiThread(() -> {
                postAdapter = new PostAdapter(FeedActivity.this, posts, FeedActivity.this);
                recyclerView.setAdapter(postAdapter);
            });
        }).start();
    }

    private void showFeedMenu(View view) {
        PopupMenu feedMenu = new PopupMenu(this, view);
        feedMenu.inflate(R.menu.feed_menu);
        feedMenu.setOnMenuItemClickListener(item -> {
            Intent LOGOUT = new Intent(this, LogInActivity.class);
            startActivity(LOGOUT);
            return true;
        });
        feedMenu.show();
    }


    public void onAdd() {
        Intent createPost = new Intent(this, CreatePostActivity.class);
        startActivity(createPost);
    }

    public void onEdit(int position) {
        // Option 1: Directly use the database to fetch the post ID if 'position' correlates with database IDs
        new Thread(() -> {
            Post postToEdit = db.postDao().getLatestPosts().get(position); // This assumes the list order hasn't changed
            long postId = postToEdit.getId();

            Intent editIntent = new Intent(FeedActivity.this, EditPostActivity.class);
            editIntent.putExtra("postId", postId); // Pass the post's unique ID
            startActivity(editIntent);
        }).start();
    }


//    public void onDelete(int position) {
//        PostManager.deletePost(position);
//        postAdapter.notifyItemRemoved(position);
//        CommentsDataHolder.onPostDeleted(position);
//    }

    public void onDelete(int position) {
        // Retrieve the post to delete using the new getPostAt method
        Post postToDelete = postAdapter.getPostAt(position);

        if (postToDelete != null) {
            new Thread(() -> {
                // Delete the post from the database
                db.postDao().delete(postToDelete);

                // Once deleted, update UI on the main thread
                runOnUiThread(() -> {
                    // Call deletePost from PostAdapter to remove it from the RecyclerView
                    postAdapter.deletePost(position);
                    // Optionally, manage any additional UI updates or show a message
                    Toast.makeText(FeedActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                });
            }).start();
        } else {
            // Handle the case where the post is not found or invalid position
            Toast.makeText(FeedActivity.this, "Error deleting post", Toast.LENGTH_SHORT).show();
        }
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
