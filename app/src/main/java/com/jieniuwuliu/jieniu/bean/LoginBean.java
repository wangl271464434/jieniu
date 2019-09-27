package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

public class LoginBean {

    /**
     * status : 0
     * msg : 成功
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTY5NjU0NzIyLCJsZXZlbCI6IiIsIm5hbWUiOiIiLCJwaG9uZSI6IjE1NjE5MzcwMjY3IiwidHlwZSI6MCwidWlkIjozNjE2fQ.9zGf0N1822DoyFADkrSrgjtDqVHQtXUsV2L1MsSfYWk
     * data : {"id":3616,"CreatedAt":"2019-09-27 14:44:05","UpdatedAt":"2019-09-27 14:47:20","phone":"15619370267","auth":0,"code":"","personType":0,"nickname":"","address":{"id":0,"CreatedAt":"1970-01-01 08:00:00","UpdatedAt":"1970-01-01 08:00:00","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0},"vip":false,"vipTime":"1970-01-01 08:00:00","point":0,"couponNum":0,"shopPhoto":"","wechat":"","todayCount":0,"totalCount":0,"addr":"","unionid":"oGtAC1CmLO1msstcMHAqhME9_KUk","wxName":"","original":"","display":0,"sort":0,"pushq":0,"landline":"","storeinform":"","label":"","partscity":0,"openid":"oC9zb1WFI-YbhojYklUoRT48mRKY"}
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
         * id : 3616
         * CreatedAt : 2019-09-27 14:44:05
         * UpdatedAt : 2019-09-27 14:47:20
         * phone : 15619370267
         * auth : 0
         * code :
         * personType : 0
         * nickname :
         * address : {"id":0,"CreatedAt":"1970-01-01 08:00:00","UpdatedAt":"1970-01-01 08:00:00","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0}
         * vip : false
         * vipTime : 1970-01-01 08:00:00
         * point : 0
         * couponNum : 0
         * shopPhoto :
         * wechat :
         * todayCount : 0
         * totalCount : 0
         * addr :
         * unionid : oGtAC1CmLO1msstcMHAqhME9_KUk
         * wxName :
         * original :
         * display : 0
         * sort : 0
         * pushq : 0
         * landline :
         * storeinform :
         * label :
         * partscity : 0
         * openid : oC9zb1WFI-YbhojYklUoRT48mRKY
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private String phone;
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
        private int todayCount;
        private int totalCount;
        private String addr;
        private String unionid;
        private String wxName;
        private String original;
        private int display;
        private int sort;
        private int pushq;
        private String landline;
        private String storeinform;
        private String label;
        private int partscity;
        private String openid;

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

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

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

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public int getDisplay() {
            return display;
        }

        public void setDisplay(int display) {
            this.display = display;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getPushq() {
            return pushq;
        }

        public void setPushq(int pushq) {
            this.pushq = pushq;
        }

        public String getLandline() {
            return landline;
        }

        public void setLandline(String landline) {
            this.landline = landline;
        }

        public String getStoreinform() {
            return storeinform;
        }

        public void setStoreinform(String storeinform) {
            this.storeinform = storeinform;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getPartscity() {
            return partscity;
        }

        public void setPartscity(int partscity) {
            this.partscity = partscity;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public static class AddressBean {
            /**
             * id : 0
             * CreatedAt : 1970-01-01 08:00:00
             * UpdatedAt : 1970-01-01 08:00:00
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

            @Override
            public String toString() {
                return "AddressBean{" +
                        "id=" + id +
                        ", CreatedAt='" + CreatedAt + '\'' +
                        ", UpdatedAt='" + UpdatedAt + '\'' +
                        ", address='" + address + '\'' +
                        ", defaultX=" + defaultX +
                        ", uid=" + uid +
                        ", name='" + name + '\'' +
                        ", phone='" + phone + '\'' +
                        ", lng=" + lng +
                        ", lat=" + lat +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", CreatedAt='" + CreatedAt + '\'' +
                    ", UpdatedAt='" + UpdatedAt + '\'' +
                    ", phone='" + phone + '\'' +
                    ", auth=" + auth +
                    ", code='" + code + '\'' +
                    ", personType=" + personType +
                    ", nickname='" + nickname + '\'' +
                    ", address=" + address +
                    ", vip=" + vip +
                    ", vipTime='" + vipTime + '\'' +
                    ", point=" + point +
                    ", couponNum=" + couponNum +
                    ", shopPhoto='" + shopPhoto + '\'' +
                    ", wechat='" + wechat + '\'' +
                    ", todayCount=" + todayCount +
                    ", totalCount=" + totalCount +
                    ", addr='" + addr + '\'' +
                    ", unionid='" + unionid + '\'' +
                    ", wxName='" + wxName + '\'' +
                    ", original='" + original + '\'' +
                    ", display=" + display +
                    ", sort=" + sort +
                    ", pushq=" + pushq +
                    ", landline='" + landline + '\'' +
                    ", storeinform='" + storeinform + '\'' +
                    ", label='" + label + '\'' +
                    ", partscity=" + partscity +
                    ", openid='" + openid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
