package com.example.foobook_android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FeedActivity extends AppCompatActivity implements PostAdapter.PostItemListener {
    private static final int CAMERA_PERMISSION_CODE = 101;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private PhotoSelectorHelper photoSelectorHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setupRecyclerView();
        setupButtons();
        PostManager.loadPostsFromJson(this, "posts.json");

    }

    private void setupButtons() {
        ImageButton addPostBtn = findViewById(R.id.addPost);
        addPostBtn.setOnClickListener(v -> onAdd());

        ImageButton feedMenuBtn = findViewById(R.id.feedMenuBtn);
        feedMenuBtn.setOnClickListener(this::showFeedMenu);
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.feedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(this, PostManager.getPosts(), this);
        recyclerView.setAdapter(postAdapter);
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
        Post postToEdit = PostManager.getPosts().get(position);
        Intent editIntent = new Intent(this, EditPostActivity.class)
                .putExtra("postDetails", postToEdit)
                .putExtra("postPosition", position);
        startActivity(editIntent);
    }


    public void onDelete(int position) {
        PostManager.deletePost(position);
        postAdapter.notifyItemRemoved(position);
        CommentsDataHolder.onPostDeleted(position);
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
        updateUI();
    }
}
