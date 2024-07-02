package com.example.foobook_android.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foobook_android.R;

import java.io.ByteArrayOutputStream;

public class ImageUtility {
    private static final int ZERO = 0;
    private static final int PHOTO_QUALITY = 90;

    public static void loadImage(ImageView imageView, String imageUrl, Context context) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.defaultpic)
                .error(R.drawable.saved)
                .into(imageView);
    }
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, PHOTO_QUALITY, outputStream); // Compress Bitmap
        byte[] byteArray = outputStream.toByteArray();
        String base64Image = "";
        if (byteArray != null && byteArray.length > 0) {
            String base64EncodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            base64Image = "data:image/jpeg;base64," + base64EncodedImage;
        }
        return base64Image;
    }

    public static Bitmap base64ToBitmap(String base64Str) {
        // Check for and remove data URI scheme if present
        if (base64Str.startsWith("data:image/png;base64,")) {
            base64Str = base64Str.substring("data:image/png;base64,".length());
        } else if (base64Str.startsWith("data:image/jpeg;base64,")) {
            base64Str = base64Str.substring("data:image/jpeg;base64,".length());
        }
        // Decode base64 string
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);

        // Convert the byte array to Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, ZERO, decodedBytes.length);
    }

    // Compress a bitmap to a specified quality
    public static Bitmap compressBitmap(Bitmap originalBitmap, int quality) {
        // Compress the original bitmap to a byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);

        // Decode the byte array back into a bitmap
        byte[] bitmapData = out.toByteArray();
        return BitmapFactory.decodeByteArray(bitmapData, ZERO, bitmapData.length);
    }
    // Resize a bitmap to a specified width and height
    public static Bitmap resizeBitmap(Bitmap bitmap, float maxWidth, float maxHeight) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = maxWidth / originalWidth;
        float scaleHeight = maxHeight / originalHeight;
        float scaleFactor = Math.min(scaleWidth, scaleHeight); // Maintains the aspect ratio

        return Bitmap.createScaledBitmap(bitmap, (int)(originalWidth * scaleFactor)
                , (int)(originalHeight * scaleFactor), true);
    }
    public static boolean isImageUrl(String source) {
        return source.startsWith("http://") || source.startsWith("https://");
    }

    public static boolean isBase64(String source) {
        return source.startsWith("data:image/") || (source.length() > 100 && !source.contains(" "));
    }
}
