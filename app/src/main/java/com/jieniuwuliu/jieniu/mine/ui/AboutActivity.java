package com.jieniuwuliu.jieniu.mine.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.APKVersionCodeUtils;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于捷牛
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        title.setText("关于捷牛");
        String version = APKVersionCodeUtils.getVersionName(this);
        tvVersion.setText("版本号：V"+version);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
