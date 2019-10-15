package com.jieniuwuliu.jieniu.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.messageEvent.WeChatEvent;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private String token;
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
                switch (Constant.SHARETYPE){
                    case 1:
                        getCoupon();
                        break;
                    case 2:
                        MyToast.show(getApplicationContext(),"分享成功");
                        break;
                        default:
                            String code = ((SendAuth.Resp) baseResp).code;
                            WeChatEvent event = new WeChatEvent();
                            event.setCode(code);
                            EventBus.getDefault().post(event);
                            break;
                }
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
    private void getCoupon() {
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        Log.i("当前存储的token：",token);
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).putCounpons();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    MyToast.show(getApplicationContext(),"领取成功");
                }else{
                    MyToast.show(getApplicationContext(),"领取失败");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
