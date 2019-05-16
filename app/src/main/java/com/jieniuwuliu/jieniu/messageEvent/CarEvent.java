package com.jieniuwuliu.jieniu.messageEvent;

import com.jieniuwuliu.jieniu.bean.SortModel;
import com.jieniuwuliu.jieniu.bean.WorkType;

import java.util.List;

public class CarEvent {
    private List<SortModel> sortModelList;
    private List<WorkType> list;
    private String type;

    public List<SortModel> getSortModelList() {
        return sortModelList;
    }

    public void setSortModelList(List<SortModel> sortModelList) {
        this.sortModelList = sortModelList;
    }

    public List<WorkType> getList() {
        return list;
    }

    public void setList(List<WorkType> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
