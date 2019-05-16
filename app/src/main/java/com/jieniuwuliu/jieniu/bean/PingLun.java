package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;

public class PingLun implements Serializable {

    /**
     * id : 1
     * CreatedAt : 2019-04-28T17:45:35Z
     * UpdatedAt : 2019-04-28T17:45:35Z
     * Fid : 20
     * Uid : 41
     * Name : 测试门店
     * info : 测试发评论
     * Photo : http://jieniu-1254151230.cos.ap-beijing.myqcloud.com/img/Picture_06.jpg
     */

    private int id;
    private String CreatedAt;
    private String UpdatedAt;
    private int fid;
    private int uid;
    private int ruid;
    private String rname;
    private String name;
    private String info;
    private String Photo;

    public int getRuid() {
        return ruid;
    }

    public void setRuid(int ruid) {
        this.ruid = ruid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }
}
