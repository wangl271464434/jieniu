package com.jieniuwuliu.jieniu.bean;

public class CodeBean {

    /**
     * status : 0
     * msg : 请求成功
     * data : {"errmsg":"OK","ext":"","fee":1,"result":0,"sid":"2019:5061527018230179834"}
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
         * errmsg : OK
         * ext :
         * fee : 1
         * result : 0
         * sid : 2019:5061527018230179834
         */

        private String errmsg;
        private String ext;
        private int fee;
        private int result;
        private String sid;

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
    }
}
