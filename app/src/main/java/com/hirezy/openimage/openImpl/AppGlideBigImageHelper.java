package com.hirezy.openimage.openImpl;

import android.content.Context;
import android.widget.ImageView;

import com.hirezy.openimage.imageloader.MyImageLoader;
import com.hirezy.openimageglidelib.GlideBigImageHelper;
import com.hirezy.openimagelib.listener.OnLoadBigImageListener;
import com.squareup.picasso.Picasso;

public class AppGlideBigImageHelper extends GlideBigImageHelper {
    @Override
    public void loadImage(Context context, String imageUrl, OnLoadBigImageListener onLoadBigImageListener) {
        if (MyImageLoader.loader_os_type == MyImageLoader.GLIDE){
            super.loadImage(context, imageUrl, onLoadBigImageListener);
        }else {
            new PicassoLoader(context, imageUrl, onLoadBigImageListener).load();
        }

    }

    @Override
    public void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (MyImageLoader.loader_os_type == MyImageLoader.GLIDE){
            super.loadImage(context, imageUrl, imageView);
        }else {
            Picasso.get().load(imageUrl).into(imageView);
        }

    }

}
