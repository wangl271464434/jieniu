package com.jieniuwuliu.jieniu.messageEvent;

import com.jieniuwuliu.jieniu.bean.ContactInfo;
import com.jieniuwuliu.jieniu.bean.Coupon;

public class WeightEvent {
    private String type = "";
    private String info = "";
    private int num = 0;
    private ContactInfo contactInfo;

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
