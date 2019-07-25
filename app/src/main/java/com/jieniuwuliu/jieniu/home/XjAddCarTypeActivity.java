package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.view.SideBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XjAddCarTypeActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sideBar)
    SideBar sideBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xj_add_car_type;
    }

    @Override
    protected void init() {

    }


    @OnClick({R.id.layout_back, R.id.img_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.img_search:
                break;
        }
    }
}
