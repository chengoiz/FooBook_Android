package com.example.foobook_android.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.foobook_android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class PhotoSelectorHelper {

    private final Activity activity;
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

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, cameraRequestCode);
        } else {
            Toast.makeText(activity, "Unable to open camera", Toast.LENGTH_SHORT).show();
        }
    }


    public void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(pickPhotoIntent, galleryRequestCode);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == cameraRequestCode && data != null && data.getExtras() != null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                onPhotoSelected.accept(image);
            } else if (requestCode == galleryRequestCode && data != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),
                            imageUri);
                    onPhotoSelected.accept(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
