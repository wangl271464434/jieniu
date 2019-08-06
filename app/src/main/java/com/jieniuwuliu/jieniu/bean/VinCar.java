package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;

public class VinCar implements Serializable {
    private int status;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public static class Data implements Serializable {
        private String Cartype;
        private String States;
        private String Brand ;
        private String Logos;

        public String getBrand() {
            return Brand;
        }

        public void setBrand(String brand) {
            Brand = brand;
        }

        public String getCartype() {
            return Cartype;
        }

        public void setCartype(String Cartype) {
            this.Cartype = Cartype;
        }

        public String getStates() {
            return States;
        }

        public void setStates(String States) {
            this.States = States;
        }

        public String getLogos() {
            return Logos;
        }

        public void setLogos(String Logos) {
            this.Logos = Logos;
        }
    }
}
