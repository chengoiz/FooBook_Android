package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foobook_android.database.PostDB;
import com.example.foobook_android.ViewModels.PostViewModel;
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.post.PostManager;
import com.example.foobook_android.R;
import com.example.foobook_android.adapters.PostAdapter;


public class EditPostActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private PostDB db;
    private PostViewModel postViewModel;
    private EditText postEditText;
    private ImageView selectedImage;
    private Button postButton, removePhoto;
    private ImageButton btnGallery, btnCamera;
    private Uri postImageUri;
    private boolean isPhotoSelected = false;
    private Post currentPost;
    private int postPosition = -1;

    private PostAdapter postAdapter;
    private PhotoSelectorHelper photoSelectorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        db = PostDB.getInstance(this);
        Log.i("EditPostActivity", "onCreate");
        initializeViewComponents();
        // Initialize PostViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        setupListeners();
        initializeHelpers();
        handleIncomingIntent();
    }

    private void initializeViewComponents() {
        postEditText = findViewById(R.id.postEditText);
        selectedImage = findViewById(R.id.selectedImage);
        postButton = findViewById(R.id.postButton);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);
        removePhoto = findViewById(R.id.removePhoto);
    }

    private void setupListeners() {
        btnGallery.setOnClickListener(v -> photoSelectorHelper.openGallery());
        btnCamera.setOnClickListener(v -> photoSelectorHelper.checkCameraPermissionAndOpen());
        removePhoto.setOnClickListener(v -> clearSelectedPhoto());
        postButton.setOnClickListener(v -> savePost());
    }

    private void initializeHelpers() {
        postAdapter = new PostAdapter(this, PostManager.getPosts(), null);
        photoSelectorHelper = new PhotoSelectorHelper(this, CAMERA_REQUEST_CODE,
                GALLERY_REQUEST_CODE, this::setImage);
    }


    private void setImage(Bitmap bitmap) {
        String filename = "photo_" + System.currentTimeMillis() + ".png";
        postImageUri = photoSelectorHelper.saveBitmapToFile(this, bitmap, filename);
        Glide.with(this).load(postImageUri).into(selectedImage);
        selectedImage.setVisibility(View.VISIBLE);
        removePhoto.setVisibility(View.VISIBLE);
        isPhotoSelected = true;
    }


    private void handleIncomingIntent() {
        String postId = getIntent().getStringExtra("postId");
        if (postId != null) {
            postViewModel.getPostById(postId).observe(this, post -> {
                currentPost = post;
                if (post != null) {
                    populateUIWithPostDetails();
                }
            });
        }
    }

    private void populateUIWithPostDetails() {
        // Check if currentPost is not null
        if (currentPost != null) {
            // Set the post content
            postEditText.setText(currentPost.getText());

            // Check if there's an image URI available
            if (currentPost.getImageUrl() != null && !currentPost.getImageUrl().isEmpty()) {
                Uri imageUri = Uri.parse(currentPost.getImageUrl());
                Glide.with(this).load(imageUri).into(selectedImage);
                selectedImage.setVisibility(View.VISIBLE); // Make the ImageView visible
                removePhoto.setVisibility(View.VISIBLE); // Show the remove photo button if applicable
            }
        } else {

            selectedImage.setVisibility(View.GONE); // If no photo was picked, ensure the ImageView is not visible
            removePhoto.setVisibility(View.GONE); // Hide the remove photo button if no photo is set

        }
    }

    private void clearSelectedPhoto() {
        selectedImage.invalidate();
        selectedImage.setVisibility(View.GONE);
        isPhotoSelected = false;
        currentPost.setImageUrl("");
        postImageUri = null;
    }

    private void savePost() {
        String postText = postEditText.getText().toString();
        String postImageUriString = null;

        if (postImageUri != null) {
            postImageUriString = postImageUri.toString();
            currentPost.setImageUrl(postImageUriString);
        }
        if (!postText.isEmpty() || postImageUriString != null ) {
            currentPost.setContent(postText);

            // Update the post using ViewModel
            postViewModel.updatePost(getCurrentUserId(), currentPost.getPostId(), currentPost, this);

            Toast.makeText(EditPostActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(EditPostActivity.this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }
}