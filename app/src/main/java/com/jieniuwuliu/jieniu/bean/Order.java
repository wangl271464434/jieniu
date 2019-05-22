package com.jieniuwuliu.jieniu.bean;

public class Order {
    private int fromUid;
    private String fromAddress;
    private String fromName;
    private double fromLat;
    private double fromLng;
    private String fromPhone;
    private int toUid;
    private String toAddress;
    private String toName;
    private double toLat;
    private double toLng;
    private String toPhone;
    private int sendType;//寄件方式 1 上门取件 2 服务点自寄
    private String weight;//重量
    private int number;  //数量
    private int couponID;//优惠券id
    private int yunfeiMoney;//运费
    private int daishouMoney;//代收货款
    private int baojiajine;//保价金额
    private int baojiaMoney;//保价费
    private int totalMoney;//合计
    private int payType;  //1货到付款 2在线付款
    private String info;//物品信息
    private int payUid;
    private String finishPhone;

    public String getFinishPhone() {
        return finishPhone;
    }

    public void setFinishPhone(String finishPhone) {
        this.finishPhone = finishPhone;
    }

    public int getBaojiajine() {
        return baojiajine;
    }

    public void setBaojiajine(int baojiajine) {
        this.baojiajine = baojiajine;
    }

    public int getPayUid() {
        return payUid;
    }

    public void setPayUid(int payUid) {
        this.payUid = payUid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public double getFromLat() {
        return fromLat;
    }

    public void setFromLat(double fromLat) {
        this.fromLat = fromLat;
    }

    public double getFromLng() {
        return fromLng;
    }

    public void setFromLng(double fromLng) {
        this.fromLng = fromLng;
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

    public double getToLat() {
        return toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }

    public double getToLng() {
        return toLng;
    }

    public void setToLng(double toLng) {
        this.toLng = toLng;
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

    public int getBaojiaMoney() {
        return baojiaMoney;
    }

    public void setBaojiaMoney(int baojiaMoney) {
        this.baojiaMoney = baojiaMoney;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getDaishouMoney() {
        return daishouMoney;
    }

    public void setDaishouMoney(int daishouMoney) {
        this.daishouMoney = daishouMoney;
    }
}
