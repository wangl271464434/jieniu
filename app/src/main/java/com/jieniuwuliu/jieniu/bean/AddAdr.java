package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

public class AddAdr {

    /**
     * status : 0
     * msg : 新增地址信息成功
     * data : {"id":38,"CreatedAt":"2019-04-22T16:43:18.064904033Z","UpdatedAt":"2019-04-22T16:43:18.064904033Z","address":"陕西省西安市西北大学桃园校区科学楼体育","default":false,"uid":2,"name":"离职手续","phone":"58798","lng":108.893903,"lat":34.239703}
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

    public static class DataBean {
        /**
         * id : 38
         * CreatedAt : 2019-04-22T16:43:18.064904033Z
         * UpdatedAt : 2019-04-22T16:43:18.064904033Z
         * address : 陕西省西安市西北大学桃园校区科学楼体育
         * default : false
         * uid : 2
         * name : 离职手续
         * phone : 58798
         * lng : 108.893903
         * lat : 34.239703
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private String address;
        @SerializedName("default")
        private boolean defaultX;
        private int uid;
        private String name;
        private String phone;
        private double lng;
        private double lat;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isDefaultX() {
            return defaultX;
        }

        public void setDefaultX(boolean defaultX) {
            this.defaultX = defaultX;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}
