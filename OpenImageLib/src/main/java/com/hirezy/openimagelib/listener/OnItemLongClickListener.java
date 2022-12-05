package com.hirezy.openimagelib.listener;


import com.hirezy.openimagelib.BaseFragment;
import com.hirezy.openimagelib.beans.OpenImageUrl;

public interface OnItemLongClickListener {
    void onItemLongClick(BaseFragment fragment, OpenImageUrl openImageUrl, int position);
}
