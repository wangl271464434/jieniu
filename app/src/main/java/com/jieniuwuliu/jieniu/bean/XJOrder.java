package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class XJOrder {

    /**
     * ID : 12
     * CreatedAt : 2019-06-14 11:38:46
     * Cartype : 雪佛兰 创酷 2014款 1.4T SX AT
     * Logos : //car2.autoimg.cn/cardfs/series/g29/M03/AF/A2/100x100_f40_autohomecar__wKgHJFs9uFKAb5uSAAAhD-fryHg510.png
     * Stype : 1
     * Carlist : [{"ID":1,"Partstype":2,"Partsnum":"前杠"}]
     */

    private int ID;
    private String CreatedAt;
    private String Cartype;
    private String Logos;
    private int Stype;
    private List<CarlistBean> Carlist;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    public String getCartype() {
        return Cartype;
    }

    public void setCartype(String Cartype) {
        this.Cartype = Cartype;
    }

    public String getLogos() {
        return Logos;
    }

    public void setLogos(String Logos) {
        this.Logos = Logos;
    }

    public int getStype() {
        return Stype;
    }

    public void setStype(int Stype) {
        this.Stype = Stype;
    }

    public List<CarlistBean> getCarlist() {
        return Carlist;
    }

    public void setCarlist(List<CarlistBean> Carlist) {
        this.Carlist = Carlist;
    }

    public static class CarlistBean {
        /**
         * ID : 1
         * Partstype : 2
         * Partsnum : 前杠
         */

        private int ID;
        private int Partstype;
        private String Partsnum;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getPartstype() {
            return Partstype;
        }

        public void setPartstype(int Partstype) {
            this.Partstype = Partstype;
        }

        public String getPartsnum() {
            return Partsnum;
        }

        public void setPartsnum(String Partsnum) {
            this.Partsnum = Partsnum;
        }
    }
}
