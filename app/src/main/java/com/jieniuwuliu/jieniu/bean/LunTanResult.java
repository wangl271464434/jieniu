package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class LunTanResult implements Serializable {

    /**
     * status : 0
     * msg : 获取所有评论成功
     * data : [{"id":4,"CreatedAt":"2019-04-26T10:39:25Z","UpdatedAt":"2019-04-26T10:39:25Z","Uid":2,"info":"测试视频","photos":"","videos":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/video/JieNiu_20190426_183857-1964743617.mp4","dianzan":[],"pinglun":[],"huifu":[],"Photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","Name":"魔神总司","type":3},{"id":3,"CreatedAt":"2019-04-26T10:34:00Z","UpdatedAt":"2019-04-26T10:34:00Z","Uid":2,"info":"测试视频","photos":"","videos":"","dianzan":[],"pinglun":[],"huifu":[],"Photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","Name":"魔神总司","type":3},{"id":2,"CreatedAt":"2019-04-26T10:33:12Z","UpdatedAt":"2019-04-26T10:33:12Z","Uid":2,"info":"测试拍照加多图片","photos":"[\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/JieNiu_20190426_183248114011023.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/magazine-unlock-01-2.3.1327-_47F02E8EF3CA6E7717E7D0C683B421C9.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/magazine-unlock-01-2.3.1327-_1D4B895A5937BF64D146ADAFFA95273E.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/magazine-unlock-01-2.3.1327-_2FE092757200FFE278742B851688BF05.jpg\"]","videos":"","dianzan":[],"pinglun":[],"huifu":[],"Photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","Name":"魔神总司","type":2},{"id":1,"CreatedAt":"2019-04-26T10:32:33Z","UpdatedAt":"2019-04-26T10:32:33Z","Uid":2,"info":"测试单图片","photos":"[\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/magazine-unlock-01-2.3.1327-_50440EBD8A60800911E226CE30EEB4C9.jpg\"]","videos":"","dianzan":[],"pinglun":[],"huifu":[],"Photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","Name":"魔神总司","type":1}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 4
         * CreatedAt : 2019-04-26T10:39:25Z
         * UpdatedAt : 2019-04-26T10:39:25Z
         * Uid : 2
         * info : 测试视频
         * photos :
         * videos : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/video/JieNiu_20190426_183857-1964743617.mp4
         * dianzan : []
         * pinglun : []
         * huifu : []
         * Photo : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg
         * Name : 魔神总司
         * type : 3
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private int uid;
        private String info;
        private String photos;
        private String video;
        private String videoImage;
        private String photo;
        private String name;
        private int type;
        private List<DianZan> dianzan;
        private List<PingLun> pinglun;
        private boolean isZan;
        private boolean isShow =false;//是否显示点赞和评论

        public boolean isZan() {
            return isZan;
        }

        public void setZan(boolean zan) {
            isZan = zan;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return CreatedAt;
        }

        public void setCreatedAt(String CreatedAt) {
            this.CreatedAt = CreatedAt;
        }

        public String getUpdatedAt() {
            return UpdatedAt;
        }

        public void setUpdatedAt(String UpdatedAt) {
            this.UpdatedAt = UpdatedAt;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getPhotos() {
            return photos;
        }

        public void setPhotos(String photos) {
            this.photos = photos;
        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<DianZan> getDianzan() {
            return dianzan;
        }

        public void setDianzan(List<DianZan> dianzan) {
            this.dianzan = dianzan;
        }

        public List<PingLun> getPinglun() {
            return pinglun;
        }

        public void setPinglun(List<PingLun> pinglun) {
            this.pinglun = pinglun;
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
}
