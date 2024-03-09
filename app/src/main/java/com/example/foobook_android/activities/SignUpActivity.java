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

import com.example.foobook_android.SignUpViewModel;
import com.example.foobook_android.User;
import com.example.foobook_android.utility.FieldValidation;
import com.example.foobook_android.utility.PhotoSelectorHelper;
import com.example.foobook_android.R;
import com.example.foobook_android.utility.UserInputValidator;

public class SignUpActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 103;
    public static final int USER_CREATED_SUCCESSFULY = 201;
    public static final int USERNAME_TAKEN = 400;

    private ImageView selectedImage;
    private String profileImage;
    private Button btnCamera, btnGallery, btnClose, btnSignup;
    private EditText inputUserName, inputPassword, inputPasswordVer, inputDisplayName;

    private PhotoSelectorHelper photoSelectorHelper;
    private UserInputValidator inputValidator;
    private boolean isPhotoSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i("SignUpActivity", "onCreate");
        initializeViews();
        setupListeners();
        setupFieldValidation();
        photoSelectorHelper = new PhotoSelectorHelper(this, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE, this::setImage);
        inputValidator = new UserInputValidator(this, inputUserName, inputPassword, inputPasswordVer, inputDisplayName, isPhotoSelected);
    }

    private void setupFieldValidation() {
        FieldValidation.setupFieldValidation(inputUserName);
        FieldValidation.setupFieldValidation(inputPassword);
        FieldValidation.setupFieldValidation(inputPasswordVer);
        FieldValidation.setupFieldValidation(inputDisplayName);
    }

    private void setupListeners() {
        btnCamera.setOnClickListener(v -> photoSelectorHelper.checkCameraPermissionAndOpen());
        btnGallery.setOnClickListener(v -> photoSelectorHelper.openGallery());
        btnSignup.setOnClickListener(v -> attemptSignUp());
        btnClose.setOnClickListener(v -> navigateBack());
    }

    private void initializeViews() {
        selectedImage = findViewById(R.id.registerImage);
        btnCamera = findViewById(R.id.registerBtnTakePhoto);
        btnGallery = findViewById(R.id.registerBtnChoseFile);
        inputUserName = findViewById(R.id.registerUsername);
        inputPassword = findViewById(R.id.registerPassword);
        inputPasswordVer = findViewById(R.id.registerPasswordVerification);
        inputDisplayName = findViewById(R.id.registerDisplayName);
        btnClose = findViewById(R.id.registerBtnBack);
        btnSignup = findViewById(R.id.registerBtnSignup);
    }

    private void attemptSignUp() {
        if (inputValidator.isInputValid()) {
            SignUpViewModel signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
            User newUser = new User(inputUserName.getText().toString(),
                    inputPassword.getText().toString(),
                    inputDisplayName.getText().toString(),
                    profileImage);
            signUpViewModel.signUpUser(newUser).observe(this,userResponse -> {
                Toast.makeText(getApplicationContext(),userResponse.getMessage(), Toast.LENGTH_LONG).show();
                if (userResponse.getCode() == USER_CREATED_SUCCESSFULY) {
                    navigateToLogInActivity();
                }
            });
        }
    }

    private void navigateToLogInActivity() {
        startActivity(new Intent(this, LogInActivity.class));
    }

    private void navigateBack() {
        startActivity(new Intent(this, LogInActivity.class));
    }

    private void setImage(Bitmap bitmap) {
        String filename = "photo_" + System.currentTimeMillis() + ".png";
        Uri imageUri = photoSelectorHelper.saveBitmapToFile(this, bitmap, filename);
        profileImage = imageUri.toString();
        selectedImage.setImageURI(null);
        selectedImage.setImageURI(imageUri);
        inputValidator.setPhotoSelected(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectorHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photoSelectorHelper.openCamera();
            }
        }
    }


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