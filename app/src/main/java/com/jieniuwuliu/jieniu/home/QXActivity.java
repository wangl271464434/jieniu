package com.jieniuwuliu.jieniu.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.adapter.ListAdapter;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.PSYUser;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.home.adapter.QXAdapter;
import com.jieniuwuliu.jieniu.jijian.ChoosePsyActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QXActivity extends BaseActivity implements AMapLocationListener, OnItemClickListener, OnRefreshListener, OnLoadMoreListener, QXAdapter.CallBack {

    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_yewu)
    TextView tvYewu;
    @BindView(R.id.right)
    TextView right;
    private QXAdapter adapter;
    private List<StoreBean.DataBean> list;
    private String token;
    private int page = 1;//页数
    private int num = 10;//条数
    private String yewu = "",distance = "20000", region = "";
    public static double longitude = 0.0;
    public static double currentLng = 0.0;
    public static double latitude = 0.0;
    public static double currentLat = 0.0;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    private Intent intent;
    private String province = "陕西省",city = "西安市",district = "雁塔区";
    private List<String> distList,yewuList;
    private MyLoading loading;
    private boolean isSort = true;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qx;
    }

    @Override
    protected void init() {
        title.setText("捷牛快修");
        loading = new MyLoading(this,R.style.CustomDialog);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new QXAdapter(this,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setCallBack(this);
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
    @OnClick({R.id.back,R.id.tv_all_area, R.id.tv_area, R.id.tv_yewu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_all_area:
                district = "";
                distance = "";
                latitude = 0.0 ;
                longitude = 0.0;
                region = "";
                tvArea.setText("区域");
                list.clear();
                isSort = false;
                getData();
                break;
            case R.id.tv_area:
                cityDialog();
                break;
            case R.id.tv_yewu:
                yewuDialog();
                break;
        }
    }
    /***
     * 业务谈框
     * */
    private void yewuDialog() {
        yewuList = new ArrayList<>();
        yewuList.add("所有业务");
        yewuList.add("钣金烤漆");
        yewuList.add("机修电路");
        yewuList.add("保险理赔");
        yewuList.add("救援服务");
        yewuList.add("装潢美容");
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setContentView(R.layout.dialog_list);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv);
        ListAdapter adapter = new ListAdapter(this,yewuList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                tvYewu.setText(yewuList.get(position));
                if (position == 0){
                    yewu = "";
                }else{
                    yewu = yewuList.get(position);
                }
                isSort = true;
                list.clear();
                getData();
                dialog.dismiss();
            }
        });
    }

    /**
     * 选择区域
     * */
    private void cityDialog() {
        CityPicker cityPicker = new CityPicker.Builder(this)
                .textSize(20).title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#0CB6CA")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province(province)
                .city(city)
                .district(district)
                .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                .provinceCyclic(true)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                province = citySelected[0];
                //城市
                city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                district = citySelected[2];
                tvArea.setText(district);
                isSort = true;
                distance = "";
                latitude = 0.0 ;
                longitude = 0.0;
                region = district;
                list.clear();
                getData();
            }
            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                longitude = aMapLocation.getLongitude();
                currentLng = aMapLocation.getLongitude();
                latitude = aMapLocation.getLatitude();
                currentLat = aMapLocation.getLatitude();
                Log.i("lat+long", "经度：" + longitude + "维度：" + latitude);
                tvArea.setText(aMapLocation.getDistrict());
                region = aMapLocation.getDistrict();
                getData();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(QXActivity.this, "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * 获取数据
     * */
    private void getData() {
        loading.show();
        Call<StoreBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getQXSList(latitude,longitude,page,num,region,distance,yewu);
        call.enqueue(new SimpleCallBack<StoreBean>(QXActivity.this) {
            @Override
            public void onSuccess(Response<StoreBean> response) {
                loading.dismiss();
                try {
                    if (refreshLayout!=null){
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                    if (response.body().getData().size()==0||response.body().getData().size()<10){
                        refreshLayout.setNoMoreData(true);
                    }else{
                        refreshLayout.setNoMoreData(false);
                    }
                    list.addAll(response.body().getData());
                    List<LatLonPoint> points = new ArrayList<>();
                    for (int i =0;i<list.size();i++){
                        StoreBean.DataBean storeBean =list.get(i);
                        points.add(new LatLonPoint(storeBean.getAddress().getLat(),storeBean.getAddress().getLng()));
                    }
                    LatLonPoint end = new LatLonPoint(currentLat,currentLng);
                    DistanceSearch search = new DistanceSearch(QXActivity.this);
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
                            if (isSort){
                                Collections.sort(list);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    search.calculateRouteDistanceAsyn(query);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<StoreBean> response) {
                loading.dismiss();
                try {
                    String error = response.errorBody().string();
                    JSONObject object = new JSONObject(error);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                }catch (Exception e){
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

    @Override
    public void onItemClick(View view, int position) {
        intent = new Intent();
        intent.setClass(this,QPSORQXInfoActivity.class);
        intent.putExtra("id",list.get(position).getUid());
        startActivity(intent);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        list.clear();
        page = 1;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }
    //动态权限申请后处理
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted callDirectly(mobile);
                }else {
                    // Permission Denied Toast.makeText(MainActivity.this,"CALL_PHONE Denied", Toast.LENGTH_SHORT) .show();
                }break;
            default:super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /***
     * 选择地图
     * */
    @Override
    public void chooseMap(int position) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setContentView(R.layout.dialog_check_map);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvGaode = dialog.findViewById(R.id.tv_gaode);
        TextView tvBaidu = dialog.findViewById(R.id.tv_baidu);
        tvGaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkGaodeMap(list.get(position).getAddress().getLat(),list.get(position).getAddress().getLng(),list.get(position).getAddress().getAddress());
            }
        });
        tvBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkBaiduMap(list.get(position).getAddress().getLat(),list.get(position).getAddress().getLng(),list.get(position).getAddress().getAddress());
            }
        });
    }
    //跳转到高德地图
    private void checkGaodeMap(double latitude,double longtitude,String address){
        if (isInstallApk(QXActivity.this, "com.autonavi.minimap")) {// 是否安装了高德地图
            try {
                Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=&poiname="+address+"&lat="
                        + latitude
                        + "&lon="
                        + longtitude + "&dev=0");
                QXActivity.this.startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        }else {// 未安装
            Toast.makeText(QXActivity.this, "您尚未安装高德地图", Toast.LENGTH_LONG)
                    .show();
            Uri uri = Uri
                    .parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            QXActivity.this.startActivity(intent);
        }
    }

    //跳转到百度地图
    private void checkBaiduMap(double latitude,double longtitude,String address) {
        if (isInstallApk(QXActivity.this, "com.baidu.BaiduMap")) {// 是否安装了百度地图
            try {
                Intent intent = Intent.getIntent("intent://map/direction?destination=latlng:"
                        + latitude + ","
                        + longtitude + "|name:"+address + // 终点
                        "&mode=driving&" + // 导航路线方式
                        "region=" + //
                        "&src=#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                QXActivity.this.startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        }else {// 未安装
            Toast.makeText(QXActivity.this, "您尚未安装百度地图", Toast.LENGTH_LONG)
                    .show();
            Uri uri = Uri
                    .parse("market://details?id=com.baidu.BaiduMap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            QXActivity.this.startActivity(intent);
        }
    }
    /** 判断手机中是否安装指定包名的软件 */
    public static boolean isInstallApk(Context context, String name) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(name)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
}
