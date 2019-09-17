package com.jieniuwuliu.jieniu.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliPayUtil {
    /**
     * 支付宝公共参数
     * */
    public static Map<String, String> buildOrderParamMap(String appId, String title,String price, String orderNo, String notifyUrl) {
        Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put("app_id", appId);
        keyValues.put("biz_content", getContent(title,price,orderNo));
        keyValues.put("charset", "utf-8");
        keyValues.put("format", "JSON");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("sign_type","RSA2");
        keyValues.put("timestamp", TimeUtil.getCurrentTime());
        keyValues.put("notify_url",notifyUrl);
        keyValues.put("version", "1.0");
        return keyValues;
    }
    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }
    /**
     * 支付宝业务参数
     * */
    public static String getContent(String title,String price,String orderNo){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("out_trade_no",orderNo);
        map.put("timeout_express","30m");
        map.put("total_amount",price);
        map.put("subject",title);
        map.put("product_code","QUICK_MSECURITY_PAY");
        return GsonUtil.mapToJson(map);
    }
    public static String pay(String privateKey,Map<String, String> map){
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), privateKey);
        String encodedSign = "";
        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }
    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
}
