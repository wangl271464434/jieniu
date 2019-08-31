package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class XjInfo {
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

    public  class DataBean{
        private String Name;
        private int Pid;
        private int Bid;
        private int State;
        private String UpdatedAt;
        private String Phone;
        private String Address;
        private String Label;
        private String Partslist;

        public int getState() {
            return State;
        }

        public void setState(int state) {
            State = state;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getLabel() {
            return Label;
        }

        public void setLabel(String label) {
            Label = label;
        }

        public int getPid() {
            return Pid;
        }

        public void setPid(int pid) {
            Pid = pid;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getBid() {
            return Bid;
        }

        public void setBid(int bid) {
            Bid = bid;
        }

        public String getUpdatedAt() {
            return UpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            UpdatedAt = updatedAt;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getPartslist() {
            return Partslist;
        }

        public void setPartslist(String partslist) {
            Partslist = partslist;
        }
    }
}
