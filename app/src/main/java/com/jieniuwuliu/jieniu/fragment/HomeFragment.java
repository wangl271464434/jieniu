package com.jieniuwuliu.jieniu.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
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
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.ScanQCActivity;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.bean.RecomStore;
import com.jieniuwuliu.jieniu.home.QXActivity;
import com.jieniuwuliu.jieniu.home.adapter.HomeAdapter;
import com.jieniuwuliu.jieniu.home.OrderInfoActivity;
import com.jieniuwuliu.jieniu.home.adapter.RecomStoreAdapter;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements OnItemClickListener, OnRefreshListener, OnLoadMoreListener, AMapLocationListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    private HomeAdapter adapter;
    private Intent intent;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    private MyLoading loading;
    private String token;
    private int page = 1,pageNum=10;
    private int state =0,type=0;//配送中
    private List<OrderInfo> list;
    private List<RecomStore.DataBean> recomList;//推荐门店
    private RecomStoreAdapter recomStoreAdapter;
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.home;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        recomList = new ArrayList<>();
        loading = new MyLoading(getActivity(),R.style.CustomDialog);
        //推荐商家
        LinearLayoutManager shopManager = new LinearLayoutManager(getActivity());
        shopManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(shopManager);
        recomStoreAdapter = new RecomStoreAdapter(getActivity(),recomList);
        recyclerView.setAdapter(recomStoreAdapter);
        recomStoreAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent();
                intent.setClass(getActivity(),QPSORQXInfoActivity.class);
                intent.putExtra("id",recomList.get(position).getId());
                startActivity(intent);
            }
        });
        //最新物流
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(manager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.custom_divider));
        rv.addItemDecoration(divider);
        adapter = new HomeAdapter(getActivity(),list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

    }
    /**
     * 获取推荐商家
     * */
    private void getRecomList() {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getRecomStoreList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            RecomStore recomStore = (RecomStore) GsonUtil.praseJsonToModel(response.body().string(),RecomStore.class);
                            recomList.addAll(recomStore.getData());
                            recomStoreAdapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getActivity(), object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("error","recomlist error is:"+t.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        recomList.clear();
        checkSDK();
        token = (String) SPUtil.get(getActivity(),Constant.TOKEN,Constant.TOKEN,"");
        getRecomList();
    }

    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }else{
                location();
            }
        }else{
            location();
        }
    }
    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
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
    @OnClick({R.id.tv_position, R.id.tv_scan, R.id.home_tab_1, R.id.home_tab_2, R.id.home_tab_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_position://定位
                break;
            case R.id.tv_scan://扫码
                intent = new Intent();
                intent.setClass(getActivity(),ScanQCActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.home_tab_1://捷牛快修
                intent = new Intent();
                intent.setClass(getActivity(),QXActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.home_tab_2://汽配商城
                MyToast.show(getActivity(),"该功能正在开发");
                break;
            case R.id.home_tab_3://我的快件
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        intent = new Intent();
        intent.setClass(getActivity(),OrderInfoActivity.class);
        intent.putExtra("orderNo",list.get(position).getOrderNumber());
        getActivity().startActivity(intent);
    }
    /**
     * 下拉刷新
     * */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        list.clear();
        getData();
    }
    /**
     * 上拉加载
     * */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String s = aMapLocation.getCity();
               tvPosition.setText(s.substring(0,2));
               getData();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * 获取个人订单
     * */
    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getHomeOrderList(page,pageNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                            }
                            String json = response.body().string();
                            OrderResult orderResult = (OrderResult) GsonUtil.praseJsonToModel(json,OrderResult.class);
                            if (orderResult.getData().size()<10){
                                refreshLayout.setNoMoreData(true);
                            }
                            if (orderResult.getData().size()!=0){
                                tvEmpty.setVisibility(View.GONE);
                                list.addAll(orderResult.getData());
                                adapter.notifyDataSetChanged();
                            }else{
                                tvEmpty.setVisibility(View.VISIBLE);
                            }
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getActivity(), object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                MyToast.show(getActivity(),"网络出现错误");
            }
        });
    }
}
