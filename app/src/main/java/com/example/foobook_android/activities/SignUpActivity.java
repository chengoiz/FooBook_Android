package com.example.foobook_android.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.foobook_android.ViewModels.SignUpViewModel;
import com.example.foobook_android.Api.SignUpRequest;
import com.example.foobook_android.utility.FieldValidation;
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.R;
import com.example.foobook_android.utility.UserInputValidator;

/**
 * SignUpActivity manages the user registration process, including collecting user details
 * and handling profile picture selection through camera or gallery. It validates user input,
 * communicates with the SignUpViewModel for registration, and provides feedback to the user
 * about the registration process. Successful registration redirects the user to the LogInActivity.
 */
public class SignUpActivity extends AppCompatActivity {
    // Constants for permission and request codes
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 103;
    public static final String USER_CREATED_SUCCESSFULLY = "User created successfully";

    // ViewModel for handling signup operations
    private SignUpViewModel signUpViewModel;
    // UI components
    private ImageView selectedImage;
    private Button btnCamera, btnGallery, btnClose, btnSignup;
    private EditText inputUserName, inputPassword, inputPasswordVer, inputDisplayName;

    // Helper for handling photo selection and permissions
    private PhotoSelectorHelper photoSelectorHelper;
    // Validator for user input
    private UserInputValidator inputValidator;
    // Flag to track if a photo has been selected
    private boolean isPhotoSelected = false;
    // Path of the selected profile image
    private String profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i("SignUpActivity", "onCreate");
        initializeViews();
        setupListeners();
        setupFieldValidation();
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        // Observes the response from the SignUpViewModel
        signUpViewModel.getUserResponseLiveData().observe(this, userResponse -> {
            Toast.makeText(getApplicationContext(), userResponse.getMessage(), Toast.LENGTH_LONG).show();
            if (USER_CREATED_SUCCESSFULLY.equals(userResponse.getMessage())) {
                navigateToLogInActivity(); // Navigate to login if sign up is successful
            }
        });

        // Initialize helper and validator
        photoSelectorHelper = new PhotoSelectorHelper(this, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE, this::setImage);
        inputValidator = new UserInputValidator(this, inputUserName, inputPassword, inputPasswordVer, inputDisplayName, isPhotoSelected);
    }

    // Sets up validation for input fields
    private void setupFieldValidation() {
        FieldValidation.setupFieldValidation(inputUserName);
        FieldValidation.setupFieldValidation(inputPassword);
        FieldValidation.setupFieldValidation(inputPasswordVer);
        FieldValidation.setupFieldValidation(inputDisplayName);
    }

    // Sets up click listeners for buttons
    private void setupListeners() {
        btnCamera.setOnClickListener(v -> photoSelectorHelper.checkCameraPermissionAndOpen());
        btnGallery.setOnClickListener(v -> photoSelectorHelper.openGallery());
        btnSignup.setOnClickListener(v -> attemptSignUp()); // Attempts sign up
        btnClose.setOnClickListener(v -> navigateBack()); // Navigates back to the login screen
    }

    // Initializes UI components
    private void initializeViews() {
        selectedImage = findViewById(R.id.registerImage);
        btnCamera = findViewById(R.id.registerBtnTakePhoto);
        btnGallery = findViewById(R.id.registerBtnChoseFile);
        btnClose = findViewById(R.id.registerBtnBack);
        btnSignup = findViewById(R.id.registerBtnSignup);
        inputUserName = findViewById(R.id.registerUsername);
        inputPassword = findViewById(R.id.registerPassword);
        inputPasswordVer = findViewById(R.id.registerPasswordVerification);
        inputDisplayName = findViewById(R.id.registerDisplayName);
    }

    // Attempts to sign up with the provided information
    private void attemptSignUp() {
        if (inputValidator.isInputValid()) {
            SignUpRequest signUpRequest = new SignUpRequest(
                    inputUserName.getText().toString(),
                    inputPassword.getText().toString(),
                    inputDisplayName.getText().toString(),
                    profileImage
            );
            signUpViewModel.signUpUser(signUpRequest);
        }
    }

    // Navigates to LogInActivity
    private void navigateToLogInActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    // Navigates back to the LogInActivity
    private void navigateBack() {
        finish();
    }

    // Sets the selected image in the ImageView and updates the validator
    private void setImage(Bitmap bitmap) {
        String filename = "photo_" + System.currentTimeMillis() + ".png";
        Uri imageUri = photoSelectorHelper.saveBitmapToFile(this, bitmap, filename);
        profileImage = imageUri.toString();
        selectedImage.setImageURI(null);
        selectedImage.setImageURI(imageUri);
        isPhotoSelected = true;
        inputValidator.setPhotoSelected(true);
    }

    // Handles activity result for photo selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }

    // Handles the result of permission requests, particularly for camera access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If camera permission is granted, open the camera
                photoSelectorHelper.openCamera();
            } else {
                // Handle the case where camera permission is denied
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lifecycle methods with logging for monitoring the activity's state changes
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("SignUpActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("SignUpActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("SignUpActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("SignUpActivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("SignUpActivity", "onRestart");
    }
}