package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;
import java.util.List;

public class StoreCerity implements Serializable {
    private String nickname;//门店名称
    private int personType;//门店类型
    private String shopPhoto;//门店照片
    private String zizhiPhoto;//营业执照
    private String yewu;//主营业务
    private String wechat;//微信
    private String landline;//固定电话
    private String fuwuCars;//服务车型
    private  Address address;//地址
    private int auth;//门店认证

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
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

    public String getFuwuCars() {
        return fuwuCars;
    }

    public void setFuwuCars(String fuwuCars) {
        this.fuwuCars = fuwuCars;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }
}
