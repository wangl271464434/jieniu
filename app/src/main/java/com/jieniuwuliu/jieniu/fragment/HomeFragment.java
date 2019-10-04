package com.jieniuwuliu.jieniu.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.amap.api.maps.LocationSource;
import com.jieniuwuliu.jieniu.MainActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.KeyboardUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.ImgBanner;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.bean.RecomStore;
import com.jieniuwuliu.jieniu.home.BJListActivity;
import com.jieniuwuliu.jieniu.home.MsgActivity;
import com.jieniuwuliu.jieniu.home.QXActivity;
import com.jieniuwuliu.jieniu.home.XJCarTypeActivity;
import com.jieniuwuliu.jieniu.home.XJListActivity;
import com.jieniuwuliu.jieniu.home.adapter.HomeAdapter;
import com.jieniuwuliu.jieniu.home.OrderInfoActivity;
import com.jieniuwuliu.jieniu.home.adapter.RecomStoreAdapter;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.view.GlideImageLoader;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements OnItemClickListener,OnLoadMoreListener, AMapLocationListener, TextView.OnEditorActionListener, OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.banner)
    Banner banner;
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
    private int userType,isCertify;
    private int page = 1,pageNum=10;
    private List<OrderInfo> list;
    private List<RecomStore.DataBean> recomList;//推荐门店
    private RecomStoreAdapter recomStoreAdapter;
    private List<ImgBanner.DataBean> imgs;
    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.home;
    }

    @Override
    protected void init() {
        imgs = new ArrayList<>();
        rv.setVisibility(View.GONE);
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
      /*  LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv);*/
        adapter.setOnItemClickListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
    }
    /**
     * 设置轮播图
     * */
    private void setBanner() {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imgs);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
//        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    /**
     * 获取推荐商家
     * */
    private void getRecomList() {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getRecomStoreList(String.valueOf(page),String.valueOf(pageNum));
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    if (refreshLayout!=null){
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                    String json = response.body().string();
                    Log.i("json",json);
                    RecomStore recomStore = (RecomStore) GsonUtil.praseJsonToModel(json,RecomStore.class);
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
        imgs.clear();
        page = 1;
        checkSDK();
        token = (String) SPUtil.get(getActivity(),Constant.TOKEN,Constant.TOKEN,"");
        userType = (int) SPUtil.get(getActivity(),Constant.USERTYPE,Constant.USERTYPE,0);
        isCertify = (int) SPUtil.get(getActivity(),Constant.ISCERTIFY,Constant.ISCERTIFY,0);
        getBanner();
        getRecomList();
    }
    /**
     * 轮播
     * */
    private void getBanner() {
        Call<ImgBanner> call = HttpUtil.getInstance().getApi(token).getBanner();
        call.enqueue(new Callback<ImgBanner>() {
            @Override
            public void onResponse(Call<ImgBanner> call, Response<ImgBanner> response) {
                if (response.code() == 200){
                    imgs.addAll(response.body().getData());
                    setBanner();
                }
            }

            @Override
            public void onFailure(Call<ImgBanner> call, Throwable t) {

            }
        });
    }

    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), permissions, 200);
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
    @OnClick({R.id.tv_position, R.id.tv_msg, R.id.home_tab_1, R.id.home_tab_2, R.id.home_tab_3,R.id.home_tab_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_position://定位
                break;
            case R.id.tv_msg://消息
                intent = new Intent();
                intent.setClass(getActivity(), MsgActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.home_tab_1://捷牛快修
                intent = new Intent();
                intent.setClass(getActivity(),QXActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.home_tab_2://询价
                if (isCertify != 1){
                    MyToast.show(getActivity(),"请去进行认证");
                    return;
                }
                if (isCertify == 3){
                    MyToast.show(getActivity(),"正在认证中");
                    return;
                }
                if (userType == 2){
                    intent = new Intent();
                    intent.setClass(getActivity(),XJCarTypeActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    MyToast.show(getActivity(),"您不是汽修厂，不能发布询价单");
                }
                break;
            case R.id.home_tab_3://询价单
                if (isCertify != 1){
                    MyToast.show(getActivity(),"请去进行认证");
                    return;
                }
                if (isCertify == 3){
                    MyToast.show(getActivity(),"正在认证中");
                    return;
                }
                if (userType == 2){
                    intent = new Intent();
                    intent.setClass(getActivity(),XJListActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    MyToast.show(getActivity(),"您不是汽修厂，不能查看询价单");
                }
                break;
            case R.id.home_tab_4://我要报价
                if (isCertify != 1){
                    MyToast.show(getActivity(),"请去进行认证");
                    return;
                }
                if (isCertify == 3){
                    MyToast.show(getActivity(),"正在认证中");
                    return;
                }
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
        if (list.size()>0){
            intent = new Intent();
            intent.setClass(getActivity(),OrderInfoActivity.class);
            intent.putExtra("orderNo",list.get(position).getOrderNumber());
            getActivity().startActivity(intent);
        }
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        recomList.clear();
        getRecomList();
    }
    /**
     * 上拉加载
     * */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getRecomList();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                Constant.CITY = aMapLocation.getCity();
               tvPosition.setText(Constant.CITY.substring(0,2));
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
                        rv.setVisibility(View.VISIBLE);
                        list.addAll(orderResult.getData());
                        list.add(new OrderInfo());
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
