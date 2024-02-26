package com.example.foobook_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


public class CreatePostActivity extends AppCompatActivity  {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;

    private EditText postEditText;
    private ImageView selectedImage;
    private Button postButton, removePhoto;
    private ImageButton btnGallery, btnCamera;
    private Uri postImageUri;
    private boolean isPhotoSelected = false;

    private PostAdapter postAdapter;
    private Post currentPost;
    private PhotoSelectorHelper photoSelectorHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Log.i("CreatePostActivity", "onCreate");
        initializeViews();
        setupListeners();
        initializeHelpers();
    }

    private void initializeViews() {
        Log.i("CreatePostActivity", "onCreate");
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
        removePhoto.setOnClickListener(v -> removeSelectedPhoto());
        postButton.setOnClickListener(v -> savePost());
    }

    private void initializeHelpers() {
        postAdapter = new PostAdapter(this, PostManager.getPosts(), null);
        photoSelectorHelper = new PhotoSelectorHelper(this, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE, this::setImage);
    }

    private void removeSelectedPhoto() {
        selectedImage.invalidate();
        selectedImage.setVisibility(View.GONE);
        isPhotoSelected = false;
        currentPost.setIsPhotoPicked(Post.NO_PHOTO);
    }

    private void setImage(Bitmap bitmap) {
        String filename = "photo_" + System.currentTimeMillis() + ".png";
        postImageUri = photoSelectorHelper.saveBitmapToFile(this, bitmap, filename);
        selectedImage.setImageURI(null);
        selectedImage.setImageURI(postImageUri);
        selectedImage.setVisibility(View.VISIBLE);
        isPhotoSelected = true;
    }

    private void savePost() {
        // hard coded for now
        String postAuthor = "Tomer";
        String authorProfileImage = getResources().getResourceName(R.drawable.defaultpic);
        String postText = postEditText.getText().toString();
        if (!postText.isEmpty() || isPhotoSelected) {
            Post newPost;
            if (isPhotoSelected && postImageUri != null) {
                newPost = new Post(postAuthor, TimestampUtil.getCurrentTimestamp(), postText, authorProfileImage, postImageUri.toString());
                newPost.setImageSetByUser(isPhotoSelected);
                newPost.setIsPhotoPicked(Post.PHOTO_PICKED);
            } else {
                newPost = new Post(postAuthor, TimestampUtil.getCurrentTimestamp(), postText, authorProfileImage);
                newPost.setIsPhotoPicked(Post.NO_PHOTO);
            }
            PostManager.addPost(newPost);
            postAdapter.notifyItemInserted(0);
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(CreatePostActivity.this, "post text cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }
}


