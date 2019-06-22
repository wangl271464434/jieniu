package com.jieniuwuliu.jieniu.bean;

import com.amap.api.services.core.PoiItem;

public class AddressItem {
   private PoiItem poiItem;
   private boolean isChecked;

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
