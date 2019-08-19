package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class BJOrder implements Serializable{
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

    public static class DataBean implements Serializable {
        private int ID;
        private String CreatedAt;
        private String Partsphoto;
        private String Remarks;
        private String Logos;
        private String Cartype;
        private String Carbrand;
        private String gode;
        private int Stype;
        private int Pcount;
        private String Partslist;

        public String getGode() {
            return gode;
        }

        public void setGode(String gode) {
            this.gode = gode;
        }

        public int getPcount() {
            return Pcount;
        }

        public void setPcount(int pcount) {
            Pcount = pcount;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getCreatedAt() {
            return CreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            CreatedAt = createdAt;
        }

        public String getPartsphoto() {
            return Partsphoto;
        }

        public void setPartsphoto(String partsphoto) {
            Partsphoto = partsphoto;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String remarks) {
            Remarks = remarks;
        }

        public String getLogos() {
            return Logos;
        }

        public void setLogos(String logos) {
            Logos = logos;
        }

        public String getCartype() {
            return Cartype;
        }

        public void setCartype(String cartype) {
            Cartype = cartype;
        }

        public String getCarbrand() {
            return Carbrand;
        }

        public void setCarbrand(String carbrand) {
            Carbrand = carbrand;
        }

        public int getStype() {
            return Stype;
        }

        public void setStype(int stype) {
            Stype = stype;
        }

        public String getPartslist() {
            return Partslist;
        }

        public void setPartslist(String partslist) {
            Partslist = partslist;
        }
    }
}
