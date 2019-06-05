package com.jieniuwuliu.jieniu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LoginBean;
import com.jieniuwuliu.jieniu.peisongyuan.PeisongHomeActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    private boolean flag = false;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
    }

    @OnClick({R.id.back, R.id.tv_register,R.id.img_eye, R.id.tv_forget, R.id.login,R.id.tv_phone, R.id.tv_agreement})
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
                String phone = etPhone.getText().toString();
                String pwd = etPwd.getText().toString();
                if (phone.isEmpty() || pwd.isEmpty()) {
                    MyToast.show(this, "手机号或密码不能为空");
                    return;
                }
                login(phone, pwd);
                break;
            case R.id.tv_agreement://协议
                startAcy(YinSIActivity.class);
                break;
            case R.id.img_eye:
                if (flag){//密码可见
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgEye.setImageResource(R.mipmap.ic_setting_invisible);
                    etPwd.setSelection(etPwd.getText().length());
                    flag = false;
                }else{//密码不可见
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
                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 100);
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
     * 登录方法
     */
    private void login(String phone, String pwd) {
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
                                //token
                                SPUtil.put(getApplicationContext(), Constant.TOKEN, Constant.TOKEN, response.body().getToken());
                                //是否认证
                                SPUtil.put(getApplicationContext(), Constant.ISCERTIFY, Constant.ISCERTIFY, response.body().getData().getAuth());
                                //用户类型
                                SPUtil.put(getApplicationContext(), Constant.USERTYPE, Constant.USERTYPE, response.body().getData().getPersonType());
                                if (response.body().getData().getPersonType() == 5 ||response.body().getData().getPersonType() == 6) {
                                    MyToast.show(LoginActivity.this, "用户名或者密码错误");
                                } else {
                                    startAcy(MainActivity.class);
                                    finish();
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
                    MyToast.show(getApplicationContext(),e.toString());
                }
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),"网络连接失败，请重试");
            }
        });
    }
}
