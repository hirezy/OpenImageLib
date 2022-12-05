package com.hirezy.openimagelib.listener;

import androidx.annotation.IdRes;

import com.hirezy.openimagelib.beans.OpenImageUrl;

public interface SourceImageViewIdGet<T extends OpenImageUrl> {
    @IdRes int getImageViewId(T data, int position);
}
