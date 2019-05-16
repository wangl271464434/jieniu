package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;

public class DianZan implements Serializable {

    /**
     * id : 1
     * CreatedAt : 2019-04-28T15:44:33Z
     * UpdatedAt : 2019-04-28T15:44:33Z
     * Fid : 20
     * Uid : 41
     * name : 测试门店
     */

    private int id;
    private String CreatedAt;
    private String UpdatedAt;
    private int fid;
    private int uid;
    private String name;

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

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
