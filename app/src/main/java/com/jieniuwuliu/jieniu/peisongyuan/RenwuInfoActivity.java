package com.jieniuwuliu.jieniu.peisongyuan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.jieniuwuliu.jieniu.ForgetPwdActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.AMapUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.JwtUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.PrintUtil;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LoginBean;
import com.jieniuwuliu.jieniu.bean.PSOrderInfo;
import com.jieniuwuliu.jieniu.messageEvent.MessageEvent;
import com.jieniuwuliu.jieniu.messageEvent.PSYEvent;
import com.jieniuwuliu.jieniu.view.DrivingRouteOverlay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import HPRTAndroidSDKA300.HPRTPrinterHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RenwuInfoActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {

    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.tv_fahuo_name)
    TextView tvFahuoName;
    @BindView(R.id.tv_fahuo_address)
    TextView tvFahuoAddress;
    @BindView(R.id.tv_fahuo_phone)
    TextView tvFahuoPhone;
    @BindView(R.id.tv_fahuo_contact)
    TextView tvFahuoContact;
    @BindView(R.id.tv_shouhuo_name)
    TextView tvShouhuoName;
    @BindView(R.id.tv_shouhuo_address)
    TextView tvShouhuoAddress;
    @BindView(R.id.tv_shouhuo_phone)
    TextView tvShouhuoPhone;
    @BindView(R.id.tv_shouhuo_contact)
    TextView tvShouhuoContact;
    @BindView(R.id.layout_jiedan)
    LinearLayout layoutJiedan;
    @BindView(R.id.layout_gaipai)
    LinearLayout layoutGaipai;
    protected Unbinder unbinder;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_gaipai)
    TextView tvGaipai;
    private AMap aMap;
    private double longitude;
    private double latitude;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    private PSOrderInfo.DataBean data;
    private LatLonPoint start, end;
    private RouteSearch.DriveRouteQuery query;
    private String type ,token;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renwu_info);
        unbinder = ButterKnife.bind(this);
        map.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = map.getMap();
            UiSettings settings = aMap.getUiSettings();
            settings.setLogoBottomMargin(-50);
            settings.setZoomControlsEnabled(false);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
//                   setUpMap();
        }
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        data = (PSOrderInfo.DataBean) getIntent().getSerializableExtra("data");
        type = getIntent().getStringExtra("type");
        tvFahuoName.setText(data.getFromName());
        tvFahuoPhone.setText(data.getFromPhone());
        tvFahuoAddress.setText(data.getFromAddress());
        tvShouhuoName.setText(data.getToName());
        tvShouhuoAddress.setText(data.getToAddress());
        tvShouhuoPhone.setText(data.getToPhone());
        switch (type) {
            case "daijie":
                tvState.setText("接单");
                tvGaipai.setText("拒接");
                layoutJiedan.setEnabled(true);
                layoutGaipai.setEnabled(true);
                break;
            case "paisong":
                tvState.setText("已接单");
                tvGaipai.setText("改派");
                layoutJiedan.setEnabled(false);
                layoutGaipai.setEnabled(true);
                break;
            case "wancheng":
                tvState.setText("已完成");
                tvGaipai.setText("改派");
                layoutJiedan.setEnabled(false);
                layoutGaipai.setEnabled(false);
                break;
        }
        checkSDK();
    }
    /**
     * 改派
     * */
    private void gaipai(int id) {
        try {
            JSONObject object = new JSONObject();
            object.put("kuaidiStatus",false);
            object.put("kuaidiID",id);
            String json = object.toString();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).updateOrder(data.getOrderNumber(),body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        switch (response.code()){
                            case 200:
                                MyToast.show(getApplicationContext(), "改派成功");
                                finish();
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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    MyToast.show(getApplicationContext(), "网络错误，请重试");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置路线
     */
    private void setLine() {
        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        start = new LatLonPoint(data.getFromLat(), data.getFromLng());
        end = new LatLonPoint(data.getToLat(), data.getToLng());
        setfromandtoMarker();
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start, end);
        query = new RouteSearch.DriveRouteQuery(fromAndTo,
                RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    /**
     * 设置起点和终点的显示
     */
    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(start))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(end))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
    }

    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }
        setLine();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back, R.id.tv_dayin, R.id.layout_jiedan, R.id.layout_gaipai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_dayin:
                if (tvState.getText().toString().equals("已接单")){//已接单的订单才可以打印
                    intent = new Intent();
                    intent.setClass(this,PrintActivity.class);
                    intent.putExtra("data",data);
                    startActivity(intent);
                }else{
                    MyToast.show(getApplicationContext(),"接单后才可以打印订单");
                }

                break;
            case R.id.layout_jiedan:
                update();
                break;
            case R.id.layout_gaipai:
                if (tvGaipai.getText().toString().equals("拒接")){
                    refuseOrder();
                }else{
                    intent = new Intent();
                    intent.setClass(this,PSYListActivity.class);
                    startActivityForResult(intent,100);
                }
                break;
        }
    }

    /**
     * 拒接单
     * */
    private void refuseOrder() {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).reFuseOrder(data.getOrderNumber());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    switch (response.code()){
                        case 200:
                            finish();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error",t.toString());
            }
        });
    }

    /**
     * 接单
     * */
    private void update() {
        try {
            JSONObject object = new JSONObject();
            object.put("kuaidiStatus",true);
            object.put("kuaidiID",Integer.valueOf(JwtUtil.JWTParse(token)));
            String json = object.toString();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).updateOrder(data.getOrderNumber(),body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        switch (response.code()){
                            case 200:
                                MyToast.show(getApplicationContext(), "接单成功");
                                tvState.setText("已接单");
                                layoutJiedan.setEnabled(false);
                                layoutGaipai.setEnabled(true);
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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    MyToast.show(getApplicationContext(), "网络错误，请重试");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
//                    location();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "ACCESS_FINE_LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    DrivePath drivePath = driveRouteResult.getPaths().get(0);
                    if (drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            RenwuInfoActivity.this,
                            aMap,
                            drivePath,
                            driveRouteResult.getStartPos(),
                            driveRouteResult.getTargetPos(),
                            null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();

                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(start.getLatitude(), start.getLongitude())));
                    aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(start.getLatitude(), start.getLongitude()),
                            new LatLng(end.getLatitude(), end.getLongitude())), 50));
                }
            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                int id = data.getIntExtra("id",0);
                gaipai(id);
                break;
        }
    }
}
