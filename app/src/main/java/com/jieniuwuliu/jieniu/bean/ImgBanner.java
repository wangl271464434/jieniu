package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class ImgBanner {

    /**
     * status : 0
     * msg : 成功
     * total : 2
     * data : [{"Url":"https://www.jieniuwuliu.com/20190814101230.jpg"},{"Url":"https://www.jieniuwuliu.com/20190814101239.jpg"}]
     */

    private int status;
    private String msg;
    private int total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Url : https://www.jieniuwuliu.com/20190814101230.jpg
         */

        private String Url;

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }
    }
}
