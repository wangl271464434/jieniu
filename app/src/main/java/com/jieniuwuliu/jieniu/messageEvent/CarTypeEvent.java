package com.jieniuwuliu.jieniu.messageEvent;

public class CarTypeEvent {
    private String name = "";
    private int type;//用户类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CarTypeEvent{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
