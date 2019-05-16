package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;

public class Car implements Serializable {
    private String name;
    private String zimu;//显示拼音的首字母
    private String tupian;//图片地址
    private String created_at;
    private String updated_at;

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
