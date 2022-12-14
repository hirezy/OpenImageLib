package com.hirezy.openimage.openImpl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hirezy.openimage.bean.ImageItem;
import com.flyjingfish.openimage.databinding.LayoutFriendsBinding;
import com.hirezy.openimagelib.BaseInnerFragment;
import com.hirezy.openimagelib.beans.OpenImageUrl;
import com.hirezy.openimagelib.listener.OnSelectMediaListener;
import com.hirezy.openimagelib.utils.ScreenUtils;

public class FriendsFragment extends BaseInnerFragment {

    private LayoutFriendsBinding binding;
    private ImageItem imageItem;
    private AnimatorSet hideAnim;
    private ObjectAnimator hideTopAnim;
    private ObjectAnimator hideBottomAnim;
    private boolean isHide;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            imageItem = (ImageItem) bundle.getSerializable("ImageItem");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutFriendsBinding.inflate(inflater,container,false);
        ViewGroup.LayoutParams layoutParams = binding.vStatus.getLayoutParams();
        layoutParams.height = ScreenUtils.getStatusBarHeight(requireContext());
        binding.vStatus.setLayoutParams(layoutParams);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (imageItem != null){
            binding.tvText.setText(imageItem.text);
        }
        binding.ivBack.setOnClickListener(v -> close());

        addOnItemClickListener((fragment, openImageUrl, position) -> {
            if (isHide){
                clickShowAnim();
            }else {
                clickHideAnim();
            }
            isHide= !isHide;
        });

        addOnSelectMediaListener(new OnSelectMediaListener() {
            @Override
            public void onSelect(OpenImageUrl openImageUrl, int position) {
                Log.e("addOnSelectMedia","---"+position);
            }
        });
    }

    private void initClickAnim(boolean isHide){
        if (hideAnim == null){
            hideTopAnim = ObjectAnimator.ofFloat(binding.rlTop,"translationY",0,-binding.rlTop.getHeight());
            hideBottomAnim = ObjectAnimator.ofFloat(binding.llBottom,"translationY",0,binding.llBottom.getHeight());
            hideAnim = new AnimatorSet();
            hideAnim.playTogether(hideTopAnim, hideBottomAnim);
            hideAnim.setDuration(240);
        }

        if (isHide){
            hideTopAnim.setFloatValues(0,-binding.rlTop.getHeight());
            hideBottomAnim.setFloatValues(0,binding.llBottom.getHeight());
        }else {
            hideTopAnim.setFloatValues(-binding.rlTop.getHeight(),0);
            hideBottomAnim.setFloatValues(binding.llBottom.getHeight(),0);
        }
    }

    private void clickHideAnim(){
        initClickAnim(true);
        hideAnim.start();
    }

    private void clickShowAnim(){
        initClickAnim(false);
        hideAnim.start();
    }

    @Override
    protected void onTouchScale(float scale) {
        super.onTouchScale(scale);
        if (!isHide){
            binding.rlTop.setTranslationY(-binding.rlTop.getHeight()*(1-scale)*4);
            binding.llBottom.setTranslationY(binding.llBottom.getHeight()*(1-scale)*4);
        }
    }
}
