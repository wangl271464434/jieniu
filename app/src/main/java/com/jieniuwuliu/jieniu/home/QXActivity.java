package com.jieniuwuliu.jieniu.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.adapter.ListAdapter;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.home.adapter.QXAdapter;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
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
    private AMap aMap;
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
        call.enqueue(new Callback<StoreBean>() {
            @Override
            public void onResponse(Call<StoreBean> call, Response<StoreBean> response) {
                loading.dismiss();
                try {
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                            }
                            if (response.body().getData().size()==0||response.body().getData().size()<10){
                                refreshLayout.setNoMoreData(true);
                            }
                            list.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<StoreBean> call, Throwable t) {
                loading.dismiss();
                Log.i("error",t.toString());
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
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }
    /**
     * 打电话
     * */
    @Override
    public void callPhone(int positon) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 100);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + list.get(positon).getAddress().getPhone());
        intent.setData(data);
        startActivity(intent);
    }
    /**
     * 复制微信
     * */
    @Override
    public void callWeChat(int positon) {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label",list.get(positon).getWechat());
        manager.setPrimaryClip(clipData);
        MyToast.show(this,"复制成功");
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
}
