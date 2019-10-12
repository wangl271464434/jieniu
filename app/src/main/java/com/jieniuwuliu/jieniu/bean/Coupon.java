package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠券
 * */
public class Coupon implements Serializable {

    /**
     * status : 0
     * msg : 获取所有优惠券成功
     * data : [{"id":2,"CreatedAt":"2019-04-16T12:21:46Z","UpdatedAt":"2019-04-16T12:21:46Z","uid":1,"useMoney":-1,"couponTime":"2019-05-16 20:21:46","money":0,"use":false},{"id":4,"CreatedAt":"2019-04-18T16:49:46Z","UpdatedAt":"2019-04-18T16:49:46Z","uid":1,"useMoney":600,"couponTime":"2019-06-18 16:49:46","money":500,"use":false}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 2
         * CreatedAt : 2019-04-16T12:21:46Z
         * UpdatedAt : 2019-04-16T12:21:46Z
         * uid : 1
         * useMoney : -1
         * couponTime : 2019-05-16 20:21:46
         * money : 0
         * use : false
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private int uid;
        private int useMoney;
        private String couponTime;
        private int money;
        private int st;//领取状态
        private boolean use;//是否使用
        private boolean expried = false;//是否过期

        public int getSt() {
            return st;
        }

        public void setSt(int st) {
            this.st = st;
        }

        public boolean isExpried() {
            return expried;
        }

        public void setExpried(boolean expried) {
            this.expried = expried;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getUseMoney() {
            return useMoney;
        }

        public void setUseMoney(int useMoney) {
            this.useMoney = useMoney;
        }

        public String getCouponTime() {
            return couponTime;
        }

        public void setCouponTime(String couponTime) {
            this.couponTime = couponTime;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public boolean isUse() {
            return use;
        }

        public void setUse(boolean use) {
            this.use = use;
        }
    }
}
