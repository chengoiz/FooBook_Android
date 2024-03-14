package com.example.foobook_android.utility;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foobook_android.R;

public class ImageUtility {
    public static void loadImage(ImageView imageView, String imageUrl, Context context) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.defaultpic)
                .error(R.drawable.saved)
                .into(imageView);
    }
}
