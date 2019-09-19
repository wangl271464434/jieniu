package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class QPType {

    /**
     * status : 0
     * msg : 成功
     * data : [{"id":1,"nickname":"欢乐港汽配城"},{"id":2,"nickname":"海纳汽配城"},{"id":3,"nickname":"玉林汽配城"},{"id":0,"nickname":"其他汽配城"}]
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
         * nickname : 欢乐港汽配城
         */

        private int id;
        private String nickname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
