package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XjInfoActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xj_info;
    }

    @Override
    protected void init() {

    }
    @OnClick({R.id.layout_back, R.id.img1, R.id.img2, R.id.img3, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.img1:
                break;
            case R.id.img2:
                break;
            case R.id.img3:
                break;
            case R.id.btn:
                break;
        }
    }
}
