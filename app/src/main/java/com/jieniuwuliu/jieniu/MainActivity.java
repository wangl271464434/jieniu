package com.jieniuwuliu.jieniu;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.fragment.HomeFragment;
import com.jieniuwuliu.jieniu.fragment.LunTanFragment;
import com.jieniuwuliu.jieniu.fragment.MineFragment;
import com.jieniuwuliu.jieniu.fragment.QiPeiShangFragment;
import com.jieniuwuliu.jieniu.jijian.JiJianActivity;
import com.jieniuwuliu.jieniu.service.SocketService;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity{
    @BindView(R.id.home)
    RadioButton home;
    @BindView(R.id.luntan)
    RadioButton luntan;
    @BindView(R.id.qipeishang)
    RadioButton qipeishang;
    @BindView(R.id.mine)
    RadioButton mine;
    private Fragment homeFragment,mineFragment,qipeishangFragment,luntanFragment;
    private Intent intent;
    private int status;
    public static Activity activity;
    private MsgReceiver receiver;
    public static Badge badge;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        activity = this;
        badge = new QBadgeView(this).bindTarget(luntan);
        //启动接收推送服务
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        //接收消息广播
        receiver = new MsgReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("jieniu.msg");
        registerReceiver(receiver, filter);
        homeFragment = new HomeFragment();
        getFragment(homeFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){finish();}
        return super.onKeyDown(keyCode, event);
    }
    @OnClick({R.id.home, R.id.qipeishang,R.id.jijian, R.id.luntan, R.id.mine})
    public void onViewClicked(View view) {
        status = (int) SPUtil.get(this,Constant.ISCERTIFY,Constant.ISCERTIFY,0);
        switch (view.getId()) {
            case R.id.home://首页
                home.setChecked(true);
                qipeishang.setChecked(false);
                luntan.setChecked(false);
                mine.setChecked(false);
                if (homeFragment==null){
                    homeFragment = new HomeFragment();
                }
                getFragment(homeFragment);
                break;
            case R.id.qipeishang://汽配商
                home.setChecked(false);
                qipeishang.setChecked(true);
                luntan.setChecked(false);
                mine.setChecked(false);
                if (status != 1){
                    MyToast.show(MainActivity.this,"请去进行认证");
                    return;
                }
                if (qipeishangFragment == null){
                    qipeishangFragment = new QiPeiShangFragment();
                }
                getFragment(qipeishangFragment);
                break;
            case R.id.jijian://寄件
                if (status != 1){
                    MyToast.show(MainActivity.this,"请去进行认证");
                    return;
                }
                setDialog();
                break;
            case R.id.luntan://论坛
                home.setChecked(false);
                qipeishang.setChecked(false);
                luntan.setChecked(true);
                mine.setChecked(false);
                if (status != 1){
                    MyToast.show(MainActivity.this,"请去进行认证");
                    return;
                }
                if (luntanFragment == null){
                    luntanFragment = new LunTanFragment();
                }
                getFragment(luntanFragment);
                break;
            case R.id.mine://我的
                home.setChecked(false);
                qipeishang.setChecked(false);
                luntan.setChecked(false);
                mine.setChecked(true);
                if (mineFragment == null){
                    mineFragment = new MineFragment();
                }
                getFragment(mineFragment);
                break;
        }
    }
    /**寄件弹框**/
    private void setDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.pop_jijian, null);
        TextView tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        TextView tvTab1 = viewDialog.findViewById(R.id.tv_tab1);
        TextView tvTab2 = viewDialog.findViewById(R.id.tv_tab2);
        tvTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setClass(MainActivity.this,JiJianActivity.class);
                intent.putExtra("type","上门取件");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        tvTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setClass(MainActivity.this,JiJianActivity.class);
                intent.putExtra("type","服务点自寄");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        dialog.setContentView(viewDialog, layoutParams);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    /**
     * 广播接收器
     * */
    class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("jieniu.msg")){
                badge.setBadgeNumber(-1);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
