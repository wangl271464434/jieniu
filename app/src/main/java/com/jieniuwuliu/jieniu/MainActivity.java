package com.jieniuwuliu.jieniu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jieniuwuliu.jieniu.adapter.TicketWuliuAdapter;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.bean.Coupon;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.messageEvent.CityEvent;
import com.jieniuwuliu.jieniu.mine.ui.MyTicketActivity;
import com.jieniuwuliu.jieniu.qipeishang.QPSListActivity;
import com.jieniuwuliu.jieniu.util.APKVersionCodeUtils;
import com.jieniuwuliu.jieniu.util.AppUtil;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.util.StringUtil;
import com.jieniuwuliu.jieniu.util.UpdateManager;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Notice;
import com.jieniuwuliu.jieniu.bean.Version;
import com.jieniuwuliu.jieniu.fragment.HomeFragment;
import com.jieniuwuliu.jieniu.fragment.LunTanFragment;
import com.jieniuwuliu.jieniu.fragment.MineFragment;
import com.jieniuwuliu.jieniu.jijian.JiJianActivity;
import com.jieniuwuliu.jieniu.service.SocketService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements AMapLocationListener {
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
    private String token;
    private int userType;
    private String scoketService = "com.jieniuwuliu.jieniu.service.SocketService";
    private String localVersion ="";
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        activity = this;
        checkSDK();
        localVersion = APKVersionCodeUtils.getVersionName(this);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        userType = (int) SPUtil.get(this, Constant.USERTYPE, Constant.USERTYPE, 0);
        badge = new QBadgeView(this).bindTarget(luntan);
        //启动接收推送服务
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
//        MyToast.show(getApplicationContext(),"启动接受消息服务");
        //接收消息广播
        receiver = new MsgReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("jieniu.msg");
        registerReceiver(receiver, filter);
        homeFragment = new HomeFragment();
        getFragment(homeFragment);
        checkVerSion();
    }
    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 200);
            }else{
                location();
            }
        }else{
            location();
        }
    }
    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    private void checkVerSion() {
        Call<Version> call = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).checkVersion();
        call.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if (!localVersion.equals(response.body().getData().getVersion())){
                    showCheck(response.body().getData());
                }else{
                    couponDialog();
//                    getCoupon();
//                    getNotice();
                }
            }
            @Override
            public void onFailure(Call<Version> call, Throwable t) {

            }
        });
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
        dialog.setCancelable(false);
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
                UpdateManager updateManager = new UpdateManager(MainActivity.this);
                updateManager.checkUpdateInfo();
            }
        });
    }

    /**
     * 获取通告
     * */
    private void getNotice() {
        Call<Notice> call = HttpUtil.getInstance().getApi(token).getNotice();
        call.enqueue(new SimpleCallBack<Notice>(activity) {
            @Override
            public void onSuccess(Response<Notice> response) {
                try{ String info = response.body().getData().get(0).getInfo();
                    if (response.body().getData().get(0).isStatus()){ //判断是否弹出公告
                        showNotice(info);
                    }else{
                        getCoupon();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<Notice> response) {

            }

            @Override
            public void onNetError(String s) {

            }
        });
    }
    /**
     * 网络获取是否有需要领取的优惠券
     *
     * */
    private void getCoupon() {
/*        Call<Coupon> call = HttpUtil.getInstance().getApi(token).getCoupons();
        call.enqueue(new Callback<Coupon>() {
            @Override
            public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                if (response.body().getData().size()>0){
                    couponDialog(response.body().getData());
                }else{
                    getNotice();
                }
            }

            @Override
            public void onFailure(Call<Coupon> call, Throwable t) {

            }
        });*/

    }

    /**
     * 通告
     * */
    private void showNotice(String info) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setBackgroundDrawableResource(R.drawable.bg_white_shape);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.7);
        window.setAttributes(params);
        dialog.setContentView(R.layout.notice);
        dialog.setCanceledOnTouchOutside(true);
        ImageView img = dialog.findViewById(R.id.img_close);
        TextView textView = dialog.findViewById(R.id.tv_info);
        RelativeLayout layout = dialog.findViewById(R.id.notice);
        textView.setText(StringUtil.ToDBC(info));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        if (!AppUtil.isServiceRunning(this,scoketService)){
            Log.i("service","重新启动推送服务");
            Intent intent = new Intent(this, SocketService.class);
            startService(intent);
        }
    }
    //获取用户状态
    private void getUserInfo() {
        Call<UserBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getUserInfo();
        call.enqueue(new SimpleCallBack<UserBean>(this) {
            @Override
            public void onSuccess(Response<UserBean> response) {
                try{
                    if (response.body().getStatus() == 0){
                        //是否认证
                        SPUtil.put(MainActivity.this,Constant.ISCERTIFY,Constant.ISCERTIFY,response.body().getData().getAuth());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<UserBean> response) {
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    Log.e("getUserInfo",object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                Log.e("getUserInfo",s);
            }
        });
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commitAllowingStateLoss();
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
    /**
     *领取优惠券弹框
     *
     * @param data*/
    private void couponDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.8);
        window.setAttributes(params);
        dialog.setContentView(R.layout.dialog_ticket);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        Button btn = dialog.findViewById(R.id.btn);
        ImageView close = dialog.findViewById(R.id.img_close);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        List<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(1);
        TicketWuliuAdapter ticketWuliuAdapter = new TicketWuliuAdapter(this,data);
        recyclerView.setAdapter(ticketWuliuAdapter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                if (status == 3){
                    MyToast.show(MainActivity.this,"正在认证中");
                    return;
                }
                qpsDialogType();
               /* if (qipeishangFragment == null){
                    qipeishangFragment = new QiPeiShangFragment();
                }
                getFragment(qipeishangFragment);*/
                break;
            case R.id.jijian://寄件
                if (status != 1){
                    MyToast.show(MainActivity.this,"请去进行认证");
                    return;
                }
                if (status == 3){
                    MyToast.show(MainActivity.this,"正在认证中");
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
                if (status == 3){
                    MyToast.show(MainActivity.this,"正在认证中");
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
    /**汽配商弹框*/
    private void qpsDialogType(){
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
        dialog.setContentView(R.layout.dialog_qps);
        ImageView img1 = dialog.findViewById(R.id.img1);
        ImageView img2 = dialog.findViewById(R.id.img2);
        ImageView img3 = dialog.findViewById(R.id.img3);
        ImageView img4 = dialog.findViewById(R.id.img4);
        ImageView img5 = dialog.findViewById(R.id.img5);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                intent = new Intent();
                intent.setClass(MainActivity.this,MoreCarActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(MainActivity.this,MoreCarActivity.class);
                intent.putExtra("type",8);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(MainActivity.this,QPSListActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(MainActivity.this,QPSListActivity.class);
                intent.putExtra("type",9);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(MainActivity.this, QPSListActivity.class);
                intent.putExtra("type",4);
                startActivity(intent);
                dialog.dismiss();
            }
        });
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
                if (userType == 2){//判断是否是汽修厂
                    intent = new Intent();
                    intent.setClass(MainActivity.this,JiJianActivity.class);
                    intent.putExtra("type","上门取件");
                    startActivity(intent);
                    dialog.dismiss();
                }else{
                    MyToast.show(MainActivity.this,"只有汽修厂用户才能选择上门取件");
                }
            }
        });
        tvTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType != 2){//判断是否是汽修厂
                    intent = new Intent();
                    intent.setClass(MainActivity.this,JiJianActivity.class);
                    intent.putExtra("type","服务点自寄");
                    startActivity(intent);
                    dialog.dismiss();
                }else{
                    MyToast.show(MainActivity.this,"您是汽修厂用户不能选择服务点自寄");
                }

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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                Constant.PROVINCE = aMapLocation.getProvince();
                Constant.CITY = aMapLocation.getCity();
                CityEvent event = new CityEvent();
                event.setProvince(Constant.PROVINCE);
                event.setCity(Constant.CITY);
                EventBus.getDefault().post(event);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(this, "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0;i<permissions.length;i++){
            if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)){//判断是否有定位权限
                if (grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    location();
                }
            }
        }
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
