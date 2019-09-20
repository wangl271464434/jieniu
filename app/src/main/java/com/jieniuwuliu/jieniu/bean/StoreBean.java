package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * 门店
 * */
public class StoreBean {

    /**
     * status : 0
     * msg : 获取所有汽配商信息成功
     * data : [{"uid":2,"CreatedAt":"2019-04-20T03:50:10Z","UpdatedAt":"2019-04-22T11:47:52Z","auth":4,"personType":1,"nickname":"哦哟是我人做","address":{"id":30,"CreatedAt":"2019-04-22T11:47:52Z","UpdatedAt":"2019-04-22T11:47:52Z","address":"陕西省西安市灞桥区哦哟是我人做","default":true,"uid":2,"name":"","phone":"","lng":109.064671,"lat":34.273409},"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194742.png","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194753.png","fuwuCars":"[阿斯顿·马丁]","fuwuCar":[],"yewu":"Bbnb","wechat":"用肉在真谦虚"},{"uid":3,"CreatedAt":"2019-04-20T07:33:55Z","UpdatedAt":"2019-04-20T09:01:08Z","auth":4,"personType":1,"nickname":"捷牛汽配","address":{"id":0,"CreatedAt":"0001-01-01T00:00:00Z","UpdatedAt":"0001-01-01T00:00:00Z","address":"","default":false,"uid":0,"name":"","phone":"","lng":0,"lat":0},"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/JieNiu_20190420_1700554324561740108802429.jpg","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/JieNiu_20190420_1701045340724655274270215.jpg","fuwuCars":"","fuwuCar":[],"yewu":"","wechat":"a251857374"}]
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

    public static class DataBean  implements Comparable<DataBean>{
        /**
         * uid : 2
         * CreatedAt : 2019-04-20T03:50:10Z
         * UpdatedAt : 2019-04-22T11:47:52Z
         * auth : 4
         * personType : 1
         * nickname : 哦哟是我人做
         * address : {"id":30,"CreatedAt":"2019-04-22T11:47:52Z","UpdatedAt":"2019-04-22T11:47:52Z","address":"陕西省西安市灞桥区哦哟是我人做","default":true,"uid":2,"name":"","phone":"","lng":109.064671,"lat":34.273409}
         * shopPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194742.png
         * zizhiPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190422_194753.png
         * fuwuCars : [阿斯顿·马丁]
         * fuwuCar : []
         * yewu : Bbnb
         * wechat : 用肉在真谦虚
         */

        private int uid;
        private String CreatedAt;
        private String UpdatedAt;
        private int auth;
        private int partscity;
        private int personType;
        private String nickname;
        private String label;
        private AddressBean address;
        private String shopPhoto;
        private String zizhiPhoto;
        private String fuwuCars;
        private String yewu;
        private String wechat;
        private List<Car> fuwuCar;
        private double distance;

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

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public int getAuth() {
            return auth;
        }

        public void setAuth(int auth) {
            this.auth = auth;
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

        public List<Car> getFuwuCar() {
            return fuwuCar;
        }

        public void setFuwuCar(List<Car> fuwuCar) {
            this.fuwuCar = fuwuCar;
        }

        @Override
        public int compareTo(DataBean o) {
            return (int) (this.distance-o.getDistance());
        }

        public static class AddressBean {
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
