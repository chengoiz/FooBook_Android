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

import com.example.foobook_android.databinding.ActivityCreatePostBinding;
import com.example.foobook_android.ViewModels.PostViewModel;
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
    private String fetchedDisplayName;
    private String fetchedProfilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i("CreatePostActivity", "onCreate");
        initializeViews();
        setPostViewModel();
        setupListeners();
        initializeHelpers();
    }

    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", ""); // Replace "userId" with the actual key you used to store the user's ID
    }

    private void setPostViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        postViewModel.setToken(token);
        postViewModel.fetchUsername(token, this);
        // Observe the username LiveData
        postViewModel.getUsernameLiveData().observe(this, username -> {
            if (username != null && !username.isEmpty()) {
                this.fetchedDisplayName = username;
            }
        });
        postViewModel.getProfilePicLiveData().observe(this, profilePic -> {
            if (profilePic != null && !profilePic.isEmpty()) {
                this.fetchedProfilePic = profilePic;
                // Here you might want to load the profile picture into an ImageView or similar
            }
        });
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

        String postAuthor = fetchedDisplayName;
//        String authorProfileImage = getResources().getResourceName(R.drawable.defaultpic);
        String authorProfileImage = fetchedProfilePic;

        String postText = postEditText.getText().toString();
        String postImageUriString = isPhotoSelected ? postImageUri.toString() : "";
        String userId = getCurrentUserId(); // Fetch the current user's ID


        if (!postText.isEmpty() || isPhotoSelected) {
            Post newPost = new Post(postAuthor, TimestampUtil.getCurrentTimestamp(), postText, authorProfileImage, postImageUriString);
            newPost.setImageSetByUser(isPhotoSelected);
            newPost.setIsPhotoPicked(Post.PHOTO_PICKED);
//            newPost.setCreatedBy(userId); // Set the creator's user ID

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


