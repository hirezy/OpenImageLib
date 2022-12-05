package com.hirezy.openimagelib.listener;

import android.widget.ImageView;

import com.hirezy.openimagelib.beans.OpenImageUrl;

public interface SourceImageViewGet<T extends OpenImageUrl> {
    ImageView getImageView(T data, int position);
}
