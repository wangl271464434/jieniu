package com.jieniuwuliu.jieniu.peisongyuan;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class PeisongHomeActivity extends BaseActivity {

    private Fragment psyHome,psyPage;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_peisong_home;
    }

    @Override
    protected void init() {
        psyHome = new PsyHomeFragment();
        getFragment(psyHome);
    }
    private void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.context, fragment).addToBackStack(null).commitAllowingStateLoss();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){finish();}
        return super.onKeyDown(keyCode, event);
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
