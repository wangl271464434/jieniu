package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.XJOrder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BJListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1,num = 10;
    private List<XJOrder.DataBean> list;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bjlist;
    }

    @Override
    protected void init() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }
}
