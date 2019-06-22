package com.jieniuwuliu.jieniu.messageEvent;

public class WeChatEvent {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "WeChatEvent{" +
                "code='" + code + '\'' +
                '}';
    }
}
