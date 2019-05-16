package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class LunTanBean {
    private int uid;  //发表人id
    private String info; //发表内容
    private String video; //视频链接
    private String videoImage; //视频封面
    private String photos;//图片数组链接 json型的数组
    private int type = 1; //类型 1单图片 2多图片 3视频

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }
}
