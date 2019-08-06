package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class XJCarType {
    private int status;
    private String msg;
    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String Brands;
        private String Brand;
        private String Imgurl;
        private List<String> Models;
        private boolean isShow = false;
        public String getBrands() {
            return Brands;
        }

        public void setBrands(String Brands) {
            this.Brands = Brands;
        }

        public String getBrand() {
            return Brand;
        }

        public void setBrand(String Brand) {
            this.Brand = Brand;
        }

        public String getImgurl() {
            return Imgurl;
        }

        public void setImgurl(String Imgurl) {
            this.Imgurl = Imgurl;
        }

        public List<String> getModels() {
            return Models;
        }

        public void setModels(List<String> Models) {
            this.Models = Models;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }
    }
}
