package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoreInfoBean implements Serializable{
    private int uid;
    private String CreatedAt;
    private String UpdatedAt;
    private int auth;
    private int personType;
    private int partscity;
    private String nickname;
    private Address address;
    private String shopPhoto;
    private String zizhiPhoto;
    private String fuwuCars;
    private String yewu;
    private String wechat;
    private String photos;
    private String landline;
    private String storeinform;
    private String unionid;
    private String wxName;
    private boolean isFollow;
    private List<Car> fuwuCar;

    public int getPartscity() {
        return partscity;
    }

    public void setPartscity(int partscity) {
        this.partscity = partscity;
    }

    public String getStoreinform() {
        return storeinform;
    }

    public void setStoreinform(String storeinform) {
        this.storeinform = storeinform;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public String getZizhiPhoto() {
        return zizhiPhoto;
    }

    public void setZizhiPhoto(String zizhiPhoto) {
        this.zizhiPhoto = zizhiPhoto;
    }

    public String getFuwuCars() {
        return fuwuCars;
    }

    public void setFuwuCars(String fuwuCars) {
        this.fuwuCars = fuwuCars;
    }

    public String getYewu() {
        return yewu;
    }

    public void setYewu(String yewu) {
        this.yewu = yewu;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public List<Car> getFuwuCar() {
        return fuwuCar;
    }

    public void setFuwuCar(List<Car> fuwuCar) {
        this.fuwuCar = fuwuCar;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }
}
