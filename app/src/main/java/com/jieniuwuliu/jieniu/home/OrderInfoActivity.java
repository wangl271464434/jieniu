package com.jieniuwuliu.jieniu.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
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
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.AMapUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.TimeUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.home.adapter.OrderWuLiuAdapter;
import com.jieniuwuliu.jieniu.view.DrivingRouteOverlay;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderInfoActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.rv)
    RecyclerView rv;
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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.info)
    LinearLayout info;
   /* @BindView(R.id.btn_info)
    TextView btnInfo;*/
    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_baojia)
    TextView tvBaojia;
    @BindView(R.id.tv_baojia_money)
    TextView tvBaojiaMoney;
    @BindView(R.id.tv_daishou)
    TextView tvDaiShou;
    @BindView(R.id.tv_yunfei)
    TextView tvYunFei;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.layout_img)
    LinearLayout layoutImg;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private boolean isShow = false;
    private AMap aMap;
    protected Unbinder unbinder;
    private OrderWuLiuAdapter adapter;
    private String token, orderNo;
    private MyLoading loading;
    private OrderInfo orderWuliuInfo;
    private LatLonPoint start, end;
    private Context mContext;
    private List<OrderInfo.OrderListBean> list;
    private RouteSearch.DriveRouteQuery query;
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
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(start))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_niu)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(end))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
    }

    protected void init() {
        list = new ArrayList<>();
        loading = new MyLoading(this, R.style.CustomDialog);
        checkSDK();
        if (aMap == null) {
            aMap = map.getMap();
            UiSettings settings = aMap.getUiSettings();
            settings.setLogoBottomMargin(-50);
            settings.setZoomControlsEnabled(false);
            //设置缩放级别
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        }
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    scrollView.requestDisallowInterceptTouchEvent(true);

                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new OrderWuLiuAdapter(this, list);
        rv.setAdapter(adapter);
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        orderNo = getIntent().getStringExtra("orderNo");
        getOrderInfo();
    }

    /**
     * 获取订单详情
     */
    private void getOrderInfo() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getOrderInfo(orderNo);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    switch (response.code()) {
                        case 200:
                            String json = new JSONObject(response.body().string()).getString("data");
                            orderWuliuInfo = (OrderInfo) GsonUtil.praseJsonToModel(json, OrderInfo.class);
                            tvFahuoName.setText(orderWuliuInfo.getFromName());
                            tvFahuoPhone.setText(orderWuliuInfo.getFromPhone());
                            tvFahuoAddress.setText(orderWuliuInfo.getFromAddress());
                            tvShouhuoName.setText(orderWuliuInfo.getToName());
                            tvShouhuoAddress.setText(orderWuliuInfo.getToAddress());
                            tvShouhuoPhone.setText(orderWuliuInfo.getToPhone());
                            tvInfo.setText(orderWuliuInfo.getInfo()+" x " +orderWuliuInfo.getNumber()+"件");
                            tvBaojia.setText("¥ "+(orderWuliuInfo.getBaojiaMoney()/100));
                            tvBaojiaMoney.setText("¥ "+(orderWuliuInfo.getBaojiajine()/100));
                            tvDaiShou.setText("¥ "+(orderWuliuInfo.getDaishouMoney()/100));
                            tvYunFei.setText("¥ "+(orderWuliuInfo.getYunfeiMoney()/100));
                            tvTotal.setText("¥ "+(orderWuliuInfo.getTotalMoney()/100));
                            //签收照片
                            if (!orderWuliuInfo.getFinishPhoto().equals("")){
                                layoutImg.setVisibility(View.VISIBLE);
                                GlideUtil.setImgUrl(OrderInfoActivity.this,orderWuliuInfo.getFinishPhoto(),R.mipmap.loading,img);
                            }else{
                                layoutImg.setVisibility(View.GONE);
                            }

                            if (orderWuliuInfo.getOrderList()!=null){
                                if (orderWuliuInfo.getOrderList().size()!=0){
                                    if (!orderWuliuInfo.getOrderList().get(0).getMsg().equals("已发货")){
                                        GlideUtil.setUserImgUrl(OrderInfoActivity.this,orderWuliuInfo.getOrderList().get(0).getPhoto(),imgUser);
                                        tvName.setText(orderWuliuInfo.getOrderList().get(0).getName());
                                        tvPhone.setText(orderWuliuInfo.getOrderList().get(0).getPhone());
                                    }
                                    list.addAll(orderWuliuInfo.getOrderList());
                                    adapter.notifyDataSetChanged();
                                }else{
                                    tvState.setText("正在发货中");
                                    tvName.setText("暂无信息");
                                    tvPhone.setText("暂无信息");
                                }
                            }else {
                                tvState.setText("正在发货中");
                                tvName.setText("暂无信息");
                                tvPhone.setText("暂无信息");
                            }
                            setLine();
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getApplicationContext(), object.getString("msg"));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                MyToast.show(getApplicationContext(), "网络请求失败");
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

    @OnClick({R.id.back,R.id.refresh,R.id.tv_call_phone, R.id.btn_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.refresh://刷新
                aMap.clear();
                list.clear();
                getOrderInfo();
                break;
            case R.id.tv_call_phone://联系配送员
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 100);
                        return;
                    }
                }
                String phone = tvPhone.getText().toString();
                if (phone.equals("暂无信息")){
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.btn_info://查看详情
                if (isShow){
                    info.setVisibility(View.GONE);
//                    btnInfo.setText("查看详情");
                    isShow = false;
                }else{
                    info.setVisibility(View.VISIBLE);
//                    btnInfo.setText("隐藏详情");
                    isShow = true;
                }
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
                    long duration = drivePath.getDuration();
                    tvState.setText("正在运输中");
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText("预计"+TimeUtil.formatDateTime(duration)+"后送达");
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
}
