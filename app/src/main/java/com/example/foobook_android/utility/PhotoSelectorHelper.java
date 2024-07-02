package com.example.foobook_android.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

// Helper class for selecting photos from camera or gallery in Android.
public class PhotoSelectorHelper {

    private final Activity activity;
    private String currentPhotoPath;


    // Request codes for camera and gallery
    private final int cameraRequestCode;
    private final int galleryRequestCode;
    private final Consumer<Bitmap> onPhotoSelected;

    public PhotoSelectorHelper(Activity activity, int cameraRequestCode, int galleryRequestCode,
                               Consumer<Bitmap> onPhotoSelected) {
        this.activity = activity;
        this.cameraRequestCode = cameraRequestCode;
        this.galleryRequestCode = galleryRequestCode;
        this.onPhotoSelected = onPhotoSelected;
    }

    // Saves a bitmap to a file and returns its URI
    public Uri saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        File cachePath = new File(context.getExternalCacheDir(), "my_images");
        cachePath.mkdirs();
        File imagePath = new File(cachePath, fileName);
        try (FileOutputStream fos = new FileOutputStream(imagePath)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    imagePath);
        } catch (IOException e) {
            Log.e("ImageUtil", "Error saving image", e);
            return null;
        }
    }

    // Checks camera permission and opens camera if granted
    public void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    cameraRequestCode);
        } else {
            openCamera();
        }
    }

    // Opens the camera for capturing an image
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(activity, "Could not create image file", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, cameraRequestCode);
            }
        } else {
            Toast.makeText(activity, "Unable to open camera", Toast.LENGTH_SHORT).show();
        }
    }

    // Creates a file to store the captured image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = TimestampUtil.getCurrentTimestampFile();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Opens the gallery for selecting an image
    public void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(pickPhotoIntent, galleryRequestCode);
    }

    // Handles the result from the camera or gallery activity
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == cameraRequestCode) {
                File imgFile = new File(currentPhotoPath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    onPhotoSelected.accept(myBitmap);
                }
            } else if (requestCode == galleryRequestCode && data != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
                    onPhotoSelected.accept(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
