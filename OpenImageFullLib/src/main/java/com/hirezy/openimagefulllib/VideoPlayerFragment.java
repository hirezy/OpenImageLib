package com.hirezy.openimagefulllib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.flyjingfish.openimagefulllib.R;
import com.hirezy.openimagelib.BaseImageFragment;
import com.hirezy.openimagelib.photoview.PhotoView;
import com.hirezy.openimagelib.widget.LoadingView;

public class VideoPlayerFragment extends BaseImageFragment<LoadingView> {

    protected String playerKey;
    protected boolean isPlayed;
    protected boolean isLoadImageFinish;
    protected GSYVideoPlayer videoPlayer;
    private View rootView;
    private PhotoView smallImageView;
    private PhotoView photoImageView;
    private LoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video,container,false);
        OpenImageVideoPlayer videoPlayer = rootView.findViewById(R.id.video_player);
        this.videoPlayer = videoPlayer;
        smallImageView = videoPlayer.getSmallCoverImageView();
        photoImageView = videoPlayer.getCoverImageView();
        loadingView = (LoadingView) videoPlayer.getLoadingView();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (videoPlayer.getBackButton() != null){
            videoPlayer.getBackButton().setOnClickListener(v -> close());
        }
        playerKey = videoPlayer.getVideoKey();
        videoPlayer.goneAllWidget();
        isPlayed = false;
    }

    @Override
    protected PhotoView getSmallCoverImageView() {
        return smallImageView;
    }

    @Override
    protected PhotoView getPhotoView() {
        return photoImageView;
    }

    @Override
    protected View getItemClickableView() {
        return videoPlayer;
    }

    @Override
    protected LoadingView getLoadingView() {
        return loadingView;
    }

    @Override
    protected void hideLoading(LoadingView pbLoading) {
        super.hideLoading(pbLoading);
        if (videoPlayer.getStartButton() != null){
            videoPlayer.getStartButton().setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showLoading(LoadingView pbLoading) {
        super.showLoading(pbLoading);
        if (videoPlayer.getStartButton() != null){
            videoPlayer.getStartButton().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onTouchClose(float scale) {
        super.onTouchClose(scale);
        if (videoPlayer.getTextureViewContainer() != null){
            videoPlayer.getTextureViewContainer().setVisibility(View.GONE);
        }
        videoPlayer.goneAllWidget();
    }

    @Override
    protected void onTouchScale(float scale) {
        super.onTouchScale(scale);
        videoPlayer.goneAllWidget();
        if (scale == 1){
            videoPlayer.showAllWidget();
        }
    }

    @Override
    protected void loadImageFinish(boolean isLoadImageSuccess) {
        isLoadImageFinish = true;
        play();
    }

    @Override
    protected void onTransitionEnd() {
        super.onTransitionEnd();
        play();
    }

    private void play(){
        if (isTransitionEnd && isLoadImageFinish && !isPlayed){
            if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                toPlay4Resume();
            }else {
                getLifecycle().addObserver(new LifecycleEventObserver() {
                    @Override
                    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                        if (event == Lifecycle.Event.ON_RESUME){
                            toPlay4Resume();
                            source.getLifecycle().removeObserver(this);
                        }
                    }
                });
            }

            isPlayed = true;
        }
    }

    protected void toPlay4Resume(){
        videoPlayer.playUrl(openImageUrl.getVideoUrl());
        videoPlayer.startPlayLogic();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (playerKey != null) {
            GSYVideoController.resumeByKey(playerKey);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (playerKey != null) {
            GSYVideoController.pauseByKey(playerKey);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (playerKey != null) {
            GSYVideoController.cancelByKeyAndDeleteKey(playerKey);
        }
    }
}
