package com.example.foobook_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foobook_android.R;
import com.example.foobook_android.ViewModels.UserViewModel;
import com.example.foobook_android.utility.PhotoSelectorHelper;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etDisplayName;
    private Button btnChooseFile, btnTakePicture, btnSaveChanges, btnClose;
    private ImageView ivProfilePicture;
    private TextView tvNoFileChosen;
    private PhotoSelectorHelper photoSelectorHelper;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private Uri profilePictureUri;
    private boolean isPhotoSelected = false;
    private UserViewModel userViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        etDisplayName = findViewById(R.id.etDisplayName);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnClose = findViewById(R.id.btnClose);
        ivProfilePicture = findViewById(R.id.selectedImage);
        tvNoFileChosen = findViewById(R.id.tvNoFileChosen);

        // Initialize PhotoSelectorHelper with activity callbacks
        photoSelectorHelper = new PhotoSelectorHelper(this, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE, this::setImage);


        // Set up the button listeners
        setListeners();
    }

    private void setListeners() {
        btnChooseFile.setOnClickListener(v -> photoSelectorHelper.openGallery());
        btnTakePicture.setOnClickListener(v -> photoSelectorHelper.checkCameraPermissionAndOpen());
        btnSaveChanges.setOnClickListener(v -> saveProfileChanges());
        btnClose.setOnClickListener(v -> finish());
    }

    private void observeViewModel() {
        userViewModel.getUserDetailsLiveData().observe(this, userDetails -> {
            // Handle successful update
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

        userViewModel.getErrorLiveData().observe(this, error -> {
            // Handle error
            Toast.makeText(this, "Error updating profile: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    private void setImage(Bitmap bitmap) {
        String filename = "photo_" + System.currentTimeMillis() + ".png";
        this.profilePictureUri = photoSelectorHelper.saveBitmapToFile(this, bitmap, filename);
        ivProfilePicture.setImageURI(null);
        ivProfilePicture.setImageURI(profilePictureUri);
        tvNoFileChosen.setVisibility(View.INVISIBLE);
        isPhotoSelected = true;
    }

    private void saveProfileChanges() {
        String displayName = etDisplayName.getText().toString().trim();
        String profilePicUri = (profilePictureUri != null) ? profilePictureUri.toString() : null;

        if (displayName.isEmpty()) {
            Toast.makeText(this, "Display name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Now call the updateUserDetails method from UserViewModel
        userViewModel.updateUserDetails(displayName, profilePicUri);
        navigateToFeedWithSuccessMessage();
    }

    private void navigateToFeedWithSuccessMessage() {
        Intent intent = new Intent(this, FeedActivity.class);
        // Clear the task stack and start fresh.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Show success message
        Toast.makeText(this, "User details updated successfully!", Toast.LENGTH_LONG).show();
    }

    // Handle the result from PhotoSelectorHelper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }
}
