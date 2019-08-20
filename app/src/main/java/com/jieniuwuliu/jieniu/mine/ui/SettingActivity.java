package com.jieniuwuliu.jieniu.mine.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.LoginActivity;
import com.jieniuwuliu.jieniu.MainActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.APKVersionCodeUtils;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.UpdateManager;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Version;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.point)
    TextView point;
    private String token;
    private String localVersion = "";
    private Version version;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        title.setText(R.string.setting);
        localVersion = APKVersionCodeUtils.getVersionName(this) + "." + APKVersionCodeUtils.getVersionCode(this);
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        checkVerSion();
    }

    private void checkVerSion() {
        Call<Version> call = HttpUtil.getInstance().getApi(token).checkVersion();
        call.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                version = response.body();
                if (!version.getData().getVersion().equals(localVersion)) {
                    point.setVisibility(View.VISIBLE);
                } else {
                    point.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.back, R.id.exit_btn, R.id.layout_tab1, R.id.layout_tab3,
            R.id.layout_tab6, R.id.layout_tab7})
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
                if (!version.getData().getVersion().equals(localVersion)){
                    showCheck(version.getData());
                }else{
                    MyToast.show(this,"已是最新版本");
                }
                break;
            case R.id.exit_btn://退出
                exit();
                break;
        }
    }
    /**
     * 版本更新提示
     * */
    @SuppressLint("SetTextI18n")
    private void showCheck(Version.Data data) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setBackgroundDrawableResource(R.drawable.bg_white_shape);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.8);
        window.setAttributes(params);
        dialog.setContentView(R.layout.check_update_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvVersion = dialog.findViewById(R.id.tv_version);
        TextView tvSize = dialog.findViewById(R.id.tv_size);
        TextView tvInfo = dialog.findViewById(R.id.tv_info);
        TextView tvSure = dialog.findViewById(R.id.tv_sure);
        tvVersion.setText("最新版本："+data.getVersion());
        tvSize.setText("新版本大小："+data.getTotal());
        tvInfo.setText(data.getInfom());
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                UpdateManager updateManager = new UpdateManager(SettingActivity.this);
                updateManager.checkUpdateInfo();
            }
        });
    }
    /**
     * 退出登录
     */
    private void exit() {
        SPUtil.put(this, Constant.TOKEN, Constant.TOKEN, "");
        startAcy(LoginActivity.class);
        MainActivity.activity.finish();
        finish();
    }

}
