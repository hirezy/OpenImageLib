package com.hirezy.openimagelib.listener;

import com.hirezy.openimagelib.BaseFragment;
import com.hirezy.openimagelib.beans.OpenImageUrl;

public interface OnItemClickListener {
    void onItemClick(BaseFragment fragment, OpenImageUrl openImageUrl, int position);
}
