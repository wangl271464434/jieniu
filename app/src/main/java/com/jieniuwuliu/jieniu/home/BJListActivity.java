package com.jieniuwuliu.jieniu.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.BJOrder;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.XJOrder;
import com.jieniuwuliu.jieniu.home.adapter.BJListAdapter;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class BJListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, OnItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1,num = 10;
    private List<BJOrder.DataBean> list;
    private MyLoading loading;
    private String token;
    private BJListAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bjlist;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        loading = new MyLoading(this,R.style.CustomDialog);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new BJListAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        getData();
    }

    private void getData() {
        loading.show();
        Call<BJOrder> call = HttpUtil.getInstance().getApi(token).getBJOrderList(String.valueOf(page),String.valueOf(num));
        call.enqueue(new SimpleCallBack<BJOrder>(this) {
            @Override
            public void onSuccess(Response<BJOrder> response) {
                loading.dismiss();
                if (refresh!=null){
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                }
                try {
                    BJOrder  xjOrder = response.body();
                    if (xjOrder.getData()!=null){
                        if (xjOrder.getTotal()<10){
                            refresh.setNoMoreData(true);
                        }else{
                            refresh.setNoMoreData(false);
                        }
                        if (xjOrder.getData().size()>0){
                            list.addAll(xjOrder.getData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<BJOrder> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e){
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

    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        list.clear();
        page =1;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (list.get(position).getStype() == 1){
            Intent intent = new Intent();
            intent.setClass(this,BjInfoActivity.class);
            intent.putExtra("data",list.get(position));
            startActivity(intent);
        }else{
            MyToast.show(this,"不能报价");
        }

    }
}
