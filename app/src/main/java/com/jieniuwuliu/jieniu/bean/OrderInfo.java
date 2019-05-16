package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable{
    private int id;
    private String CreatedAt;
    private String UpdatedAt;
    private int fromUid;
    private String fromAddress;
    private String fromName;
    private double fromLng;
    private double fromLat;
    private String fromPhone;
    private int toUid;
    private String toAddress;
    private String toName;
    private double toLng;
    private double toLat;
    private String toPhone;
    private int sendType;
    private String weight;
    private int number;
    private int couponID;
    private int yunfeiMoney;
    private int daishouMoney;
    private int baojiajine;
    private int baojiaMoney;
    private int payType;
    private int totalMoney;
    private int status;
    private String orderNumber;
    private int kuaidiID;
    private String info;
    private boolean isInvoice = false;//是否开发票
    private List<OrderListBean> orderList;

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public boolean isInvoice() {
        return isInvoice;
    }

    public void setInvoice(boolean invoice) {
        isInvoice = invoice;
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

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public double getFromLng() {
        return fromLng;
    }

    public void setFromLng(double fromLng) {
        this.fromLng = fromLng;
    }

    public double getFromLat() {
        return fromLat;
    }

    public void setFromLat(double fromLat) {
        this.fromLat = fromLat;
    }

    public String getFromPhone() {
        return fromPhone;
    }

    public void setFromPhone(String fromPhone) {
        this.fromPhone = fromPhone;
    }

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public double getToLng() {
        return toLng;
    }

    public void setToLng(double toLng) {
        this.toLng = toLng;
    }

    public double getToLat() {
        return toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }

    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }

    public int getYunfeiMoney() {
        return yunfeiMoney;
    }

    public void setYunfeiMoney(int yunfeiMoney) {
        this.yunfeiMoney = yunfeiMoney;
    }

    public int getDaishouMoney() {
        return daishouMoney;
    }

    public void setDaishouMoney(int daishouMoney) {
        this.daishouMoney = daishouMoney;
    }

    public int getBaojiajine() {
        return baojiajine;
    }

    public void setBaojiajine(int baojiajine) {
        this.baojiajine = baojiajine;
    }

    public int getBaojiaMoney() {
        return baojiaMoney;
    }

    public void setBaojiaMoney(int baojiaMoney) {
        this.baojiaMoney = baojiaMoney;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getKuaidiID() {
        return kuaidiID;
    }

    public void setKuaidiID(int kuaidiID) {
        this.kuaidiID = kuaidiID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public static class OrderListBean implements Serializable {
        /**
         * ID : 1
         * CreatedAt : 2019-04-30T18:22:18Z
         * UpdatedAt : 2019-04-30T18:22:26Z
         * OrderNumber : 1122395766657126400
         * Uid : 2
         * Name : 老王
         * Status : false
         * Phone : 18691487231
         * Lat : 34.039598
         * Lng : 108.58623
         * Region : 东区
         * Level : 1
         */

        private int ID;
        private String CreatedAt;
        private String UpdatedAt;
        private String OrderNumber;
        private int Uid;
        private String Name;
        private boolean Status;
        private String Phone;
        private double Lat;
        private double Lng;
        private String Region;
        private int Level;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
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

        public String getOrderNumber() {
            return OrderNumber;
        }

        public void setOrderNumber(String OrderNumber) {
            this.OrderNumber = OrderNumber;
        }

        public int getUid() {
            return Uid;
        }

        public void setUid(int Uid) {
            this.Uid = Uid;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean Status) {
            this.Status = Status;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double Lat) {
            this.Lat = Lat;
        }

        public double getLng() {
            return Lng;
        }

        public void setLng(double Lng) {
            this.Lng = Lng;
        }

        public String getRegion() {
            return Region;
        }

        public void setRegion(String Region) {
            this.Region = Region;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int Level) {
            this.Level = Level;
        }
    }
}
