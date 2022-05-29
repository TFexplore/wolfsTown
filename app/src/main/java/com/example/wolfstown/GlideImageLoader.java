package com.example.wolfstown;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideImageLoader extends ImageLoader {

    public void displayImage(Context context, Object path, ImageView imageView) {

        Glide.with(context).load(path).into(imageView);

    }
}