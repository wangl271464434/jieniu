package com.jieniuwuliu.jieniu.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class AliPayUtil {
    public static String pay(String privateKey,String info){
        String sign = getSign(info, privateKey);
        final String orderInfo = info + "&" + sign;
        return orderInfo;
    }
    /**
     * 对支付参数信息进行签名
     *
     * @param info
     *            待签名授权信息
     *
     * @return
     */
    public static String getSign(String info, String rsaKey) {
        String oriSign = SignUtils.sign(info, rsaKey);
        String encodedSign = "";
        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }
}
