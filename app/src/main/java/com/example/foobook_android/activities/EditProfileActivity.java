package com.example.foobook_android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.foobook_android.comment.CommentsDataHolder;
import com.example.foobook_android.utility.ImageUtility;
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.utility.TokenManager;

public class EditProfileActivity extends AppCompatActivity {

    public static final int QUALITY = 80;
    private EditText etDisplayName;
    private Button btnChooseFile, btnTakePicture, btnSaveChanges, btnClose;
    private ImageView ivProfilePicture;
    private TextView tvNoFileChosen;
    private PhotoSelectorHelper photoSelectorHelper;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private UserViewModel userViewModel;
    private String profilePictureBase64; // Add this as a class member to store the Base64 image
    private TokenManager tokenManager; // Field to hold the TokenManager instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        tokenManager = new TokenManager(this); // Initialize the TokenManager
        setUpFields();
        setUpHelpers();
        // Set up the button listeners
        setListeners();
    }

    private void setUpFields() {
        etDisplayName = findViewById(R.id.etDisplayName);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnClose = findViewById(R.id.btnClose);
        ivProfilePicture = findViewById(R.id.selectedImage);
        tvNoFileChosen = findViewById(R.id.tvNoFileChosen);
    }

    private void setListeners() {
        btnChooseFile.setOnClickListener(v -> photoSelectorHelper.openGallery());
        btnTakePicture.setOnClickListener(v -> photoSelectorHelper.checkCameraPermissionAndOpen());
        btnSaveChanges.setOnClickListener(v -> saveProfileChanges());
        btnClose.setOnClickListener(v -> finish());
    }

    private void setImage(Bitmap bitmap) {
        profilePictureBase64 = ImageUtility.bitmapToBase64(ImageUtility.compressBitmap(bitmap, QUALITY));
        ivProfilePicture.setImageBitmap(bitmap);
        ivProfilePicture.setVisibility(View.VISIBLE);
        tvNoFileChosen.setVisibility(View.INVISIBLE);
    }

    private void setUpHelpers() {
        // Initialize PhotoSelectorHelper with activity callbacks
        photoSelectorHelper = new PhotoSelectorHelper(this, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE, this::setImage);
    }

    private void saveProfileChanges() {
        String displayName = etDisplayName.getText().toString().trim();
        if (displayName.isEmpty()) {
            Toast.makeText(this, "Display name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Now call the updateUserDetails method from UserViewModel
        userViewModel.updateUserDetails(displayName, profilePictureBase64);
        CommentsDataHolder.updateCommenterName(displayName,
                profilePictureBase64 != null ? profilePictureBase64 : getProfilePicture(), this);
        updateSharedPreferences(displayName);

        navigateToFeedActivity();
    }


    private void updateSharedPreferences(String newDisplayName) {
        tokenManager.setDisplayName(newDisplayName);
        if (profilePictureBase64 != null) {
            tokenManager.setProfilePic(profilePictureBase64);
        }// Update profile pic name
    }

    private void navigateToFeedActivity() {
        Intent intent = new Intent(this, FeedActivity.class);
        // Clear the task stack and start fresh.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Show success message
        Toast.makeText(this, "User details updated successfully!", Toast.LENGTH_LONG).show();
    }

    private String getProfilePicture() {
        return tokenManager.getProfilePic();
    }
    // Handle the result from PhotoSelectorHelper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }
}
