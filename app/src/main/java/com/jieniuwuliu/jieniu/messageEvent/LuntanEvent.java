package com.jieniuwuliu.jieniu.messageEvent;

import com.jieniuwuliu.jieniu.bean.LunTanResult;

public class LuntanEvent {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "LuntanEvent{" +
                "success=" + success +
                '}';
    }
}
