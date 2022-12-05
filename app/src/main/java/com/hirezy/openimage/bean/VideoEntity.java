package com.hirezy.openimage.bean;

import com.hirezy.openimagelib.beans.OpenImageUrl;
import com.hirezy.openimagelib.enums.MediaType;

public class VideoEntity implements OpenImageUrl {
    public String videoUrl;
    public String coverUrl;
    public String smallCoverUrl;

    @Override
    public String getImageUrl() {
        return coverUrl;
    }

    @Override
    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public String getCoverImageUrl() {
        return smallCoverUrl;
    }

    @Override
    public MediaType getType() {
        return MediaType.VIDEO;
    }
}
