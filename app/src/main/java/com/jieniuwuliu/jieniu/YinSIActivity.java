package com.jieniuwuliu.jieniu;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YinSIActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yin_si;
    }

    @Override
    protected void init() {
        title.setText("捷牛隐私政策");
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
