package com.jieniuwuliu.jieniu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LoginBean;
import com.jieniuwuliu.jieniu.bean.WeChatInfo;
import com.jieniuwuliu.jieniu.bean.WeChatToken;
import com.jieniuwuliu.jieniu.messageEvent.WeChatEvent;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.RegularUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.TimeCountUtil;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.phone)
    EditText etPhone;
    @BindView(R.id.pwd)
    EditText etPwd;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.img_eye)
    ImageView imgEye;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_login_type)
    TextView tvLoginType;
    @BindView(R.id.layout_pwd)
    RelativeLayout layoutPwd;
    @BindView(R.id.layout_code)
    RelativeLayout layoutCode;
    private boolean flag = false;
    private boolean isCode = true;
    private MyLoading loading;
    private boolean isRestart;
    private String phone, pwd, code,openid,unionid,name;
    private IWXAPI api;
    private int loginType;//1是账号登录 2是验证码登录 3是微信登录

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        isRestart = getIntent().getBooleanExtra("restart", false);
        loading = new MyLoading(this, R.style.CustomDialog);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginType = 2;
        tvLoginType.setText("账号登录");
        layoutPwd.setVisibility(View.GONE);
        layoutCode.setVisibility(View.VISIBLE);
        tvForget.setVisibility(View.GONE);
        phone = (String) SPUtil.get(this, Constant.PHONE, Constant.PHONE, "");
        pwd = (String) SPUtil.get(this, Constant.PWD, Constant.PWD, "");
        if (!"".equals(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
            etPwd.setText(pwd);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WeChatEvent event) {
        Log.i("wechat", event.toString());
        if (!event.getCode().equals("")) {
            loading.show();
            getAccessToken(event.getCode());
        }
    }

    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.WXAPPID + "&secret=" + Constant.WXSERCET + "&code=" + code + "&grant_type=authorization_code";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                MyToast.show(getApplicationContext(), "获取accesstoken失败");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                WeChatToken weChatToken = (WeChatToken) GsonUtil.praseJsonToModel(s, WeChatToken.class);
                openid =  weChatToken.getOpenid();
                getWeChatInfo(weChatToken.getAccess_token());
            }
        });
    }

    private void getWeChatInfo(String access_token) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.i("wechatInfo","wechatInfo:获取个人信息失败");
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String s = response.body().string();
                WeChatInfo info = (WeChatInfo) GsonUtil.praseJsonToModel(s, WeChatInfo.class);
                Log.i("wechatinfo", info.toString());
                unionid = info.getUnionid();
                name = info.getNickname();
                checkWeChat();
            }
        });
    }
    /**
     * 检测是否需要绑定手机
     * */
    private void checkWeChat() {
        Call<LoginBean> call = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).weChatLogin(openid,unionid);
        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                loading.dismiss();
                try{
                    if (response.code()==200){
                        //账号
                        SPUtil.put(getApplicationContext(), Constant.PHONE, Constant.PHONE, response.body().getData().getPhone());
                        //token
                        SPUtil.put(getApplicationContext(), Constant.TOKEN, Constant.TOKEN, response.body().getToken());
                        //是否认证
                        SPUtil.put(getApplicationContext(), Constant.ISCERTIFY, Constant.ISCERTIFY, response.body().getData().getAuth());
                        //用户类型
                        SPUtil.put(getApplicationContext(), Constant.USERTYPE, Constant.USERTYPE, response.body().getData().getPersonType());
                        SPUtil.put(getApplicationContext(), Constant.LOGINTYPE, Constant.LOGINTYPE, 3);
                        startAcy(MainActivity.class);
                        finish();
                    }else if (response.code() == 404){
                        Intent intent = new Intent(LoginActivity.this,BindPhoneActivity.class);
                        intent.putExtra("openid",openid);
                        intent.putExtra("unionid",unionid);
                        intent.putExtra("name",name);
                        startActivityForResult(intent,1000);
                    }else{
                        String s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        MyToast.show(getApplicationContext(), object.getString("msg"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                loading.dismiss();
                MyToast.show(getApplicationContext(), getResources().getString(R.string.net_fail));
            }
        });
    }

    @OnClick({R.id.back, R.id.tv_register, R.id.img_eye, R.id.tv_code, R.id.tv_login_type, R.id.tv_forget, R.id.login, R.id.img_weChat, R.id.tv_phone, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_register://注册
                startAcy(RegisterActivity.class);
                break;
            case R.id.tv_forget://忘记密码
                startAcy(ForgetPwdActivity.class);
                break;
            case R.id.login://登录
                phone = etPhone.getText().toString();
                pwd = etPwd.getText().toString();
                code = etCode.getText().toString();
                switch (loginType){
                    case 1://密码登录
                        if (phone.isEmpty() || pwd.isEmpty()) {
                            MyToast.show(this, "手机号或密码不能为空");
                            return;
                        }
                        if (phone.length() > 11) {
                            MyToast.show(this, "请输入正确的手机格式");
                            return;
                        }
                        login();
                        break;
                    case 2://验证码登录
                        if (phone.isEmpty() || code.isEmpty()) {
                            MyToast.show(this, "手机号或验证码不能为空");
                            return;
                        }
                        if (phone.length() > 11) {
                            MyToast.show(this, "请输入正确的手机格式");
                            return;
                        }
                        codeLogin();
                        break;
                }
                break;
            case R.id.tv_code://获取验证码
                phone = etPhone.getText().toString();
                if (phone.isEmpty()) {
                    MyToast.show(this, R.string.phone_empty_notice);
                    return;
                }
                if (!RegularUtil.isMobileNO(phone)) {
                    MyToast.show(this, R.string.phone_error_notice);
                    return;
                }
                TimeCountUtil.countDown(this, tvCode, 60 * 1000, 1000, "重新获取");
                getPhoneCode();
                break;
            case R.id.tv_login_type://验证码和账号切换
                if (isCode){
                    loginType = 1;
                    isCode = false;
                    tvLoginType.setText("验证码登录");
                    layoutCode.setVisibility(View.GONE);
                    layoutPwd.setVisibility(View.VISIBLE);
                    tvForget.setVisibility(View.VISIBLE);
                }else{
                    loginType = 2;
                    isCode = true;
                    tvLoginType.setText("账号登录");
                    layoutPwd.setVisibility(View.GONE);
                    layoutCode.setVisibility(View.VISIBLE);
                    tvForget.setVisibility(View.GONE);
                }
                break;
            case R.id.img_weChat:
                loginType = 3;
                Constant.SHARETYPE = 3;
                loginWeChat();
                break;
            case R.id.tv_agreement://协议
                startAcy(YinSIActivity.class);
                break;
            case R.id.img_eye:
                if (flag) {//密码可见
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgEye.setImageResource(R.mipmap.ic_setting_invisible);
                    etPwd.setSelection(etPwd.getText().length());
                    flag = false;
                } else {//密码不可见
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgEye.setImageResource(R.mipmap.ic_setting_visible);
                    etPwd.setSelection(etPwd.getText().length());
                    flag = true;
                }
                break;
            case R.id.tv_phone:
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                        return;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + tvPhone.getText().toString());
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }
    /**
     * 验证码登录
     * */
    private void codeLogin() {
        loading.show();
        Call<LoginBean> call = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).weChatBindPhone("","",phone,code,"");
        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                loading.dismiss();
                try{
                    if (response.code()==200){
                        //账号
                        SPUtil.put(getApplicationContext(), Constant.PHONE, Constant.PHONE, phone);
                        //token
                        SPUtil.put(getApplicationContext(), Constant.TOKEN, Constant.TOKEN, response.body().getToken());
                        //是否认证
                        SPUtil.put(getApplicationContext(), Constant.ISCERTIFY, Constant.ISCERTIFY, response.body().getData().getAuth());
                        //用户类型
                        SPUtil.put(getApplicationContext(), Constant.USERTYPE, Constant.USERTYPE, response.body().getData().getPersonType());
                        SPUtil.put(getApplicationContext(), Constant.LOGINTYPE, Constant.LOGINTYPE, 2);
                        startAcy(MainActivity.class);
                        finish();
                    }else{
                        String s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        MyToast.show(getApplicationContext(), object.getString("msg"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                loading.dismiss();
                MyToast.show(getApplicationContext(), getResources().getString(R.string.net_fail));
            }


        });
    }

    //验证码登录获取验证码
    private void getPhoneCode() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).code(phone,"4");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    switch (response.code()){
                        case 200:
                            Log.i("codeLogin",response.body().string());
                            MyToast.show(LoginActivity.this, "验证码已发送，请注意查收");
                            break;
                        case 400:
                            String json = response.errorBody().string();
                            Log.i("codeLogin",json);
                            MyToast.show(getApplicationContext(),new JSONObject(json).getString("data"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Log.i("codeLogin",t.toString());
                MyToast.show(getApplicationContext(), getResources().getString(R.string.net_fail));
            }
        });
    }

    /**
     * 微信登录
     */
    private void loginWeChat() {
        loading.show();
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, Constant.WXAPPID, true);
        }
        if (!api.isWXAppInstalled()) {
            MyToast.show(this, "请您安装微信客户端！");
            loading.dismiss();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_login_duzun";
        api.sendReq(req);
    }

    /**
     * 登录方法
     */
    private void login() {
        loading.show();
        Map<String, Object> map = new HashMap();
        map.put("phone", phone);
        map.put("password", pwd);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<LoginBean> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).login(body);
        observable.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                loading.dismiss();
                try {
                    switch (response.code()) {
                        case 200:
                            if (response.body().getStatus() == 0) {
                                //账号
                                SPUtil.put(getApplicationContext(), Constant.PHONE, Constant.PHONE, phone);
                                //密码
                                SPUtil.put(getApplicationContext(), Constant.PWD, Constant.PWD, pwd);
                                //token
                                SPUtil.put(getApplicationContext(), Constant.TOKEN, Constant.TOKEN, response.body().getToken());
                                //是否认证
                                SPUtil.put(getApplicationContext(), Constant.ISCERTIFY, Constant.ISCERTIFY, response.body().getData().getAuth());
                                //用户类型
                                SPUtil.put(getApplicationContext(), Constant.USERTYPE, Constant.USERTYPE, response.body().getData().getPersonType());
                                //登录方式
                                SPUtil.put(getApplicationContext(), Constant.LOGINTYPE, Constant.LOGINTYPE, 1);
                                if (response.body().getData().getPersonType() == 5 || response.body().getData().getPersonType() == 6) {
                                    MyToast.show(LoginActivity.this, "用户名或者密码错误");
                                } else {
                                    if (isRestart) {
                                        finish();
                                    } else {
                                        startAcy(MainActivity.class);
                                        finish();
                                    }
                                }
                            }
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(LoginActivity.this, "用户名或者密码错误");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MyToast.show(getApplicationContext(), e.toString());
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                loading.dismiss();
                MyToast.show(getApplicationContext(), getResources().getString(R.string.net_fail));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (requestCode == 1000){//微信绑定手机的返回
                boolean code = data.getBooleanExtra("weChatBind",false);
                if (code){
                    startAcy(MainActivity.class);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
