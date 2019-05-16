package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

public class LoginBean {

    /**
     * status : 0
     * msg : 登录成功
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZG1pbiI6MSwicGhvbmUiOiIxODY5MTQ4NzIzMCIsInVpZCI6Mn0.QMieUs3S6s6mGfJapYXzIWZ3FrfeUZw8t4BN7RCo7mU
     * data : {"id":2,"CreatedAt":"2019-04-20T03:50:10Z","UpdatedAt":"2019-04-22T11:47:52Z","phone":"18691487230","password":"123456","auth":4,"code":"","personType":1,"avatar":"","nickname":"哦哟是我人做","address":{"id":0,"CreatedAt":"0001-01-01T00:00:00Z","UpdatedAt":"0001-01-01T00:00:00Z","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0},"vip":false,"vipTime":"","point":0,"couponNum":0,"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194742.png","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194753.png","fuwuCars":"[阿斯顿·马丁]","fuwuCar":null,"yewu":"Bbnb","wechat":"用肉在真谦虚"}
     */

    private int status;
    private String msg;
    private String token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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
         * address : {"id":0,"CreatedAt":"0001-01-01T00:00:00Z","UpdatedAt":"0001-01-01T00:00:00Z","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0}
         * vip : false
         * vipTime :
         * point : 0
         * couponNum : 0
         * shopPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194742.png
         * zizhiPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194753.png
         * fuwuCars : [阿斯顿·马丁]
         * fuwuCar : null
         * yewu : Bbnb
         * wechat : 用肉在真谦虚
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private String phone;
        private String password;
        private int auth;
        private String code;
        private int personType;
        private String avatar;
        private String nickname;
        private AddressBean address;
        private boolean vip;
        private String vipTime;
        private int point;
        private int couponNum;
        private String shopPhoto;
        private String zizhiPhoto;
        private String fuwuCars;
        private Object fuwuCar;
        private String yewu;
        private String wechat;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getZizhiPhoto() {
            return zizhiPhoto;
        }

        public void setZizhiPhoto(String zizhiPhoto) {
            this.zizhiPhoto = zizhiPhoto;
        }

        public String getFuwuCars() {
            return fuwuCars;
        }

        public void setFuwuCars(String fuwuCars) {
            this.fuwuCars = fuwuCars;
        }

        public Object getFuwuCar() {
            return fuwuCar;
        }

        public void setFuwuCar(Object fuwuCar) {
            this.fuwuCar = fuwuCar;
        }

        public String getYewu() {
            return yewu;
        }

        public void setYewu(String yewu) {
            this.yewu = yewu;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
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
