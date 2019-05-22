package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class QPShopActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qpshop;
    }

    @Override
    protected void init() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
