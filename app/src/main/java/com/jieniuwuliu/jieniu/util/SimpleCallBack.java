package com.jieniuwuliu.jieniu.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.jieniuwuliu.jieniu.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class SimpleCallBack<T> implements Callback<T> {
    private Activity activity;
    private Intent intent;
    public SimpleCallBack(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        switch (response.code()){
            case 200:
                onSuccess(response);
                break;
            case 400:
                onFail(400,response);
                break;
            case 401:
                Log.i("error",response.toString());
                intent = new Intent();
                intent.setClass(activity,LoginActivity.class);
                intent.putExtra("restart",true);
                activity.startActivity(intent);
                break;

        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("网络连接失败","连接失败原因："+t.toString());
        onNetError("网络连接失败，请检查网络");
    }
    /**
     * 请求成功
     * @param response
     * */
    public abstract void onSuccess(Response<T> response);
    /**
     * 请求失败
     * @param errorCode
     * @param response
     * */
    public abstract void onFail(int errorCode,Response<T> response);
    /**
     * 网络异常
     * @param s
     * */
    public abstract void onNetError(String  s);
}
