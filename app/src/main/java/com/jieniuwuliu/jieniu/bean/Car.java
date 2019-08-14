package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;

public class Car implements Serializable {
    private String name;
    private String zimu;//显示拼音的首字母
    private String tupian;//图片地址
    private String CreatedAt;
    private String UpdatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZimu() {
        return zimu;
    }

    public void setZimu(String zimu) {
        this.zimu = zimu;
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian;
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
}
