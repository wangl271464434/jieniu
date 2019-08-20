package com.jieniuwuliu.jieniu.bean;

public class Version {
    private int status;
    private String msg;
    private Data data;
    public class Data{
        private String total;
        private String infom;
        private String version;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getInfom() {
            return infom;
        }

        public void setInfom(String infom) {
            this.infom = infom;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

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
}
