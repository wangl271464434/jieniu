package com.jieniuwuliu.jieniu.messageEvent;

public class VideoEvent {
    private String imgUrl = "";
    private String videoUrl = "";

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "VideoEvent{" +
                "imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
