package com.jieniuwuliu.jieniu.Util;

import android.content.Context;

import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.WxPayInfo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayUtil {
    static IWXAPI api;
    public static void pay(Context context, WxPayInfo.DataBean info){
        api = WXAPIFactory.createWXAPI(context, Constant.WXAPPID);
        // 将该app注册到微信
        if(!api.isWXAppInstalled()){
            MyToast.show(context,"请您先安装微信客户端！");
            return;
        }
        api.registerApp(Constant.WXAPPID);
        PayReq request = new PayReq();
        request.appId = info.getAppid();
        request.partnerId = info.getPartnerid();
        request.prepayId= info.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr= info.getNoncestr();
        request.timeStamp= info.getTimestamp();
        request.sign= info.getSign();
        api.sendReq(request);
    }
}
