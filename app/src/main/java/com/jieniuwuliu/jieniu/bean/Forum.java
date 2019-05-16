package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class Forum implements Serializable{


    /**
     * status : 0
     * msg : 成功
     * data : {"id":217,"CreatedAt":"2019-05-14T09:09:30Z","UpdatedAt":"2019-05-14T09:09:30Z","uid":41,"info":"123123","photos":"","video":"","videoImage":"","dianzan":[{"id":32,"CreatedAt":"2019-05-14T09:13:11Z","UpdatedAt":"2019-05-14T09:13:11Z","fid":217,"uid":41,"name":"测试门店"},{"id":33,"CreatedAt":"2019-05-14T09:20:47Z","UpdatedAt":"2019-05-14T09:20:47Z","fid":217,"uid":2,"name":"老王"}],"pinglun":[{"id":62,"CreatedAt":"2019-05-14T09:21:29Z","UpdatedAt":"2019-05-14T09:21:29Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":63,"CreatedAt":"2019-05-14T09:24:24Z","UpdatedAt":"2019-05-14T09:24:24Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":64,"CreatedAt":"2019-05-14T09:24:51Z","UpdatedAt":"2019-05-14T09:24:51Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":65,"CreatedAt":"2019-05-14T09:25:25Z","UpdatedAt":"2019-05-14T09:25:25Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":66,"CreatedAt":"2019-05-14T09:26:14Z","UpdatedAt":"2019-05-14T09:26:14Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":67,"CreatedAt":"2019-05-14T09:33:29Z","UpdatedAt":"2019-05-14T09:33:29Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":68,"CreatedAt":"2019-05-14T09:34:14Z","UpdatedAt":"2019-05-14T09:34:14Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":69,"CreatedAt":"2019-05-14T09:35:56Z","UpdatedAt":"2019-05-14T09:35:56Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":70,"CreatedAt":"2019-05-14T09:37:21Z","UpdatedAt":"2019-05-14T09:37:21Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":71,"CreatedAt":"2019-05-14T09:43:15Z","UpdatedAt":"2019-05-14T09:43:15Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":72,"CreatedAt":"2019-05-14T09:44:37Z","UpdatedAt":"2019-05-14T09:44:37Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":73,"CreatedAt":"2019-05-14T09:46:10Z","UpdatedAt":"2019-05-14T09:46:10Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":74,"CreatedAt":"2019-05-14T09:49:31Z","UpdatedAt":"2019-05-14T09:49:31Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":75,"CreatedAt":"2019-05-14T09:50:48Z","UpdatedAt":"2019-05-14T09:50:48Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":76,"CreatedAt":"2019-05-14T10:03:11Z","UpdatedAt":"2019-05-14T10:03:11Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":77,"CreatedAt":"2019-05-14T10:04:02Z","UpdatedAt":"2019-05-14T10:04:02Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":80,"CreatedAt":"2019-05-15T06:05:05Z","UpdatedAt":"2019-05-15T06:05:05Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":81,"CreatedAt":"2019-05-15T06:06:28Z","UpdatedAt":"2019-05-15T06:06:28Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":82,"CreatedAt":"2019-05-15T06:07:11Z","UpdatedAt":"2019-05-15T06:07:11Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0}],"photo":"","name":"","type":1,"isZan":true}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 217
         * CreatedAt : 2019-05-14T09:09:30Z
         * UpdatedAt : 2019-05-14T09:09:30Z
         * uid : 41
         * info : 123123
         * photos :
         * video :
         * videoImage :
         * dianzan : [{"id":32,"CreatedAt":"2019-05-14T09:13:11Z","UpdatedAt":"2019-05-14T09:13:11Z","fid":217,"uid":41,"name":"测试门店"},{"id":33,"CreatedAt":"2019-05-14T09:20:47Z","UpdatedAt":"2019-05-14T09:20:47Z","fid":217,"uid":2,"name":"老王"}]
         * pinglun : [{"id":62,"CreatedAt":"2019-05-14T09:21:29Z","UpdatedAt":"2019-05-14T09:21:29Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":63,"CreatedAt":"2019-05-14T09:24:24Z","UpdatedAt":"2019-05-14T09:24:24Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":64,"CreatedAt":"2019-05-14T09:24:51Z","UpdatedAt":"2019-05-14T09:24:51Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":65,"CreatedAt":"2019-05-14T09:25:25Z","UpdatedAt":"2019-05-14T09:25:25Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":66,"CreatedAt":"2019-05-14T09:26:14Z","UpdatedAt":"2019-05-14T09:26:14Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":67,"CreatedAt":"2019-05-14T09:33:29Z","UpdatedAt":"2019-05-14T09:33:29Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":68,"CreatedAt":"2019-05-14T09:34:14Z","UpdatedAt":"2019-05-14T09:34:14Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":69,"CreatedAt":"2019-05-14T09:35:56Z","UpdatedAt":"2019-05-14T09:35:56Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":70,"CreatedAt":"2019-05-14T09:37:21Z","UpdatedAt":"2019-05-14T09:37:21Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":71,"CreatedAt":"2019-05-14T09:43:15Z","UpdatedAt":"2019-05-14T09:43:15Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":72,"CreatedAt":"2019-05-14T09:44:37Z","UpdatedAt":"2019-05-14T09:44:37Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":73,"CreatedAt":"2019-05-14T09:46:10Z","UpdatedAt":"2019-05-14T09:46:10Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":74,"CreatedAt":"2019-05-14T09:49:31Z","UpdatedAt":"2019-05-14T09:49:31Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":75,"CreatedAt":"2019-05-14T09:50:48Z","UpdatedAt":"2019-05-14T09:50:48Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":76,"CreatedAt":"2019-05-14T10:03:11Z","UpdatedAt":"2019-05-14T10:03:11Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":77,"CreatedAt":"2019-05-14T10:04:02Z","UpdatedAt":"2019-05-14T10:04:02Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":80,"CreatedAt":"2019-05-15T06:05:05Z","UpdatedAt":"2019-05-15T06:05:05Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":81,"CreatedAt":"2019-05-15T06:06:28Z","UpdatedAt":"2019-05-15T06:06:28Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":82,"CreatedAt":"2019-05-15T06:07:11Z","UpdatedAt":"2019-05-15T06:07:11Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0}]
         * photo :
         * name :
         * type : 1
         * isZan : true
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
        private boolean isZan;
        private List<DianZan> dianzan;
        private List<PingLun> pinglun;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isIsZan() {
            return isZan;
        }

        public void setIsZan(boolean isZan) {
            this.isZan = isZan;
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
    }
}
