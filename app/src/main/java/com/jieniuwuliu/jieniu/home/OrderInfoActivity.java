package com.jieniuwuliu.jieniu.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.AMapUtil;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.util.TimeUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.home.adapter.OrderWuLiuAdapter;
import com.jieniuwuliu.jieniu.view.DrivingRouteOverlay;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Response;
@RequiresApi(api = Build.VERSION_CODES.M)
public class OrderInfoActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener, WeatherSearch.OnWeatherSearchListener, AMap.InfoWindowAdapter {
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.gifView)
    GifImageView gifView;
    @BindView(R.id.layout_gif)
    LinearLayout layoutGif;
    @BindView(R.id.tv_circle_refresh)
    TextView tvCircleRefresh;
    @BindView(R.id.layout_refresh)
    RelativeLayout layoutRefresh;
    private AMap aMap;
    protected Unbinder unbinder;
    private OrderWuLiuAdapter adapter;
    private String token, orderNo,timeStr,state;
    private MyLoading loading;
    private OrderInfo orderWuliuInfo;
    private LatLonPoint start, end;
    private Context mContext;
    private List<OrderInfo.OrderListBean> list;
    private RouteSearch.DriveRouteQuery query;
    private Intent intent;
    private Marker startMarker;
    private UiSettings settings;
    private CountDownTimer timer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        mContext = OrderInfoActivity.this;
        unbinder = ButterKnife.bind(this);
        map.onCreate(savedInstanceState);
        init();
    }

    /**
     * 设置起点和终点的显示
     */
    private void setfromandtoMarker() {
       startMarker =  aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(start))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_order_car)));
       startMarker.showInfoWindow();
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(end))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
    }
    @SuppressLint("SetTextI18n")
    protected void init() {
        tvCircleRefresh.setText("15");
        timer = new CountDownTimer(15*1000,1000) {
            @Override
            public void onTick(long l) {
                tvCircleRefresh.setText(""+(l/1000));
            }
            @Override
            public void onFinish() {
                timer.cancel();
                tvCircleRefresh.setText("15");
                getOrderInfo();
        }
        };
        list = new ArrayList<>();
        loading = new MyLoading(this, R.style.CustomDialog);
        checkSDK();
        if (aMap == null) {
            aMap = map.getMap();
            settings = aMap.getUiSettings();
            settings.setLogoBottomMargin(-50);
            //设置缩放级别
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            aMap.showIndoorMap(true);//开启室内地图
            aMap.setInfoWindowAdapter(this);
        }
    getWeather();
    token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
    orderNo = getIntent().getStringExtra("orderNo");
}
    /**
     * 获取天气信息
     * */
    private void getWeather() {
        WeatherSearchQuery query = new WeatherSearchQuery(Constant.CITY, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch search = new WeatherSearch(this);
        search.setOnWeatherSearchListener(this);
        search.setQuery(query);
        search.searchWeatherAsyn();
    }

    /**
     * 获取订单详情
     */
    private void getOrderInfo() {
        loading.show();
        aMap.clear();
        list.clear();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getOrderInfo(orderNo);
        call.enqueue(new SimpleCallBack<ResponseBody>(OrderInfoActivity.this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                timer.start();
                try {
                    String json = new JSONObject(response.body().string()).getString("data");
                    orderWuliuInfo = (OrderInfo) GsonUtil.praseJsonToModel(json, OrderInfo.class);
                    if (orderWuliuInfo.getPtime() != null){
                        if (Constant.DEFAULTTIME.equals(orderWuliuInfo.getPtime())){
                            state = "正在发货中";
                            timeStr = "等待配送员配送";
                        }else{
                            long  a = TimeUtil.getMiliSecond(orderWuliuInfo.getPtime());
                            for (int i = 0;i<orderWuliuInfo.getOrderList().size();i++){
                                if ("已签收".equals(orderWuliuInfo.getOrderList().get(i).getMsg())){
                                    long time = TimeUtil.getMiliSecond(orderWuliuInfo.getOrderList().get(i).getCreatedAt()) - a;
                                    String s = TimeUtil.formatDateTime(time/1000) ;
                                    state = "已完成";
                                    timeStr = "共耗时"+s+"完成";
                                    break;
                                }else{
                                    state = "正在配送中";
                                    long b = a - System.currentTimeMillis();
                                    if (b>0){
                                        timeStr = "预计"+TimeUtil.formatDateTime(b/1000)+"后到达";
                                    }else{
                                        timeStr = "已超时"+TimeUtil.formatDateTime(Math.abs(b/1000));
                                    }
                                }
                            }
                        }
                    }
                    setLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
            }
        });
    }

    /**
     * 设置路线
     */
    private void setLine() {
        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        if (orderWuliuInfo.getOrderList()!=null){
            start = new LatLonPoint(orderWuliuInfo.getOrderList().get(0).getLat(), orderWuliuInfo.getOrderList().get(0).getLng());
        }else {
            start = new LatLonPoint(orderWuliuInfo.getFromLat(), orderWuliuInfo.getFromLng());
        }
        end = new LatLonPoint(orderWuliuInfo.getToLat(), orderWuliuInfo.getToLng());
        setfromandtoMarker();
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start, end);
        query = new RouteSearch.DriveRouteQuery(fromAndTo,
                RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }
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
        getOrderInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        unbinder.unbind();
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

    @OnClick({R.id.layout_back,R.id.tv_share,R.id.layout_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.tv_share:
                MyToast.show(this,"该功能正在开发");
                break;
            case R.id.layout_refresh://刷新
                getOrderInfo();
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
                            mContext,
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
                 /*   aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(start.getLatitude(), start.getLongitude()),
                            new LatLng(end.getLatitude(), end.getLongitude())), 50));*/
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(start.getLatitude(), start.getLongitude())));
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
    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
        if (i == 1000){
            if (localWeatherLiveResult!=null){
              LocalWeatherLive weatherLive = localWeatherLiveResult.getLiveResult();
              if (weatherLive.getWeather().equals("冰雹")){
                  layoutGif.setVisibility(View.VISIBLE);
                  gifView.setImageResource(R.drawable.xue);
              }else if (weatherLive.getWeather().equals("雨夹雪")){
                  layoutGif.setVisibility(View.VISIBLE);
                  gifView.setImageResource(R.drawable.xue);
              }else if (weatherLive.getWeather().contains("雨")){
                  layoutGif.setVisibility(View.VISIBLE);
                  gifView.setImageResource(R.drawable.yu);
              }else if (weatherLive.getWeather().contains("雪")){
                  layoutGif.setVisibility(View.VISIBLE);
                  gifView.setImageResource(R.drawable.xue);
              }
            }else{
                Log.i("weather","未查询到当前城市天气");
                layoutGif.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(R.layout.info_window,null);
        ImageView img = infoWindow.findViewById(R.id.img);
        TextView tvName = infoWindow.findViewById(R.id.tv_name);
        TextView tvInfo = infoWindow.findViewById(R.id.tv_info);
        TextView tvState = infoWindow.findViewById(R.id.tv_state);
        TextView tvTime = infoWindow.findViewById(R.id.tv_time);
        TextView tvOrderInfo = infoWindow.findViewById(R.id.tv_order_info);
        TextView tvWuLiu = infoWindow.findViewById(R.id.tv_wuliu);
        tvTime.setText(timeStr);
        tvState.setText(state);
        if (orderWuliuInfo.getOrderList().size()>0){
            tvName.setText("配送员："+orderWuliuInfo.getOrderList().get(0).getName());
            tvInfo.setText(orderWuliuInfo.getOrderList().get(0).getInfo());
            GlideUtil.setUserImgUrl(OrderInfoActivity.this,orderWuliuInfo.getOrderList().get(0).getPhoto(),img);
        }else{
            tvName.setText("暂无配送员");
        }
        //跳转到详情
        tvOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(OrderInfoActivity.this,OrderDescActivity.class);
                intent.putExtra("order",orderWuliuInfo);
                startActivity(intent);
            }
        });
        //查看物流信息
        tvWuLiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderWuliuInfo.getOrderList().size()>0){
                    showWuLiu();
                }else{
                    MyToast.show(getApplicationContext(),"暂无物流轨迹");
                }
            }
        });
        return infoWindow;
    }
    /**显示物流*/
    private void showWuLiu() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setBackgroundDrawableResource(R.drawable.bg_white_shape);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.8);
        window.setAttributes(params);
        dialog.setContentView(R.layout.dialog_wuliu);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout close = dialog.findViewById(R.id.layout_close);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        list.addAll(orderWuliuInfo.getOrderList());
        adapter = new OrderWuLiuAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
