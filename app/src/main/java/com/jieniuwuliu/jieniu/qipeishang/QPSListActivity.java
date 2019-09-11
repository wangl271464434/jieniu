package com.jieniuwuliu.jieniu.qipeishang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.CarTypeActivity;
import com.jieniuwuliu.jieniu.MoreCarActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarTypeEvent;
import com.jieniuwuliu.jieniu.qipeishang.adapter.TypeQPSAdater;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QPSListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener{

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private int page = 1,num = 10;
    private Intent intent;
    private boolean isShow = false;
    private List<String> types;
    private TypeQPSAdater typeQPSAdater;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qpslist;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        refreshlayout.setOnRefreshListener(this);
        refreshlayout.setOnLoadMoreListener(this);
        types = new ArrayList<>();
        types.add("全部汽配城");
        types.add("海纳汽配城");
        types.add("三桥汽配城");
        types.add("其他汽配城");
        LinearLayoutManager typeManager = new LinearLayoutManager(this);
        rvType.setLayoutManager(typeManager);
        typeQPSAdater = new TypeQPSAdater(this,types);
        rvType.setAdapter(typeQPSAdater);
        typeQPSAdater.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                title.setText(types.get(position));
                img.setImageResource(R.mipmap.qps_down);
                rvType.setVisibility(View.GONE);
                isShow = false;
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(CarTypeEvent event) {
        if (event!=null){
            right.setText(event.getName());
        }
    }
    @OnClick({R.id.back, R.id.layout_title, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.layout_title:
                if(isShow){
                    img.setImageResource(R.mipmap.qps_down);
                    rvType.setVisibility(View.GONE);
                    isShow = false;
                }else{
                    img.setImageResource(R.mipmap.qps_up);
                    rvType.setVisibility(View.VISIBLE);
                    isShow = true;
                }
                break;
            case R.id.right:
                intent = new Intent();
                intent.setClass(this, MoreCarActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
