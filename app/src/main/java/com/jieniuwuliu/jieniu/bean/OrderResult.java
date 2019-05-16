package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class OrderResult {
    /**
     * status : 0
     * msg : 获取订单成功
     * data : [{"id":3,"CreatedAt":"2019-04-28T06:45:13Z","UpdatedAt":"2019-04-28T06:45:13Z","fromUid":41,"fromAddress":"陕西省西安市雁塔区高新大都荟","fromName":"测试门店","fromLng":108.893905,"fromLat":34.237733,"fromPhone":"7567989798","toUid":0,"toAddress":"陕西省西安市户县石井镇石西村","toName":"下午","toLng":108.58623,"toLat":34.039598,"toPhone":"89798998","sendType":2,"weight":"小于10kg","number":3,"couponID":10,"yunfeiMoney":0,"daishouMoney":586,"baojiajine":0,"baojiaMoney":18,"payType":0,"totalMloney":604,"status":0,"orderNumber":"1122391298372800512","kuaidiID":0,"info":"快考试了"},{"id":4,"CreatedAt":"2019-04-28T07:02:59Z","UpdatedAt":"2019-04-28T07:02:59Z","fromUid":41,"fromAddress":"陕西省西安市雁塔区高新大都荟","fromName":"测试门店","fromLng":108.893905,"fromLat":34.237733,"fromPhone":"7567989798","toUid":0,"toAddress":"陕西省西安市长安区郭杜十字","toName":"墨迹无聊","toLng":108.86388,"toLat":34.1601,"toPhone":"67987888","sendType":1,"weight":"小于10kg","number":3,"couponID":0,"yunfeiMoney":25,"daishouMoney":3000,"baojiajine":0,"baojiaMoney":18,"payType":0,"totalMloney":3043,"status":0,"orderNumber":"1122395766657126400","kuaidiID":0,"info":"or你是外星人"}]
     */
    private int status;
    private String msg;
    private List<OrderInfo> data;

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

    public List<OrderInfo> getData() {
        return data;
    }

    public void setData(List<OrderInfo> data) {
        this.data = data;
    }
}
