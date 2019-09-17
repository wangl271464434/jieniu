package com.jieniuwuliu.jieniu.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.JwtUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.XJOrder;
import com.jieniuwuliu.jieniu.home.adapter.XjListAdapter;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class XJListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, OnItemClickListener {
    @BindView(R.id.layout_title)
    RelativeLayout layoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private XjListAdapter adapter;
    private List<XJOrder.DataBean> list;
    private String token;
    private int page = 1,num = 10;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xjlist;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        Log.i("userid", JwtUtil.JWTParse(token));
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new XjListAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        page = 1;
        getData();
    }

    private void getData() {
        loading.show();
        Call<XJOrder> call = HttpUtil.getInstance().getApi(token).getXJOrderList(String.valueOf(page),String.valueOf(num));
        call.enqueue(new SimpleCallBack<XJOrder>(this) {
            @Override
            public void onSuccess(Response<XJOrder> response) {
                loading.dismiss();
                if (refresh!=null){
                    refresh.finishRefresh();
                    refresh.finishLoadMore();
                }
                try {
                    XJOrder  xjOrder = response.body();
                    if (xjOrder.getTotal()<10){
                        refresh.setNoMoreData(true);
                    }else{
                        refresh.setNoMoreData(false);
                    }
//                 Log.i("list",json);
                    if (xjOrder.getData().size()>0){
                        list.addAll(xjOrder.getData());
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(int errorCode, Response<XJOrder> response) {
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
        page = 1;
        num = 10;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(this,XJContentActivity.class);
        intent.putExtra("data",list.get(position));
        startActivity(intent);
    }
}
