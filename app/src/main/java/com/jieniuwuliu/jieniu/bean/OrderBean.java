package com.jieniuwuliu.jieniu.bean;

public class OrderBean {

    /**
     * status : 0
     * msg : 成功
     * data : {"id":28,"CreatedAt":"2019-05-15T14:11:37.064255914Z","UpdatedAt":"2019-05-15T14:11:37.064255914Z","fromUid":41,"fromAddress":"陕西省西安市雁塔区高新大都荟","fromName":"测试门店","fromLng":108.893905,"fromLat":34.237733,"fromPhone":"9949898","toUid":4,"toAddress":"陕西省西安市长安区西安电子科技大学南校区","toName":"捷牛快修西电科大第三分店","toLng":108.930393,"toLat":34.117174,"toPhone":"15991701410","sendType":2,"weight":"小于10kg","number":3,"couponID":0,"yunfeiMoney":15,"daishouMoney":0,"baojiajine":0,"baojiaMoney":0,"payType":0,"totalMloney":15,"status":"0","orderNumber":"1128664231701385216","kuaidiID":0,"kuaidiStatus":false,"kuaidiName":"","kuaidiPhone":"","info":"箱子","region":"","isInvoice":false,"payStatus":false}
     */

    private int status;
    private String msg;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 28
         * CreatedAt : 2019-05-15T14:11:37.064255914Z
         * UpdatedAt : 2019-05-15T14:11:37.064255914Z
         * fromUid : 41
         * fromAddress : 陕西省西安市雁塔区高新大都荟
         * fromName : 测试门店
         * fromLng : 108.893905
         * fromLat : 34.237733
         * fromPhone : 9949898
         * toUid : 4
         * toAddress : 陕西省西安市长安区西安电子科技大学南校区
         * toName : 捷牛快修西电科大第三分店
         * toLng : 108.930393
         * toLat : 34.117174
         * toPhone : 15991701410
         * sendType : 2
         * weight : 小于10kg
         * number : 3
         * couponID : 0
         * yunfeiMoney : 15
         * daishouMoney : 0
         * baojiajine : 0
         * baojiaMoney : 0
         * payType : 0
         * totalMloney : 15
         * status : 0
         * orderNumber : 1128664231701385216
         * kuaidiID : 0
         * kuaidiStatus : false
         * kuaidiName :
         * kuaidiPhone :
         * info : 箱子
         * region :
         * isInvoice : false
         * payStatus : false
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
        private int totalMoney;
        private String status;
        private String orderNumber;
        private int kuaidiID;
        private boolean kuaidiStatus;
        private String kuaidiName;
        private String kuaidiPhone;
        private String info;
        private String region;
        private boolean isInvoice;
        private boolean payStatus;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
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

        public boolean isPayStatus() {
            return payStatus;
        }

        public void setPayStatus(boolean payStatus) {
            this.payStatus = payStatus;
        }
    }
}
