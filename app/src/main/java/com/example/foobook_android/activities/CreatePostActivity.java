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
    // Binding for activity layout.
    private ActivityCreatePostBinding binding;

    // ViewModel for handling post operations.
    private PostViewModel postViewModel;

    // Request codes for camera and gallery intents.
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;

    // UI components.
    private EditText postEditText;
    private ImageView selectedImage;
    private Button postButton, removePhoto;
    private ImageButton btnGallery, btnCamera;

    // Uri for the selected image.
    private Uri postImageUri;

    // Flag to indicate whether a photo has been selected.
    private boolean isPhotoSelected = false;

    // Adapter for displaying posts (unused here but initialized for potential use).
    private PostAdapter postAdapter;

    // Current post being created (unused but potentially useful for extension).
    private Post currentPost;

    // Helper for selecting photos from camera or gallery.
    private PhotoSelectorHelper photoSelectorHelper;

    // User details fetched from SharedPreferences or ViewModel.
    private String fetchedDisplayName;
    private String fetchedProfilePic;


    // Initializes the activity and sets up UI components and helpers.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i("CreatePostActivity", "onCreate");

        // Initial setup methods.
        initializeViews();
        setPostViewModel();
        setupListeners();
        initializeHelpers();
    }

    // Fetches the current user's ID from SharedPreferences.
    private String getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }


    // Initializes the PostViewModel and sets up LiveData observers for user details.
    private void setPostViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.fetchDisplayName(this, getCurrentUserId());

        // Observers for display name and profile picture.
        postViewModel.getDisplayNameLiveData().observe(this, username -> {
            if (username != null && !username.isEmpty()) {
                this.fetchedDisplayName = username;
            }
        });
        postViewModel.getProfilePicLiveData().observe(this, profilePic -> {
            if (profilePic != null && !profilePic.isEmpty()) {
                this.fetchedProfilePic = profilePic;
            }
        });
    }

    // Initializes UI components and retrieves them by their ID.
    private void initializeViews() {
        Log.i("CreatePostActivity", "onCreate");
        postEditText = findViewById(R.id.postEditText);
        selectedImage = findViewById(R.id.selectedImage);
        postButton = findViewById(R.id.postButton);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);
        removePhoto = findViewById(R.id.removePhoto);
    }



    // Sets up listeners for UI components to handle user interactions.
    private void setupListeners() {
        // Opens gallery to select an image.
        btnGallery.setOnClickListener(v -> photoSelectorHelper.openGallery());

        // Checks for camera permission and opens the camera if granted.
        btnCamera.setOnClickListener(v -> photoSelectorHelper.checkCameraPermissionAndOpen());

        // Removes the currently selected photo.
        removePhoto.setOnClickListener(v -> removeSelectedPhoto());

        // Saves the post to the database.
        postButton.setOnClickListener(v -> savePost());
    }

    // Initializes helper objects used within the activity.
    // This includes setting up the adapter for posts and the helper for photo selection.
    private void initializeHelpers() {
        // Initializes the PostAdapter. Even though it's not used directly in this activity,
        // it's prepared for potential use, such as displaying a list of posts or a preview.
        postAdapter = new PostAdapter(this, PostManager.getPosts(), null, postViewModel);

        // Initializes the PhotoSelectorHelper with activity context and request codes for camera and gallery.
        // The setImage method reference is passed as a callback to set the image in the UI after selection.
        photoSelectorHelper = new PhotoSelectorHelper(this, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE, this::setImage);
    }


    // Removes the selected photo from the ImageView and hides the remove photo button.
    // It also sets the photo selection status to false.
    private void removeSelectedPhoto() {
        selectedImage.invalidate(); // Refresh the ImageView
        selectedImage.setVisibility(View.GONE); // Hide the ImageView
        isPhotoSelected = false; // Update photo selection status
        currentPost.setIsPhotoPicked(Post.NO_PHOTO); // Indicate that no photo is selected for the current post
    }

    // Sets the selected image in the ImageView from a Bitmap and updates UI visibility.
    // This method is called after a photo is selected or captured.
    private void setImage(Bitmap bitmap) {
        String filename = "photo_" + System.currentTimeMillis() + ".png"; // Generate a unique file name
        postImageUri = photoSelectorHelper.saveBitmapToFile(this, bitmap, filename); // Save the bitmap as a file and get its Uri
        selectedImage.setImageURI(null); // Clear any previous image
        selectedImage.setImageURI(postImageUri); // Set the new image
        removePhoto.setVisibility(View.VISIBLE); // Show the remove photo button
        selectedImage.setVisibility(View.VISIBLE); // Make the ImageView visible
        isPhotoSelected = true; // Indicate that a photo has been selected
    }


    // Saves the post to the database.
    // This method is called when the post button is clicked.
    private void savePost() {
        String postAuthor = fetchedDisplayName; // The name of the post author
        String authorProfileImage = fetchedProfilePic; // The profile picture URL of the post author

        String postText = postEditText.getText().toString(); // The text content of the post
        String postImageUriString = isPhotoSelected ? postImageUri.toString() : ""; // The Uri of the selected image as a string, if any
        String userId = getCurrentUserId(); // Fetch the current user's ID from shared preferences

        // Check if there is either text content or an image selected for the post
        if (!postText.isEmpty() || isPhotoSelected) {
            // Create a new Post object with the current details
            Post newPost = new Post(postAuthor, TimestampUtil.getCurrentTimestamp(), postText, authorProfileImage, postImageUriString);
            // Use the PostViewModel to save the new post for the user
            postViewModel.createPostForUser(userId, newPost, this);

            Toast.makeText(CreatePostActivity.this, "Post created successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Set the result code for the calling activity
            finish(); // Close the activity
        } else {
            Toast.makeText(CreatePostActivity.this, "Post text cannot be empty", Toast.LENGTH_SHORT).show(); // Show an error if there is no content for the post
        }
    }


    // Handles the result from activities started with startActivityForResult.
    // This method is called after returning from the photo selector helper.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data); // Delegate the handling to the photoSelectorHelper
    }
}