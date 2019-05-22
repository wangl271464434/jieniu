package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class Notice {

    /**
     * status : 0
     * msg : 成功
     * data : [{"id":1,"CreatedAt":"1970-01-01 08:00:00","UpdatedAt":"2019-05-22 14:29:42","info":"今日活动就立刻就看"}]
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

    public static class DataBean {
        /**
         * id : 1
         * CreatedAt : 1970-01-01 08:00:00
         * UpdatedAt : 2019-05-22 14:29:42
         * info : 今日活动就立刻就看
         */

        private int id;
        private String CreatedAt;
        private String UpdatedAt;
        private String info;

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

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
