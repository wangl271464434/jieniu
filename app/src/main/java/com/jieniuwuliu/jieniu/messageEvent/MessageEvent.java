package com.jieniuwuliu.jieniu.messageEvent;

import java.util.List;

/**
 * 消息事件
 * */
public class MessageEvent {
    private String type;
    private String storeImg = "";
    private String zizhiImg = "";
    private String storeUrl = "";
    private String zizhiUrl = "";
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getZizhiUrl() {
        return zizhiUrl;
    }

    public void setZizhiUrl(String zizhiUrl) {
        this.zizhiUrl = zizhiUrl;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getZizhiImg() {
        return zizhiImg;
    }

    public void setZizhiImg(String zizhiImg) {
        this.zizhiImg = zizhiImg;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "type='" + type + '\'' +
                ", storeImg='" + storeImg + '\'' +
                ", zizhiImg='" + zizhiImg + '\'' +
                ", storeUrl='" + storeUrl + '\'' +
                ", zizhiUrl='" + zizhiUrl + '\'' +
                '}';
    }
}
