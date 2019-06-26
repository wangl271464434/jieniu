package com.jieniuwuliu.jieniu.mine.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.bean.AddressItem;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.mine.adapter.MapAddressAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 根据地图搜索地址
 * */
public class ChooseAddressActivity extends AppCompatActivity implements AMapLocationListener, PoiSearch.OnPoiSearchListener, OnItemClickListener {

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_context)
    EditText etContext;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.rv)
    RecyclerView rv;
    private AMap aMap;
    private double longitude;
    private double latitude;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    private String province = "陕西省";
    private String city = "西安市";
    private List<AddressItem> list;
    private MapAddressAdapter adapter;
    private PoiItem currentItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        ButterKnife.bind(this);
        map.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = map.getMap();
            UiSettings settings = aMap.getUiSettings();
            settings.setLogoBottomMargin(-50);
            settings.setZoomControlsEnabled(false);
//            aMap.setMyLocationEnabled(true);
            //设置缩放级别
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            aMap.setMyLocationStyle(myLocationStyle);
//                    setUpMap();
        }
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new MapAddressAdapter(this,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        checkSDK();
    }
    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
//        mLocationClient.stopLocation();//停止定位
//        mLocationClient.onDestroy();//销毁定位客户端。
        map.onDestroy();
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
    @OnClick({R.id.back,R.id.tv_sure,R.id.img_search, R.id.tv_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_sure:
                if (currentItem!=null){
                    CarEvent event = new CarEvent();
                    event.setType("address");
                    event.setPoint(currentItem.getLatLonPoint());
                    event.setAddress(province+tvCity.getText().toString()+currentItem.getAdName()+currentItem.toString());
                    EventBus.getDefault().post(event);
                    finish();
                }else{
                    MyToast.show(getApplicationContext(),"请选择一个地点");
                }
                break;
            case R.id.tv_city:
                chooseCity();
                break;
            case R.id.img_search:
                String address = etContext.getText().toString();
                if (address.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入您想要搜索的地址");
                    return;
                }
                searchAddress(address);
                KeyboardUtil.hideSoftKeyboard(ChooseAddressActivity.this);
                break;
        }
    }
    //选择城市
    private void chooseCity() {
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
                .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                .provinceCyclic(true)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(true)
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
                tvCity.setText(city);
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
                latitude = aMapLocation.getLatitude();
                Log.i("lat+long", "经度：" + longitude + "维度：" + latitude);
                province = aMapLocation.getProvince();
                city = aMapLocation.getCity();
                tvCity.setText(city);
                searchAddress(aMapLocation.getAddress());
                //将地图移动到定位点
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
                //点击定位按钮 能够将地图的中心移动到定位点
//                mListener.onLocationChanged(aMapLocation);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
//                Toast.makeText(ChooseAddressActivity.this, "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    location();
                } else {
                    // Permission Denied
                    Toast.makeText(ChooseAddressActivity.this, "ACCESS_FINE_LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /**
     * 根据位置搜索地址
     * */
    private void searchAddress(String address) {
        PoiSearch.Query query = new PoiSearch.Query(address,"",city);
        query.setPageSize(10);
        query.setPageNum(1);
        PoiSearch poiSearch = new PoiSearch(this,query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
        Log.w("poi",poiResult.toString());
        list.clear();
        for (int i = 0 ;i<poiResult.getPois().size();i++){
            AddressItem item = new AddressItem();
            item.setPoiItem(poiResult.getPois().get(i));
            if (i==0){
              item.setChecked(true);
            }else{
                item.setChecked(false);
            }
            list.add(item);
        }
        adapter.notifyDataSetChanged();
        aMap.clear();
        currentItem = list.get(0).getPoiItem();
        LatLng latLng = new LatLng(list.get(0).getPoiItem().getLatLonPoint().getLatitude(), list.get(0).getPoiItem().getLatLonPoint().getLongitude());
        aMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource (R.drawable.location_marker))
                .draggable(false));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        Log.w("poi",poiItem.toString());
    }

    @Override
    public void onItemClick(View view, int position) {
        aMap.clear();
        currentItem = list.get(position).getPoiItem();
        for (int i = 0 ;i<list.size();i++){
            if (i==position){
                list.get(i).setChecked(true);
            }else{
                list.get(i).setChecked(false);
            }
        }
        adapter.notifyDataSetChanged();
        LatLng latLng = new LatLng(currentItem.getLatLonPoint().getLatitude(), currentItem.getLatLonPoint().getLongitude());
        aMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource (R.drawable.location_marker))
                .draggable(false));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }
}
