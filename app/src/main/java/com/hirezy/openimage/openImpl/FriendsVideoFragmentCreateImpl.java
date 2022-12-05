package com.hirezy.openimage.openImpl;

import com.hirezy.openimagelib.BaseFragment;
import com.hirezy.openimagelib.listener.VideoFragmentCreate;

public class FriendsVideoFragmentCreateImpl implements VideoFragmentCreate {
    @Override
    public BaseFragment createVideoFragment() {
        return new FriendsPlayerFragment();
    }
}
