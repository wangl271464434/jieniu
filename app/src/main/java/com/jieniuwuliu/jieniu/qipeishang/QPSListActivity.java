package com.jieniuwuliu.jieniu.qipeishang;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.CarTypeActivity;
import com.jieniuwuliu.jieniu.MoreCarActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.QPType;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarTypeEvent;
import com.jieniuwuliu.jieniu.messageEvent.LuntanEvent;
import com.jieniuwuliu.jieniu.mine.adapter.QpTypeAdapter;
import com.jieniuwuliu.jieniu.qipeishang.adapter.QiPeiShangListAdapter;
import com.jieniuwuliu.jieniu.qipeishang.adapter.TypeQPSAdater;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QPSListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, QiPeiShangListAdapter.CallBack, OnItemClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private int page = 1,num = 10;
    private Intent intent;
    private List<StoreBean.DataBean> list;
    private int partsType = 2,type;
    private String car = "",token;
    private QiPeiShangListAdapter adapter;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qpslist;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        type = getIntent().getIntExtra("type",0);
        car = getIntent().getStringExtra("car");
        if ("".equals(car)){
            right.setVisibility(View.GONE);
        }else{
            right.setVisibility(View.VISIBLE);
            right.setText(car);
        }
        loading = new MyLoading(this,R.style.CustomDialog);
        refreshlayout.setOnRefreshListener(this);
        refreshlayout.setOnLoadMoreListener(this);
        list = new ArrayList<>();
        LinearLayoutManager listManager = new LinearLayoutManager(this);
        rv.setLayoutManager(listManager);
        adapter = new QiPeiShangListAdapter(this,list);
        rv.setAdapter(adapter);
        adapter.setCallBack(this);
        adapter.setOnItemClickListener(this);
        getData();
    }
    /**
     * 获取数据
     * */
    private void getData() {
        loading.show();
        try{
            JSONObject object = new JSONObject();
            object.put("types",type);
            object.put("partscity",partsType);
            object.put("car",car);
            object.put("page",page);
            object.put("number",num);
            object.put("nickname","");
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Call<StoreBean> call = HttpUtil.getInstance().getApi(token).getQXORQBList(body);
            call.enqueue(new SimpleCallBack<StoreBean>(QPSListActivity.this) {
                @Override
                public void onSuccess(Response<StoreBean> response) {
                    loading.dismiss();
                    if (refreshlayout!=null){
                        refreshlayout.finishRefresh();
                        refreshlayout.finishLoadMore();
                    }
                    if (response.body().getData().size()==0||response.body().getData().size()<10){
                        refreshlayout.setNoMoreData(true);
                    }else{
                        refreshlayout.setNoMoreData(false);
                    }
                    list.addAll(response.body().getData());
                    if (list.size()==0){
                        tvEmpty.setVisibility(View.VISIBLE);
                        MyToast.show(getApplicationContext(),"未获取到内容");
                    }else{
                        tvEmpty.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFail(int errorCode, Response<StoreBean> response) {
                    loading.dismiss();
                    try{
                        String s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        type = intent.getIntExtra("type",0);
        car = intent.getStringExtra("car");
        if ("".equals(car)){
            right.setVisibility(View.GONE);
        }else{
            right.setVisibility(View.VISIBLE);
            right.setText(car);
        }
        page = 1;
        list.clear();
        getData();
    }
    /**
     * 获取汽配城归属
     * */
    private void getQpList() {
        Call<QPType> call = HttpUtil.getInstance().getApi(token).getQpList();
        call.enqueue(new Callback<QPType>() {
            @Override
            public void onResponse(Call<QPType> call, Response<QPType> response) {
                try {
                    if (response.code() == 200){
                        showPartDialog(response.body().getData());
                    }else{
                        String s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        MyToast.show(getApplicationContext(),object.getString("msg"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<QPType> call, Throwable t) {
                MyToast.show(getApplicationContext(),"网络连接失败");
            }
        });
    }
    //归属地
    private void showPartDialog(List<QPType.DataBean> data) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.8);
        window.setAttributes(params);
        dialog.setContentView(R.layout.dialog_qptype_list);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv);
        ImageView img = dialog.findViewById(R.id.img_close);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(R.mipmap.qps_up);
                dialog.dismiss();
            }
        });
        QpTypeAdapter adapter = new QpTypeAdapter(this,data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                img.setImageResource(R.mipmap.qps_up);
                dialog.dismiss();
                title.setText(data.get(position).getNickname());
                partsType = data.get(position).getId();
                page = 1;
                list.clear();
                getData();
            }
        });
    }
    @OnClick({R.id.back, R.id.layout_title, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.layout_title:
                img.setImageResource(R.mipmap.qps_down);
                getQpList();
                break;
            case R.id.right:
                intent = new Intent();
                intent.setClass(this, MoreCarActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        list.clear();
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //打电话
    @Override
    public void callPhone(int positon) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 100);
                return;
            }
        }
        Constant.isCall = false;
        Constant.CALLPHONE = list.get(positon).getAddress().getPhone();
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + list.get(positon).getAddress().getPhone());
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        intent = new Intent();
        intent.setClass(this,QPSORQXInfoActivity.class);
        intent.putExtra("id",list.get(position).getUid());
        startActivity(intent);
    }
}
