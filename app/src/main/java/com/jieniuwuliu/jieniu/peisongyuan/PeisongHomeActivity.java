package com.jieniuwuliu.jieniu.peisongyuan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.AppUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class PeisongHomeActivity extends BaseActivity  {

    private Fragment psyHome,psyPage;
    private String localService = "com.jieniuwuliu.jieniu.service.MyService";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_peisong_home;
    }

    @Override
    protected void init() {
        checkSDK();
        psyHome = new PsyHomeFragment();
        getFragment(psyHome);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.context, fragment).addToBackStack(null).commitAllowingStateLoss();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 退出应用弹窗
     * */
    private void exitDialog() {
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
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(true);
        TextView yes = dialog.findViewById(R.id.tv_yes);
        TextView no = dialog.findViewById(R.id.tv_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    @OnClick({ R.id.btn_renwu, R.id.btn_wode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_renwu:
                if (psyHome==null){
                    psyHome = new PsyHomeFragment();
                }
                getFragment(psyHome);
                break;
            case R.id.btn_wode:
                if (psyPage == null){
                    psyPage = new PsyPageFragment();
                }
                getFragment(psyPage);
                break;
        }
    }


}
