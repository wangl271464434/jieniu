package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PSYUser {

    /**
     * status : 0
     * msg : 获取所有可改派快递员信息成功
     * data : [{"id":43,"CreatedAt":"2019-04-20T03:50:10Z","UpdatedAt":"2019-05-04T07:17:05Z","phone":"18691487232","password":"1234567","auth":1,"code":"","personType":3,"nickname":"小王","address":{"id":0,"CreatedAt":"0001-01-01T00:00:00Z","UpdatedAt":"0001-01-01T00:00:00Z","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0},"vip":false,"vipTime":"","point":0,"couponNum":0,"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","wechat":"","lng":108.58623,"lat":34.039598,"level":1,"region":"东区","todayCount":0,"totalCount":0},{"id":44,"CreatedAt":"2019-04-20T03:50:10Z","UpdatedAt":"2019-05-04T07:17:05Z","phone":"18691487233","password":"1234567","auth":1,"code":"","personType":3,"nickname":"小小王","address":{"id":0,"CreatedAt":"0001-01-01T00:00:00Z","UpdatedAt":"0001-01-01T00:00:00Z","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0},"vip":false,"vipTime":"","point":0,"couponNum":0,"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","wechat":"","lng":108.58623,"lat":34.039598,"level":1,"region":"东区","todayCount":0,"totalCount":0}]
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
         * id : 43
         * CreatedAt : 2019-04-20T03:50:10Z
         * UpdatedAt : 2019-05-04T07:17:05Z
         * phone : 18691487232
         * password : 1234567
         * auth : 1
         * code :
         * personType : 3
         * nickname : 小王
         * address : {"id":0,"CreatedAt":"0001-01-01T00:00:00Z","UpdatedAt":"0001-01-01T00:00:00Z","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0}
         * vip : false
         * vipTime :
         * point : 0
         * couponNum : 0
         * shopPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg
         * wechat :
         * lng : 108.58623
         * lat : 34.039598
         * level : 1
         * region : 东区
         * todayCount : 0
         * totalCount : 0
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private String phone;
        private String password;
        private int auth;
        private String code;
        private int personType;
        private String nickname;
        private AddressBean address;
        private boolean vip;
        private String vipTime;
        private int point;
        private int couponNum;
        private String shopPhoto;
        private String wechat;
        private double lng;
        private double lat;
        private int level;
        private String region;
        private int todayCount;
        private int totalCount;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getAuth() {
            return auth;
        }

        public void setAuth(int auth) {
            this.auth = auth;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getPersonType() {
            return personType;
        }

        public void setPersonType(int personType) {
            this.personType = personType;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public AddressBean getAddress() {
            return address;
        }

        public void setAddress(AddressBean address) {
            this.address = address;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public String getVipTime() {
            return vipTime;
        }

        public void setVipTime(String vipTime) {
            this.vipTime = vipTime;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public int getCouponNum() {
            return couponNum;
        }

        public void setCouponNum(int couponNum) {
            this.couponNum = couponNum;
        }

        public String getShopPhoto() {
            return shopPhoto;
        }

        public void setShopPhoto(String shopPhoto) {
            this.shopPhoto = shopPhoto;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
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

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getTodayCount() {
            return todayCount;
        }

        public void setTodayCount(int todayCount) {
            this.todayCount = todayCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public static class AddressBean {
            /**
             * id : 0
             * CreatedAt : 0001-01-01T00:00:00Z
             * UpdatedAt : 0001-01-01T00:00:00Z
             * address :
             * default : false
             * uid : 0
             * name :
             * phone :
             * lng : 0
             * lat : 0
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
            private int lng;
            private int lat;

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

            public int getLng() {
                return lng;
            }

            public void setLng(int lng) {
                this.lng = lng;
            }

            public int getLat() {
                return lat;
            }

            public void setLat(int lat) {
                this.lat = lat;
            }
        }
    }
}
