package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Follow {

    /**
     * status : 0
     * msg : 获取关注列表成功
     * data : [{"id":1,"CreatedAt":"2019-04-24T05:31:04Z","UpdatedAt":"2019-04-24T05:31:04Z","myUid":2,"fUid":2,"store":{"uid":2,"CreatedAt":"2019-04-20T03:50:10Z","UpdatedAt":"2019-04-26T14:16:15Z","auth":4,"personType":2,"nickname":"末日预言","address":{"id":56,"CreatedAt":"2019-04-25T11:58:32Z","UpdatedAt":"2019-04-26T14:16:15Z","address":"陕西省西安市户县石井镇石西村","default":true,"uid":2,"name":"贸易术语","phone":"99497997998","lng":108.58623,"lat":34.039598},"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_04.jpg","fuwuCars":"[\"奥迪\",\"阿斯顿·马丁\"]","fuwuCar":null,"yewu":"请选择","photos":"[\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_06.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190424_024201.png\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20180707_112234.jpg\"]","isFollow":false}},{"id":8,"CreatedAt":"2019-04-27T01:41:18Z","UpdatedAt":"2019-04-27T01:41:18Z","myUid":2,"fUid":3,"store":{"uid":3,"CreatedAt":"2019-04-20T07:33:55Z","UpdatedAt":"2019-04-27T07:36:28Z","auth":4,"personType":1,"nickname":"捷牛汽配","address":{"id":68,"CreatedAt":"2019-04-27T07:36:28Z","UpdatedAt":"2019-04-27T07:36:28Z","address":"陕西省西安市莲湖区西站街8号","default":true,"uid":3,"name":"苏","phone":"13991167774","lng":108.915075,"lat":34.272583},"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190401105741.jpg","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/timg-87.jpeg","fuwuCars":"[\"奥迪\",\"奔驰\",\"本田\",\"宝马\",\"别克\"]","fuwuCar":null,"yewu":"请选择","photos":"[\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190424184645.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190424184454.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG20190424184437.jpg\"]","isFollow":false}}]
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
         * id : 1
         * CreatedAt : 2019-04-24T05:31:04Z
         * UpdatedAt : 2019-04-24T05:31:04Z
         * myUid : 2
         * fUid : 2
         * store : {"uid":2,"CreatedAt":"2019-04-20T03:50:10Z","UpdatedAt":"2019-04-26T14:16:15Z","auth":4,"personType":2,"nickname":"末日预言","address":{"id":56,"CreatedAt":"2019-04-25T11:58:32Z","UpdatedAt":"2019-04-26T14:16:15Z","address":"陕西省西安市户县石井镇石西村","default":true,"uid":2,"name":"贸易术语","phone":"99497997998","lng":108.58623,"lat":34.039598},"shopPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","zizhiPhoto":"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_04.jpg","fuwuCars":"[\"奥迪\",\"阿斯顿·马丁\"]","fuwuCar":null,"yewu":"请选择","photos":"[\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_06.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190424_024201.png\",\"http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20180707_112234.jpg\"]","isFollow":false}
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private int myUid;
        private int fUid;
        private StoreBean store;

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

        public int getMyUid() {
            return myUid;
        }

        public void setMyUid(int myUid) {
            this.myUid = myUid;
        }

        public int getFUid() {
            return fUid;
        }

        public void setFUid(int fUid) {
            this.fUid = fUid;
        }

        public StoreBean getStore() {
            return store;
        }

        public void setStore(StoreBean store) {
            this.store = store;
        }

        public static class StoreBean {
            /**
             * uid : 2
             * CreatedAt : 2019-04-20T03:50:10Z
             * UpdatedAt : 2019-04-26T14:16:15Z
             * auth : 4
             * personType : 2
             * nickname : 末日预言
             * address : {"id":56,"CreatedAt":"2019-04-25T11:58:32Z","UpdatedAt":"2019-04-26T14:16:15Z","address":"陕西省西安市户县石井镇石西村","default":true,"uid":2,"name":"贸易术语","phone":"99497997998","lng":108.58623,"lat":34.039598}
             * shopPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg
             * zizhiPhoto : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_04.jpg
             * fuwuCars : ["奥迪","阿斯顿·马丁"]
             * fuwuCar : null
             * yewu : 请选择
             * photos : ["http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_06.jpg","http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_05.jpg","http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20190424_024201.png","http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/IMG_20180707_112234.jpg"]
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
            private Object fuwuCar;
            private String yewu;
            private String photos;
            private boolean isFollow;

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

            public static class AddressBean {
                /**
                 * id : 56
                 * CreatedAt : 2019-04-25T11:58:32Z
                 * UpdatedAt : 2019-04-26T14:16:15Z
                 * address : 陕西省西安市户县石井镇石西村
                 * default : true
                 * uid : 2
                 * name : 贸易术语
                 * phone : 99497997998
                 * lng : 108.58623
                 * lat : 34.039598
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
}
