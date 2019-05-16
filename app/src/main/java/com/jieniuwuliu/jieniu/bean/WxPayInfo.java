package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

public class WxPayInfo {
    /**
     * code : 0
     * data : {"appid":"wx9fa450463b6dd4c9","noncestr":"7bbb1269156342239ae166411def500e","package":"Sign=WXPay","partnerid":"1510774741","prepayid":"wx11103422133188c5c9ce11803822549256","sign":"C0BF35C53B5E400D55F739DDC69B9885","timestamp":"1533954953"}
     */

    private String status;
    private DataBean data;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * appid : wx9fa450463b6dd4c9
         * noncestr : 7bbb1269156342239ae166411def500e
         * package : Sign=WXPay
         * partnerid : 1510774741
         * prepayid : wx11103422133188c5c9ce11803822549256
         * sign : C0BF35C53B5E400D55F739DDC69B9885
         * timestamp : 1533954953
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
