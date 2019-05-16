package com.jieniuwuliu.jieniu.bean;

import java.io.Serializable;

public class WorkType implements Serializable {
    private String name;
    private boolean isChecked = false;

    public WorkType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
