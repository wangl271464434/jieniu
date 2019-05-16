package com.jieniuwuliu.jieniu.mine.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 月卡
 * */
public class MyCardActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_card;
    }

    @Override
    protected void init() {
        title.setText("月卡服务");
    }

    @OnClick({R.id.back, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn:
                startAcy(MonthCardPayActivity.class);
                break;
        }
    }
}
