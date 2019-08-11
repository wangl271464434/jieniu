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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.bean.RecomStore;
import com.jieniuwuliu.jieniu.home.BJListActivity;
import com.jieniuwuliu.jieniu.home.QPShopActivity;
import com.jieniuwuliu.jieniu.home.QXActivity;
import com.jieniuwuliu.jieniu.home.XJCarTypeActivity;
import com.jieniuwuliu.jieniu.home.XJListActivity;
import com.jieniuwuliu.jieniu.home.adapter.HomeAdapter;
import com.jieniuwuliu.jieniu.home.OrderInfoActivity;
import com.jieniuwuliu.jieniu.home.adapter.RecomStoreAdapter;
import com.jieniuwuliu.jieniu.jijian.JiJianSelectActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements OnItemClickListener,OnLoadMoreListener, AMapLocationListener, TextView.OnEditorActionListener {
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
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.layout_order)
    LinearLayout layoutOrder;
    @BindView(R.id.tv_more)
    TextView tvMore;
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
    private int userType;
    private int page = 1,pageNum=10;
    private List<OrderInfo> list;
    private List<RecomStore.DataBean> recomList;//推荐门店
    private RecomStoreAdapter recomStoreAdapter;
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.home;
    }

    @Override
    protected void init() {
        etSearch.setOnEditorActionListener(this);
        list = new ArrayList<>();
        recomList = new ArrayList<>();
        loading = new MyLoading(getActivity(),R.style.CustomDialog);
        //推荐商家
        LinearLayoutManager shopManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(shopManager);
        recomStoreAdapter = new RecomStoreAdapter(getActivity(),recomList);
        recyclerView.setAdapter(recomStoreAdapter);
        recomStoreAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent();
                intent.setClass(getActivity(),QPSORQXInfoActivity.class);
                intent.putExtra("id",recomList.get(position).getUid());
                startActivity(intent);
            }
        });
        //最新物流
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        adapter = new HomeAdapter(getActivity(),list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        refreshLayout.setOnLoadMoreListener(this);

    }
    /**
     * 获取推荐商家
     * */
    private void getRecomList() {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getRecomStoreList();
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    if (refreshLayout!=null){
                        refreshLayout.finishLoadMore();
                    }
                    RecomStore recomStore = (RecomStore) GsonUtil.praseJsonToModel(response.body().string(),RecomStore.class);
                    if (recomStore.getData().size()<10){
                        refreshLayout.setNoMoreData(true);
                    }else{
                        refreshLayout.setNoMoreData(false);
                    }
                    recomList.addAll(recomStore.getData());
                    recomStoreAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {

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
        userType = (int) SPUtil.get(getActivity(),Constant.USERTYPE,Constant.USERTYPE,0);
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
    @OnClick({R.id.tv_position, R.id.tv_scan, R.id.home_tab_1, R.id.home_tab_2, R.id.home_tab_3,R.id.home_tab_4})
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
            case R.id.home_tab_2://询价
                if (userType == 2){
                    intent = new Intent();
                    intent.setClass(getActivity(),XJCarTypeActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    MyToast.show(getActivity(),"您不是汽修厂，不能发布询价单");
                }
                break;
            case R.id.home_tab_3://询价单
                if (userType == 2){
                    intent = new Intent();
                    intent.setClass(getActivity(),XJListActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    MyToast.show(getActivity(),"您不是汽修厂，不能查看询价单");
                }
                break;
            case R.id.home_tab_4://我要报价
                if (userType == 1){
                    intent = new Intent();
                    intent.setClass(getActivity(), BJListActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    MyToast.show(getActivity(),"您不是配件商，不能查看报价单");
                }

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
     * 上拉加载
     * */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getRecomList();
//        getData();
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
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    String json = response.body().string();
                    OrderResult orderResult = (OrderResult) GsonUtil.praseJsonToModel(json,OrderResult.class);
                    if (orderResult.getData().size()!=0){
                        tvEmpty.setVisibility(View.GONE);
                        list.addAll(orderResult.getData());
                        adapter.notifyDataSetChanged();
                    }else{
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getActivity(),s);
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND||
                (event!=null&&event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
            String info = etSearch.getText().toString();
            selectOrder(info);
            KeyboardUtil.hideSoftKeyboard(getActivity());
            return true;
        }
        return false;
    }
    /**
     * 查询某一条订单
     * */
    public void selectOrder(String info){
        etSearch.setText("");
        list.clear();
        Call<ResponseBody> call =HttpUtil.getInstance().getApi(token).selectOrder(info);
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    if (refreshLayout!=null){
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                    String json = new JSONObject(response.body().string()).getString("data");
                    OrderInfo dataBean = (OrderInfo) GsonUtil.praseJsonToModel(json,OrderInfo.class);
                    list.add(dataBean);
                    adapter.notifyDataSetChanged();
                    refreshLayout.setNoMoreData(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                MyToast.show(getActivity(),s);
            }
        });
    }
}
