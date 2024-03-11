package com.example.foobook_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

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

import com.example.foobook_android.daos.PostDao;
import com.example.foobook_android.database.PostDB;
import com.example.foobook_android.databinding.ActivityCreatePostBinding;
import com.example.foobook_android.databinding.ActivityMainBinding;
import com.example.foobook_android.post.PostViewModel;
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.post.Post;
import com.example.foobook_android.post.PostManager;
import com.example.foobook_android.R;
import com.example.foobook_android.utility.TimestampUtil;
import com.example.foobook_android.adapters.PostAdapter;


public class CreatePostActivity extends AppCompatActivity  {
    private ActivityCreatePostBinding binding;
    private PostViewModel postViewModel;
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
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i("CreatePostActivity", "onCreate");
        initializeViews();
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        setupListeners();
        initializeHelpers();
        // Removed direct database access initialization.
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
        String postAuthor = "Tomer"; // This should eventually be replaced with dynamic data
        String authorProfileImage = getResources().getResourceName(R.drawable.defaultpic);
        String postText = postEditText.getText().toString();
        String postImageUriString = isPhotoSelected ? postImageUri.toString() : "";

        if (!postText.isEmpty() || isPhotoSelected) {
            Post newPost = new Post(postAuthor, TimestampUtil.getCurrentTimestamp(), postText, authorProfileImage, postImageUriString);
            newPost.setImageSetByUser(isPhotoSelected);
            newPost.setIsPhotoPicked(Post.PHOTO_PICKED);

            // Use ViewModel to save the post
            postViewModel.insert(newPost); // Assuming postViewModel is already initialized in your activity

            Toast.makeText(CreatePostActivity.this, "Post created successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(CreatePostActivity.this, "Post text cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }
}


