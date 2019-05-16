package com.jieniuwuliu.jieniu.messageEvent;

import com.amap.api.services.core.LatLonPoint;

public class AddressEvent {
    private LatLonPoint point;
    private String address;

    public LatLonPoint getPoint() {
        return point;
    }

    public void setPoint(LatLonPoint point) {
        this.point = point;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "AddressEvent{" +
                "point=" + point +
                ", address='" + address + '\'' +
                '}';
    }
}
