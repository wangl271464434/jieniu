package com.jieniuwuliu.jieniu.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneStateReceiver extends BroadcastReceiver {
    private Context context;
    private boolean isFirst = false;
    private String token;
    private String TAG = "PhoneStateReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        token = (String) SPUtil.get(context,Constant.TOKEN,Constant.TOKEN,"");
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){//去电
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }else{
            //来电
        }
    }
    PhoneStateListener listener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE://空闲
                    Log.i(TAG,"监听："+Constant.isCall);
                    if (isFirst){
                        if (!Constant.isCall){
                            Constant.isCall = true;
                            uploadPhone(Constant.CALLPHONE);
                            Log.i(TAG,"监听："+Constant.isCall);
                        }
                        isFirst = false;
                    }else{
                        isFirst = true;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接通
                    break;
                case TelephonyManager.CALL_STATE_RINGING://来电响铃中
                    System.out.println("响铃:来电号码"+phoneNumber);
                        break;
                }
            }
        };

        private void uploadPhone(String callphone) {
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).callPhone(callphone);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Constant.CALLPHONE = "";
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
    }
}
