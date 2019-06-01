package com.jieniuwuliu.jieniu.peisongyuan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.ScanQCActivity;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.PSOrderInfo;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.peisongyuan.adapter.RenwuAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PsyHomeFragment extends BaseFragment implements OnItemClickListener, OnLoadMoreListener, OnRefreshListener, AMapLocationListener{
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    private RenwuAdapter adapter;
    private Intent intent;
    private String type = "paisong";// daijie,paisong  wancheng
    private String token;
    private int page = 1,pageNum = 10;
    private MyLoading loading;
    private List<PSOrderInfo.DataBean> list;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    private String startTime = "",endTime = "",address = "";//当前位置
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.psy_home;
    }

    @Override
    protected void init() {
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        list = new ArrayList<>();
        loading = new MyLoading(getActivity(),R.style.CustomDialog);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(manager);
        adapter = new RenwuAdapter(getActivity(),list,type);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        token = (String) SPUtil.get(getActivity(), Constant.TOKEN, Constant.TOKEN, "");
        getData();
        location();
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
    @Override
    public void onResume() {
        super.onResume();
//        list.clear();
    }
    /**
     * 获取数据
     * */
    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getPSorderList(type,page,pageNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                            }
                            PSOrderInfo psOrderInfo = (PSOrderInfo) GsonUtil.praseJsonToModel(response.body().string(),PSOrderInfo.class);
                            if (psOrderInfo.getData().size()<10){
                                refreshLayout.setNoMoreData(true);
                            }
                            list.addAll(psOrderInfo.getData());
                            adapter.notifyDataSetChanged();
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
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_scan,R.id.tv_start_date,R.id.tv_end_date,R.id.tv_position,R.id.img_search,R.id.radio_btn1, R.id.radio_btn2, R.id.radio_btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_scan:
                intent = new Intent();
                intent.setClass(getActivity(),ScanQCActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.tv_position:
                MyToast.show(getActivity(),"当前位置为："+address);
                break;
            case R.id.tv_start_date://日期
                showDateDialog(1);
                break;
            case R.id.tv_end_date://日期
                showDateDialog(2);
                break;
            case R.id.radio_btn1://未接单
                type = "daijie";
                page = 1;
                list.clear();
                getData();
                break;
            case R.id.radio_btn2://派送中
                type = "paisong";
                page = 1;
                list.clear();
                getData();
                break;
            case R.id.radio_btn3://已完成
                type = "wancheng";
                page = 1;
                list.clear();
                getData();
                break;
            case R.id.img_search://搜索
                String info = etSearch.getText().toString();
                selectOrder( info,"","");
                KeyboardUtil.hideSoftKeyboard(getActivity());
                break;
        }
    }
    /**
     * 日期选择框
     *
     * @param type*/
    private void showDateDialog(int type) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String time = "";
                if ((month+1)<10){
                    if (dayOfMonth<10){
                        time = year+"-0"+(month+1)+"-0"+dayOfMonth;
                    }else{
                        time = year+"-0"+(month+1)+"-"+dayOfMonth;                    }
                }else{
                    if (dayOfMonth<10){
                        time = year+"-"+(month+1)+"-0"+dayOfMonth;
                    }else{
                        time = year+"-"+(month+1)+"-"+dayOfMonth;
                    }
                }
                switch (type){
                    case 1:
                        startTime = time;
                        tvStartDate.setText(startTime);
                        break;
                    case 2:
                        endTime = time;
                        tvEndDate.setText(endTime);
                        break;
                }
                if (!startTime.equals("")&&!endTime.equals("")){
                    selectOrder("",startTime,endTime);
                }
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String s = aMapLocation.getCity();
//                aMapLocation.get
//                tvPosition.setText(s.substring(0,2));
                tvPosition.setText(aMapLocation.getDistrict());
                address = aMapLocation.getCity()+aMapLocation.getDistrict()+aMapLocation.getAddress();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    location();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "ACCESS_FINE_LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        intent = new Intent();
        intent.setClass(getActivity(),RenwuInfoActivity.class);
        intent.putExtra("data",list.get(position));
        intent.putExtra("type",type);
        getActivity().startActivity(intent);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page =1;
        list.clear();
        getData();
    }
    /**
     * 查询某个订单
     * */
    private void selectOrder(String info, String startTime,String endTime) {
        etSearch.setText("");
        list.clear();
        Log.i("length",""+list.size());
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getPSSelectOrder(type,info,startTime,endTime);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                            }
                            PSOrderInfo psOrderInfo = (PSOrderInfo) GsonUtil.praseJsonToModel(response.body().string(),PSOrderInfo.class);
                            if (psOrderInfo.getData().size()!=0){
                                list.addAll(psOrderInfo.getData());
                                Log.i("length",""+list.size());
                            }
                            adapter.notifyDataSetChanged();
                            refreshLayout.setNoMoreData(true);
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
                Log.e("fail","fail is:"+t.toString());
                MyToast.show(getActivity(),"网络请求失败，请检查网络");
            }
        });
    }
}
