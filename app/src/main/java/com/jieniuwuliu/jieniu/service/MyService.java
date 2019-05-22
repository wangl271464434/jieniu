package com.jieniuwuliu.jieniu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.UserBean;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 定时发送位置的服务
 * */
public class MyService extends Service implements AMapLocationListener {
    private double longitude = 0.0;
    private double latitude = 0.0;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service","service is create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("service","service is start");
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        sleep(60*1000);
                        location();
                        Log.i("service","service send msg");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return START_STICKY;
    }
    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service","service is destroy");
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyService.class);  //销毁时重新启动Service
        this.startService(localIntent);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            longitude = aMapLocation.getLongitude();
            latitude = aMapLocation.getLatitude();
            Log.i("lat+long", "service 经度：" + longitude + "维度：" + latitude);
            int userType = (int) SPUtil.get(this, Constant.USERTYPE, Constant.USERTYPE, 0);
            if (userType == 5){
                updatePSYPosition();
            }
        }
    }
    /**
     * 上报配送员位置
     * */
    private void updatePSYPosition() {
        String token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        Map<String,Object> map = new HashMap();
        map.clear();
        map.put("lat",latitude);
        map.put("lng",longitude);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<UserBean> call =HttpUtil.getInstance().getApi(token).modifyUserInfo(body);
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                Log.i("result","上报的经纬度返回："+response.body().getMsg());
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                Log.i("fail",t.toString());
            }
        });
    }
}
