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

import com.bumptech.glide.Glide;


public class EditPostActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;

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
        Log.i("EditPostActivity", "onCreate");
        initializeViewComponents();
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
        isPhotoSelected = true;
        currentPost.setIsPhotoPicked(Post.PHOTO_PICKED);
    }


    private void handleIncomingIntent() {
        currentPost = (Post) getIntent().getSerializableExtra("postDetails");
        assert currentPost != null;
        populateUIWithPostDetails(currentPost);
        postPosition = getIntent().getIntExtra("postPosition", -1);

    }

    private void populateUIWithPostDetails(Post currentPost) {
        postEditText.setText(currentPost.getContent());
        if (currentPost.getIsPhotoPicked() == Post.PHOTO_PICKED) {
            // If post have image url (json)
            if (currentPost.getPostImageUrl() != null) {
                selectedImage.setImageURI(Uri.parse(currentPost.getPostImageUrl()));
                removePhoto.setVisibility(View.VISIBLE);
                Glide.with(this).load(currentPost.getPostImageUrl()).into(selectedImage);
            }

            // If post have image URI
            else if (currentPost.getPostImageUri() != null) {
                selectedImage.setImageURI(currentPost.getPostImageUri());
                removePhoto.setVisibility(View.VISIBLE);
                Glide.with(this).load(currentPost.getPostImageUri()).into(selectedImage);
            }
        }
    }
    private void clearSelectedPhoto() {
        selectedImage.invalidate();
        selectedImage.setVisibility(View.GONE);
        isPhotoSelected = false;
        currentPost.setIsPhotoPicked(Post.NO_PHOTO);
    }

    private void savePost() {
        String postText = postEditText.getText().toString();
        if (!postText.isEmpty()) {
            currentPost.setContent(postText);

            // Update the image URI if a new photo was selected
            if (isPhotoSelected && postImageUri != null) {
                currentPost.setPostImage(postImageUri.toString());
                currentPost.setImageSetByUser(isPhotoSelected);
                selectedImage.setVisibility(View.VISIBLE);
            }

            PostManager.updatePost(postPosition, currentPost);
            postAdapter.notifyItemChanged(postPosition, currentPost);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("postDetails", currentPost);
            resultIntent.putExtra("postPosition", postPosition);
            setResult(RESULT_OK);
        } else {
            Toast.makeText(EditPostActivity.this, "post text cannot be empty",
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }
}