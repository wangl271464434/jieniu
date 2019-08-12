package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class RecomStore {

    /**
     * status : 0
     * msg : 成功
     * data : [{"id":4,"CreatedAt":"2019-05-13T10:39:01Z","UpdatedAt":"2019-05-13T10:39:01Z","uid":36,"name":"玉林汽配","photo":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190401120155.jpg"}]
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

    public static class DataBean {
        /**
         * id : 4
         * CreatedAt : 2019-05-13T10:39:01Z
         * UpdatedAt : 2019-05-13T10:39:01Z
         * uid : 36
         * name : 玉林汽配
         * photo : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190401120155.jpg
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private int uid;
        private String name;
        private String photo;
        private String address ;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
