package com.jieniuwuliu.jieniu;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 服务协议
 */
public class FuWuActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fu_wu;
    }

    @Override
    protected void init() {
        title.setText("快件服务协议");
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
