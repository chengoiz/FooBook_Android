package com.example.foobook_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class SignUpActivity extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 103;
    ImageView selectedImage;
    TextView imageUriTextView;
    Button btnCamera, btnGallery, btnClose, btnSignup;
    private EditText inputUserName, inputPassword, inputPasswordVer, inputDisplayName ;
    private boolean isPhotoSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.i("SignUpActivity", "onCreate");

        selectedImage = findViewById(R.id.registerImage);
        btnCamera = findViewById(R.id.registerBtnTakePhoto);
        btnGallery = findViewById(R.id.registerBtnChoseFile);
        inputUserName = findViewById(R.id.registerUsername);
        inputPassword = findViewById(R.id.registerPassword);
        inputPasswordVer = findViewById(R.id.registerPasswordVerification);
        inputDisplayName = findViewById(R.id.registerDisplayName);
        btnClose = findViewById(R.id.registerBtnBack);
        btnSignup = findViewById(R.id.registerBtnSignup);

        btnCamera.setOnClickListener(v -> cameraPermission());
        btnGallery.setOnClickListener(v -> openGallery());
        btnSignup.setOnClickListener(v -> {
            checkInput();
        });

        btnClose.setOnClickListener(v -> {
            Intent i = new Intent(this, LogInActivity.class);
            startActivity(i);
        });

        FieldValidation.setupFieldValidation(inputUserName);
        FieldValidation.setupFieldValidation(inputPassword);
        FieldValidation.setupFieldValidation(inputPasswordVer);
        FieldValidation.setupFieldValidation(inputDisplayName);
    }




    private void checkInput() {
        String userName = inputUserName.getText().toString();
        String password = inputPassword.getText().toString();
        String passwordVer = inputPasswordVer.getText().toString();
        String displayName = inputDisplayName.getText().toString();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Must insert username", Toast.LENGTH_SHORT).show();
        } else if (!PasswordValidator.validatePassword(password)) {
            Toast.makeText(this, "password must be at least 8 figures\n" +
                            "password must contain at least 1 letter & 1 number",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!passwordVer.equals(password)) {
            Toast.makeText(this, "password not equal", Toast.LENGTH_SHORT).show();
        } else if (displayName.isEmpty()) {
            Toast.makeText(this, "Must insert display name", Toast.LENGTH_SHORT).show();
        } else if (!isPhotoSelected) {
            Toast.makeText(this, "Please select or capture a photo.", Toast.LENGTH_SHORT).show();
        } else

            Toast.makeText(this, "Register completed successfully",
                    Toast.LENGTH_SHORT).show();
    }




    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                selectedImage.setImageBitmap(image);
                isPhotoSelected = true;
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                selectedImage.setImageURI(selectedImageUri);
                isPhotoSelected = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
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