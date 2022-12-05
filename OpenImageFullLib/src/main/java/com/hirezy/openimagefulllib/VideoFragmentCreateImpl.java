package com.hirezy.openimagefulllib;

import com.hirezy.openimagelib.BaseFragment;
import com.hirezy.openimagelib.listener.VideoFragmentCreate;

public class VideoFragmentCreateImpl implements VideoFragmentCreate {
    @Override
    public BaseFragment createVideoFragment() {
        return new VideoPlayerFragment();
    }
}
