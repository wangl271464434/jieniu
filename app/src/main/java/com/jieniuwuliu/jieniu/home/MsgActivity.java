package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void init() {
        title.setText("消息");
    }


    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }
}
