package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchStore {

    /**
     * status : 0
     * msg : 获取所有门店信息成功
     * data : [{"uid":4,"CreatedAt":"2019-04-22T13:32:12Z","UpdatedAt":"2019-05-08T10:47:59Z","auth":1,"personType":2,"nickname":"捷牛快修","address":{"id":65,"CreatedAt":"2019-04-26T12:21:51Z","UpdatedAt":"2019-04-27T14:38:15Z","address":"陕西省西安市长安区南横线十字西北角","default":true,"uid":4,"name":"苏","phone":"15384642801","lng":108.834331,"lat":34.113337},"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190424190604.jpg","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/timg-2.jpeg","fuwuCars":"","fuwuCar":[],"yewu":"钣金烤漆,机修电路,保险理赔","photos":"[\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/ad_002_0.png\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20181230142047.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20181230142035.jpg\"]","isFollow":false},{"uid":42,"CreatedAt":"2019-04-27T14:58:19Z","UpdatedAt":"2019-04-27T15:07:57Z","auth":1,"personType":2,"nickname":"捷牛快修中企汽配城第2分店","address":{"id":77,"CreatedAt":"2019-04-27T15:02:48Z","UpdatedAt":"2019-04-27T15:02:48Z","address":"陕西省西安市灞桥区中企汽配装饰城","default":true,"uid":42,"name":"田","phone":"13720403565","lng":109.049102,"lat":34.279001},"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190415185705.jpg","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190126181638.jpg","fuwuCars":"","fuwuCar":[],"yewu":"机修电路,救援服务","photos":"[\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/20190425191500-200x200.png\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Screenshot_2019-04-27-20-23-41-92.png\"]","isFollow":false}]
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
         * uid : 4
         * CreatedAt : 2019-04-22T13:32:12Z
         * UpdatedAt : 2019-05-08T10:47:59Z
         * auth : 1
         * personType : 2
         * nickname : 捷牛快修
         * address : {"id":65,"CreatedAt":"2019-04-26T12:21:51Z","UpdatedAt":"2019-04-27T14:38:15Z","address":"陕西省西安市长安区南横线十字西北角","default":true,"uid":4,"name":"苏","phone":"15384642801","lng":108.834331,"lat":34.113337}
         * shopPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190424190604.jpg
         * zizhiPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/timg-2.jpeg
         * fuwuCars :
         * fuwuCar : []
         * yewu : 钣金烤漆,机修电路,保险理赔
         * photos : ["http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/ad_002_0.png","http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20181230142047.jpg","http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20181230142035.jpg"]
         * isFollow : false
         */

        private int uid;
        private String CreatedAt;
        private String UpdatedAt;
        private int auth;
        private int personType;
        private String nickname;
        private AddressBean address;
        private String shopPhoto;
        private String zizhiPhoto;
        private String fuwuCars;
        private String yewu;
        private String photos;
        private boolean isFollow;
        private List<?> fuwuCar;

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

        public String getPhotos() {
            return photos;
        }

        public void setPhotos(String photos) {
            this.photos = photos;
        }

        public boolean isIsFollow() {
            return isFollow;
        }

        public void setIsFollow(boolean isFollow) {
            this.isFollow = isFollow;
        }

        public List<?> getFuwuCar() {
            return fuwuCar;
        }

        public void setFuwuCar(List<?> fuwuCar) {
            this.fuwuCar = fuwuCar;
        }

        public static class AddressBean {
            /**
             * id : 65
             * CreatedAt : 2019-04-26T12:21:51Z
             * UpdatedAt : 2019-04-27T14:38:15Z
             * address : 陕西省西安市长安区南横线十字西北角
             * default : true
             * uid : 4
             * name : 苏
             * phone : 15384642801
             * lng : 108.834331
             * lat : 34.113337
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
