package com.jieniuwuliu.jieniu.mine.ui;

import android.view.View;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.LoginActivity;
import com.jieniuwuliu.jieniu.MainActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 设置
 * */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        title.setText(R.string.setting);
    }


    @OnClick({R.id.back, R.id.exit_btn,R.id.layout_tab1,R.id.layout_tab3,
           R.id.layout_tab6,R.id.layout_tab7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back://返回
                finish();
                break;
            case R.id.layout_tab1://门店认证
                startAcy(StoreCertifyActivity.class);
                break;
            case R.id.layout_tab3://修改密码
                startAcy(ModifyPwdActivity.class);
                break;
            case R.id.layout_tab6://关于捷牛
                startAcy(AboutActivity.class);
                break;
            case R.id.layout_tab7://检测新版本
                /***** 检查更新 *****/
                Beta.checkUpgrade();
                break;
            case R.id.exit_btn://退出
                exit();
                break;
        }
    }
    /**
     * 退出登录
     * */
    private void exit() {
        SPUtil.put(this,Constant.TOKEN,Constant.TOKEN,"");
        startAcy(LoginActivity.class);
        MainActivity.activity.finish();
        finish();
    }
}
