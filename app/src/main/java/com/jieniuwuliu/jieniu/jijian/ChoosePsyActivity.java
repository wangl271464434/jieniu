package com.jieniuwuliu.jieniu.jijian;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.PSYUser;
import com.jieniuwuliu.jieniu.jijian.adapter.ChoosePsyAdapter;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.WeightEvent;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/***
 * 选择网点
 * */
public class ChoosePsyActivity extends BaseActivity implements OnItemClickListener, AMapLocationListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String token;
    private ChoosePsyAdapter adapter;
    private List<PSYUser.DataBean> list;
    private MyLoading loading;
    public static double lat,lng;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_psy;
    }

    @Override
    protected void init() {
        title.setText("选择网点");
        loading = new MyLoading(this,R.style.CustomDialog);
        list  = new ArrayList<>();
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new ChoosePsyAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        checkSDK();
    }
    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }else{
                location();
            }
        }else{
            location();
        }
    }
    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
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
    /**
     * 获取网点
     * */
    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getWangDianList();
        call.enqueue(new SimpleCallBack<ResponseBody>(ChoosePsyActivity.this) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    switch (response.code()){
                        case 200:
                            PSYUser psyUser = (PSYUser) GsonUtil.praseJsonToModel(response.body().string(),PSYUser.class);
                            list.addAll(psyUser.getData());
                            List<LatLonPoint> points = new ArrayList<>();
                            for (int i =0;i<psyUser.getData().size();i++){
                                PSYUser.DataBean dataBean = psyUser.getData().get(i);
                                points.add(new LatLonPoint(dataBean.getLat(),dataBean.getLng()));
                            }
                            LatLonPoint end = new LatLonPoint(lat,lng);
                            DistanceSearch search = new DistanceSearch(ChoosePsyActivity.this);
                            DistanceSearch.DistanceQuery query = new DistanceSearch.DistanceQuery();
                            query.setOrigins(points);//支持多起点
                            query.setDestination(end);
                            //设置测量方式，支持直线和驾车
                            query.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);//设置为直线
                            search.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
                                @Override
                                public void onDistanceSearched(DistanceResult distanceResult, int i) {
                                    for (int j = 0;j<list.size();j++){
                                        Double distance = Double.valueOf(distanceResult.getDistanceResults().get(j).getDistance());
                                        list.get(j).setDistance(distance);
                                    }
                                    Collections.sort(list);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            search.calculateRouteDistanceAsyn(query);
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            Log.w("result",s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getApplicationContext(), object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    PSYUser psyUser = (PSYUser) GsonUtil.praseJsonToModel(response.body().string(),PSYUser.class);
                    list.addAll(psyUser.getData());
                    List<LatLonPoint> points = new ArrayList<>();
                    for (int i =0;i<psyUser.getData().size();i++){
                        PSYUser.DataBean dataBean = psyUser.getData().get(i);
                        points.add(new LatLonPoint(dataBean.getLat(),dataBean.getLng()));
                    }
                    LatLonPoint end = new LatLonPoint(lat,lng);
                    DistanceSearch search = new DistanceSearch(ChoosePsyActivity.this);
                    DistanceSearch.DistanceQuery query = new DistanceSearch.DistanceQuery();
                    query.setOrigins(points);//支持多起点
                    query.setDestination(end);
                    //设置测量方式，支持直线和驾车
                    query.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);//设置为直线
                    search.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
                        @Override
                        public void onDistanceSearched(DistanceResult distanceResult, int i) {
                            for (int j = 0;j<list.size();j++){
                                Double distance = Double.valueOf(distanceResult.getDistanceResults().get(j).getDistance());
                                list.get(j).setDistance(distance);
                            }
                            Collections.sort(list);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    search.calculateRouteDistanceAsyn(query);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(ChoosePsyActivity.this,s);
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        WeightEvent event = new WeightEvent();
        event.setUser(list.get(position));
        EventBus.getDefault().post(event);
        finish();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                lat =aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                getData();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
}
