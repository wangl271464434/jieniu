package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

    /**
     * id : 1
     * CreatedAt : 2019-04-16T12:21:46Z
     * UpdatedAt : 2019-04-16T12:21:46Z
     * address : 陕西省西安市
     * default : true
     * uid : 1
     */
    private int id;
    private String CreatedAt;
    private String UpdatedAt;
    private String address;
    @SerializedName("default")
    private boolean defaultX;
    private int uid;
    private String name;
    private String phone;
    private double lat;
    private double lng;
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefaultX() {
        return defaultX;
    }

    public void setDefaultX(boolean defaultX) {
        this.defaultX = defaultX;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
