package com.hirezy.openimage.openImpl;


import com.hirezy.openimagelib.BaseInnerFragment;
import com.hirezy.openimagelib.listener.UpperLayerFragmentCreate;

public class FriendLayerFragmentCreateImpl implements UpperLayerFragmentCreate {
    @Override
    public BaseInnerFragment createVideoFragment() {
        return new FriendsFragment();
    }
}
