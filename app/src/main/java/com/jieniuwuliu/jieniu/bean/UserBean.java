package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserBean implements Serializable {

    /**
     * status : 0
     * msg : 获取用户信息成功
     * data : {"id":2,"CreatedAt":"2019-04-20T03:50:10Z","UpdatedAt":"2019-04-22T11:47:52Z","phone":"18691487230","password":"123456","auth":4,"code":"","personType":1,"avatar":"","nickname":"哦哟是我人做","address":{"id":30,"CreatedAt":"2019-04-22T11:47:52Z","UpdatedAt":"2019-04-22T11:47:52Z","address":"陕西省西安市灞桥区哦哟是我人做","default":true,"uid":2,"name":"","phone":"","lng":109.064671,"lat":34.273409},"vip":false,"vipTime":"","point":0,"couponNum":1,"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194742.png","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194753.png","fuwuCars":"[阿斯顿·马丁]","fuwuCar":[],"yewu":"Bbnb","wechat":"用肉在真谦虚"}
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
         * id : 2
         * CreatedAt : 2019-04-20T03:50:10Z
         * UpdatedAt : 2019-04-22T11:47:52Z
         * phone : 18691487230
         * password : 123456
         * auth : 4
         * code :
         * personType : 1
         * avatar :
         * nickname : 哦哟是我人做
         * address : {"id":30,"CreatedAt":"2019-04-22T11:47:52Z","UpdatedAt":"2019-04-22T11:47:52Z","address":"陕西省西安市灞桥区哦哟是我人做","default":true,"uid":2,"name":"","phone":"","lng":109.064671,"lat":34.273409}
         * vip : false
         * vipTime :
         * point : 0
         * couponNum : 1
         * shopPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194742.png
         * zizhiPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194753.png
         * fuwuCars : [阿斯顿·马丁]
         * fuwuCar : []
         * yewu : Bbnb
         * wechat : 用肉在真谦虚
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private String phone;
        private String password;
        private int auth;// 1已认证 2拒绝 3认证中 4未认证
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
        private double lat;
        private double lng;
        private int level;
        private String region;
        private String unionid;
        private String  wxName;
        private int todayCount;
        private int totalCount;

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getWxName() {
            return wxName;
        }

        public void setWxName(String wxName) {
            this.wxName = wxName;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
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


        public static class AddressBean implements Serializable{
            /**
             * id : 30
             * CreatedAt : 2019-04-22T11:47:52Z
             * UpdatedAt : 2019-04-22T11:47:52Z
             * address : 陕西省西安市灞桥区哦哟是我人做
             * default : true
             * uid : 2
             * name :
             * phone :
             * lng : 109.064671
             * lat : 34.273409
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
}
