package com.jieniuwuliu.jieniu.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.messageEvent.WeChatEvent;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this,Constant.WXAPPID,false);
        api.handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                WeChatEvent event = new WeChatEvent();
                event.setCode(code);
                EventBus.getDefault().post(event);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                MyToast.show(getApplicationContext(),"取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝
                MyToast.show(getApplicationContext(),"拒绝");
                break;
            default:
                MyToast.show(getApplicationContext(),"返回");
                break;
        }
        finish();
    }
}
