package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.home.adapter.XjListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XJListActivity extends BaseActivity {
    @BindView(R.id.layout_title)
    RelativeLayout layoutTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private XjListAdapter adapter;
    private List<Integer> list;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xjlist;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new XjListAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }
    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }
}
