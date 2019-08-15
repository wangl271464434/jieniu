package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class XJOrder implements Serializable {

    /**
     * status : 0
     * msg : 成功
     * total : 2
     * data : [{"id":20,"uid":21,"createdat":"2019-06-14 11:38:46","partsphoto":"你给我发什么我给你返回什么","remarks":"七夕节快了","logos":"https://car2.autoimg.cn/cardfs/series/g26/M0B/AE/B3/100x100_f40_autohomecar__wKgHEVs9u5WAV441AAAKdxZGE4U148.png","cartype":"奥迪","carbrand":"奥迪 2017款","partslist":[{"name":"前杠 ","types ":"原厂件 ","money ":"0 "}],"stype":1},{"id":22,"uid":21,"createdat":"2019-06-14 11:38:46","partsphoto":"你给我发什么我给你返回什么","remarks":"七夕节快了","logos":"https://car2.autoimg.cn/cardfs/series/g26/M0B/AE/B3/100x100_f40_autohomecar__wKgHEVs9u5WAV441AAAKdxZGE4U148.png","cartype":"奥迪","carbrand":"奥迪 2017款","partslist":[{"name ":"前杠 ","types ":"原厂件 ","money ":"0"}],"stype":1}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 20
         * uid : 21
         * createdat : 2019-06-14 11:38:46
         * partsphoto : 你给我发什么我给你返回什么
         * remarks : 七夕节快了
         * logos : https://car2.autoimg.cn/cardfs/series/g26/M0B/AE/B3/100x100_f40_autohomecar__wKgHEVs9u5WAV441AAAKdxZGE4U148.png
         * cartype : 奥迪
         * carbrand : 奥迪 2017款
         * partslist : [{"name":"前杠 ","types ":"原厂件 ","money ":"0 "}]
         * stype : 1
         */

        private int id;
        private int uid;
        private String createdat;
        private String partsphoto;
        private String remarks;
        private String logos;
        private String cartype;
        private String carbrand;
        private int stype;
        private int count;
        private String partslist;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public int getStype() {
            return stype;
        }

        public void setStype(int stype) {
            this.stype = stype;
        }

        public String getPartslist() {
            return partslist;
        }

        public void setPartslist(String partslist) {
            this.partslist = partslist;
        }
    }
}
