package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class UnReadMsg implements Serializable{

    /**
     * status : 0
     * msg : 成功
     * data : [{"id":82,"CreatedAt":"2019-05-15T06:07:11Z","UpdatedAt":"2019-05-15T06:07:11Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":81,"CreatedAt":"2019-05-15T06:06:28Z","UpdatedAt":"2019-05-15T06:06:28Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":80,"CreatedAt":"2019-05-15T06:05:05Z","UpdatedAt":"2019-05-15T06:05:05Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":79,"CreatedAt":"2019-05-15T01:41:17Z","UpdatedAt":"2019-05-15T01:41:17Z","fid":212,"uid":3,"name":"申钺汽修厂","info":"你好，测试提示","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190401105741.jpg","rname":"","ruid":0},{"id":78,"CreatedAt":"2019-05-14T14:28:30Z","UpdatedAt":"2019-05-14T14:28:30Z","fid":212,"uid":4,"name":"捷牛快修西电科大第三分店","info":"👿","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/timg.jpeg","rname":"","ruid":0},{"id":77,"CreatedAt":"2019-05-14T10:04:02Z","UpdatedAt":"2019-05-14T10:04:02Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":76,"CreatedAt":"2019-05-14T10:03:11Z","UpdatedAt":"2019-05-14T10:03:11Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":75,"CreatedAt":"2019-05-14T09:50:48Z","UpdatedAt":"2019-05-14T09:50:48Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":74,"CreatedAt":"2019-05-14T09:49:31Z","UpdatedAt":"2019-05-14T09:49:31Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0},{"id":73,"CreatedAt":"2019-05-14T09:46:10Z","UpdatedAt":"2019-05-14T09:46:10Z","fid":217,"uid":2,"name":"老王","info":"好","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","rname":"","ruid":0}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 82
         * CreatedAt : 2019-05-15T06:07:11Z
         * UpdatedAt : 2019-05-15T06:07:11Z
         * fid : 217
         * uid : 2
         * name : 老王
         * info : 好
         * photo : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg
         * rname :
         * ruid : 0
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private int fid;
        private int uid;
        private String name;
        private String info;
        private String photo;
        private String rname;
        private int ruid;

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

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getRname() {
            return rname;
        }

        public void setRname(String rname) {
            this.rname = rname;
        }

        public int getRuid() {
            return ruid;
        }

        public void setRuid(int ruid) {
            this.ruid = ruid;
        }
    }
}
