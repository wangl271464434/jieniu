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
        private int id;
        private String createdat;
        private String partsphoto;
        private String remarks;
        private String logos;
        private String cartype;
        private String carbrand;
        private String gode ="";
        private int stype;
        private int pcount;
        private String partslist;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreatedat() {
            return createdat;
        }

        public void setCreatedat(String createdat) {
            this.createdat = createdat;
        }

        public String getPartsphoto() {
            return partsphoto;
        }

        public void setPartsphoto(String partsphoto) {
            this.partsphoto = partsphoto;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getLogos() {
            return logos;
        }

        public void setLogos(String logos) {
            this.logos = logos;
        }

        public String getCartype() {
            return cartype;
        }

        public void setCartype(String cartype) {
            this.cartype = cartype;
        }

        public String getCarbrand() {
            return carbrand;
        }

        public void setCarbrand(String carbrand) {
            this.carbrand = carbrand;
        }

        public String getGode() {
            return gode;
        }

        public void setGode(String gode) {
            this.gode = gode;
        }

        public int getStype() {
            return stype;
        }

        public void setStype(int stype) {
            this.stype = stype;
        }

        public int getPcount() {
            return pcount;
        }

        public void setPcount(int pcount) {
            this.pcount = pcount;
        }

        public String getPartslist() {
            return partslist;
        }

        public void setPartslist(String partslist) {
            this.partslist = partslist;
        }
    }
}
