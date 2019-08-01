package com.jieniuwuliu.jieniu.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.MoreCarActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.bean.Car;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Label;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.messageEvent.CarTypeEvent;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.mine.ui.MyFollowActivity;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.qipeishang.adapter.QiPeiShangAdater;
import com.jieniuwuliu.jieniu.qipeishang.adapter.QiPeiShangListAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QiPeiShangFragment extends Fragment implements OnItemClickListener, QiPeiShangListAdapter.CallBack, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_car)
    RecyclerView rvCar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.layout_list)
    LinearLayout layoutList;
    private View view;
    private QiPeiShangAdater adater;
    private QiPeiShangListAdapter listAdapter;
    private List<Label> cars;
    private List<StoreBean.DataBean> list;
    private Intent intent;
    private String token;
    private int page = 1;//页数
    private  int pageNum = 10;//一页返回的条数
    private String type = "",storeName = "";//筛选条件
    private MyLoading loading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.qipeishang, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        loading = new MyLoading(getActivity(),R.style.CustomDialog);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        cars = new ArrayList<>();
        cars.add(new Label("大众","https://car3.autoimg.cn/cardfs/series/g29/M07/AF/BE/100x100_f40_autohomecar__wKgHJFs9vGCABLhjAAAxZhBm1OY195.png"));
        cars.add(new Label("丰田","https://car3.autoimg.cn/cardfs/series/g29/M04/AF/BE/100x100_f40_autohomecar__wKgHJFs9vGSAY09jAAAvZAwDhiM445.png"));
        cars.add(new Label("奔驰","https://car3.autoimg.cn/cardfs/series/g26/M00/AF/E7/100x100_f40_autohomecar__wKgHHVs9u6mAaY6mAAA2M840O5c440.png"));
        cars.add(new Label("宝马","https://car2.autoimg.cn/cardfs/series/g26/M0B/AF/DD/100x100_f40_autohomecar__wKgHHVs9uuSAdz-2AAAtY7ZwY3U416.png"));
        cars.add(new Label("本田","https://car3.autoimg.cn/cardfs/series/g29/M0B/AF/A0/100x100_f40_autohomecar__ChcCSFs9s1iAGMiNAAAlP_CBhLY618.png"));
        cars.add(new Label("吉利","https://car2.autoimg.cn/cardfs/series/g3/M02/F7/00/autohomecar__ChsEkVxqFpWAMeTKAAAnrciISZ0845.png"));
        cars.add(new Label("雪佛兰","https://car2.autoimg.cn/cardfs/series/g29/M03/AF/A2/100x100_f40_autohomecar__wKgHJFs9uFKAb5uSAAAhD-fryHg510.png"));
        cars.add(new Label("奥迪","https://car2.autoimg.cn/cardfs/series/g26/M0B/AE/B3/100x100_f40_autohomecar__wKgHEVs9u5WAV441AAAKdxZGE4U148.png"));
        cars.add(new Label("汽车用品","汽车用品"));
        cars.add(new Label("汽保工具","汽保工具"));
        GridLayoutManager manager = new GridLayoutManager(getActivity(),5);
        rvCar.setLayoutManager(manager);
        adater = new QiPeiShangAdater(getActivity(),cars);
        rvCar.setAdapter(adater);
        adater.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                type = cars.get(position).getName();
                page = 1;
                pageNum = 10;
                storeName = "";
                list.clear();
                switch (type){
                    case "汽车用品":
                        getStoreList(3);
                        break;
                    case "汽保工具":
                        getStoreList(4);
                        break;
                        default:
                            getStoreList();
                            break;
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.custom_divider));
        rv.addItemDecoration(divider);
        rv.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        listAdapter = new QiPeiShangListAdapter(getActivity(),list);
        rv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(this);
        listAdapter.setCallBack(this);
        token = (String) SPUtil.get(getActivity(),Constant.TOKEN,Constant.TOKEN,"");
        getStoreList();
    }
    /**
     * 通过门店类型筛选门店
     * */
    private void getStoreList(int i) {
        loading.show();
        Call<StoreBean> call = HttpUtil.getInstance().getApi(token).getQXORQBList(i,storeName,page,pageNum);
        call.enqueue(new SimpleCallBack<StoreBean>(getActivity()) {
            @Override
            public void onSuccess(Response<StoreBean> response) {
                loading.dismiss();
                try{
                    if (refreshLayout!=null){
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                    if (response.body().getData().size()==0||response.body().getData().size()<10){
                        refreshLayout.setNoMoreData(true);
                    }
                    list.addAll(response.body().getData());
                    if (list.size()==0){
                        MyToast.show(getActivity(),"未获取到内容");
                    }
                    listAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<StoreBean> response) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(CarTypeEvent event) {
        type = event.getName();
        storeName = "";
        page = 1;
        pageNum = 10;
        list.clear();
        getStoreList();
    }
    //汽配商
    private void getStoreList() {
        loading.show();
        Call<StoreBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getQPSList(type,storeName,page,pageNum);
        call.enqueue(new SimpleCallBack<StoreBean>(getActivity()) {
            @Override
            public void onSuccess(Response<StoreBean> response) {
                try{
                    loading.dismiss();
                    if (refreshLayout!=null){
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                    if (response.body().getData().size()==0||response.body().getData().size()<10){
                        refreshLayout.setNoMoreData(true);
                    }
                    list.addAll(response.body().getData());
                    listAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<StoreBean> response) {
                try{
                    loading.dismiss();
                    String error = response.errorBody().string();
                    JSONObject object = new JSONObject(error);
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

    @OnClick({R.id.tv_more,R.id.img_search})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_more:
                intent = new Intent();
                intent.setClass(getActivity(),MoreCarActivity.class);
                startActivity(intent);
                break;
            case R.id.img_search:
                storeName = etSearch.getText().toString();
                type = "";
                list.clear();
                getStoreList(5);
                KeyboardUtil.hideSoftKeyboard(getActivity());
                etSearch.setText("");
                break;
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        intent = new Intent();
        intent.setClass(getActivity(),QPSORQXInfoActivity.class);
        intent.putExtra("id",list.get(position).getUid());
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        type ="";
        storeName = "";
        page = 1;
        pageNum = 10;
        list.clear();
        getStoreList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 打电话
     * */
    @Override
    public void callPhone(int positon) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, 100);
                return;
            }
        }
        Constant.CALLPHONE = list.get(positon).getAddress().getPhone();
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
        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label",list.get(positon).getWechat());
        manager.setPrimaryClip(clipData);
        MyToast.show(getActivity(),"复制成功");
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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        switch (type){
            case "汽车用品":
                getStoreList(3);
                break;
            case "汽保工具":
                getStoreList(4);
                break;
            default:
                getStoreList();
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        list.clear();
        switch (type){
            case "汽车用品":
                getStoreList(3);
                break;
            case "汽保工具":
                getStoreList(4);
                break;
            default:
                getStoreList();
                break;
        }
    }
}
