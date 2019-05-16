package com.jieniuwuliu.jieniu.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressList {

    /**
     * status : 0
     * msg : 领取优惠券成功
     * data : [{"id":1,"CreatedAt":"2019-04-16T12:21:46Z","UpdatedAt":"2019-04-16T12:21:46Z","address":"陕西省西安市","default":true,"uid":1}]
     */

    private int status;
    private String msg;
    private List<Address> data;

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

    public List<Address> getData() {
        return data;
    }

    public void setData(List<Address> data) {
        this.data = data;
    }
}
