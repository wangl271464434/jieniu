package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class PSOrderInfo implements Serializable{

    /**
     * status : 0
     * msg : 获取订单成功
     * data : [{"id":3,"CreatedAt":"2019-04-28T06:45:13Z","UpdatedAt":"2019-04-28T06:45:13Z","fromUid":41,"fromAddress":"陕西省西安市雁塔区高新大都荟","fromName":"测试门店","fromLng":108.893905,"fromLat":34.237733,"fromPhone":"7567989798","toUid":0,"toAddress":"陕西省西安市户县石井镇石西村","toName":"下午","toLng":108.58623,"toLat":34.039598,"toPhone":"89798998","sendType":2,"weight":"小于10kg","number":3,"couponID":10,"yunfeiMoney":0,"daishouMoney":586,"baojiajine":0,"baojiaMoney":18,"payType":0,"totalMloney":604,"status":5,"orderNumber":"1122391298372800512","kuaidiID":2,"kuaidiStatus":false,"kuaidiName":"","kuaidiPhone":"","info":"快考试了","region":"","isInvoice":false},{"id":4,"CreatedAt":"2019-04-28T07:02:59Z","UpdatedAt":"2019-04-28T07:02:59Z","fromUid":41,"fromAddress":"陕西省西安市雁塔区高新大都荟","fromName":"测试门店","fromLng":108.893905,"fromLat":34.237733,"fromPhone":"7567989798","toUid":0,"toAddress":"陕西省西安市长安区郭杜十字","toName":"墨迹无聊","toLng":108.86388,"toLat":34.1601,"toPhone":"67987888","sendType":1,"weight":"小于10kg","number":3,"couponID":0,"yunfeiMoney":25,"daishouMoney":3000,"baojiajine":0,"baojiaMoney":18,"payType":0,"totalMloney":3043,"status":0,"orderNumber":"1122395766657126400","kuaidiID":2,"kuaidiStatus":false,"kuaidiName":"","kuaidiPhone":"","info":"or你是外星人","region":"","isInvoice":false}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 3
         * CreatedAt : 2019-04-28T06:45:13Z
         * UpdatedAt : 2019-04-28T06:45:13Z
         * fromUid : 41
         * fromAddress : 陕西省西安市雁塔区高新大都荟
         * fromName : 测试门店
         * fromLng : 108.893905
         * fromLat : 34.237733
         * fromPhone : 7567989798
         * toUid : 0
         * toAddress : 陕西省西安市户县石井镇石西村
         * toName : 下午
         * toLng : 108.58623
         * toLat : 34.039598
         * toPhone : 89798998
         * sendType : 2
         * weight : 小于10kg
         * number : 3
         * couponID : 10
         * yunfeiMoney : 0
         * daishouMoney : 586
         * baojiajine : 0
         * baojiaMoney : 18
         * payType : 0
         * totalMloney : 604
         * status : 5
         * orderNumber : 1122391298372800512
         * kuaidiID : 2
         * kuaidiStatus : false
         * kuaidiName :
         * kuaidiPhone :
         * info : 快考试了
         * region :
         * isInvoice : false
         */

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
        private int totalMloney;
        private int status;
        private String orderNumber;
        private int kuaidiID;
        private boolean kuaidiStatus;
        private String kuaidiName;
        private String kuaidiPhone;
        private String info;
        private String region;
        private boolean isInvoice;

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

        public int getTotalMloney() {
            return totalMloney;
        }

        public void setTotalMloney(int totalMloney) {
            this.totalMloney = totalMloney;
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

        public boolean isKuaidiStatus() {
            return kuaidiStatus;
        }

        public void setKuaidiStatus(boolean kuaidiStatus) {
            this.kuaidiStatus = kuaidiStatus;
        }

        public String getKuaidiName() {
            return kuaidiName;
        }

        public void setKuaidiName(String kuaidiName) {
            this.kuaidiName = kuaidiName;
        }

        public String getKuaidiPhone() {
            return kuaidiPhone;
        }

        public void setKuaidiPhone(String kuaidiPhone) {
            this.kuaidiPhone = kuaidiPhone;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public boolean isIsInvoice() {
            return isInvoice;
        }

        public void setIsInvoice(boolean isInvoice) {
            this.isInvoice = isInvoice;
        }
    }
}
