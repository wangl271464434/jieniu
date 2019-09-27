package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.LoginActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class SetPwdActivity extends BaseActivity {
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.img_eye1)
    ImageView imgEye1;
    @BindView(R.id.et_pwd_again)
    EditText etPwdAgain;
    @BindView(R.id.img_eye2)
    ImageView imgEye2;
    private boolean flag1 = false,flag2 = false;
    private MyLoading loading;
    private String phone,pwd,pwdAgain,token;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        phone = (String) SPUtil.get(this, Constant.PHONE,Constant.PHONE,"");
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        String str = phone.substring(0,3)+"******"+phone.substring(8,11);
        tvInfo.setText("您的登录账号为："+str);
    }
    @OnClick({R.id.layout_back, R.id.img_eye1, R.id.img_eye2, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.img_eye1:
                if (flag1){//密码可见
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgEye1.setImageResource(R.mipmap.ic_setting_invisible);
                    etPwd.setSelection(etPwd.getText().length());
                    flag1 = false;
                }else{//密码不可见
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgEye1.setImageResource(R.mipmap.ic_setting_visible);
                    etPwd.setSelection(etPwd.getText().length());
                    flag1 = true;
                }
                break;
            case R.id.img_eye2:
                if (flag2){//密码可见
                    etPwdAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgEye2.setImageResource(R.mipmap.ic_setting_invisible);
                    etPwdAgain.setSelection(etPwdAgain.getText().length());
                    flag2 = false;
                }else{//密码不可见
                    etPwdAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgEye2.setImageResource(R.mipmap.ic_setting_visible);
                    etPwdAgain.setSelection(etPwdAgain.getText().length());
                    flag2 = true;
                }
                break;
            case R.id.sure:
                pwd = etPwd.getText().toString();
                pwdAgain = etPwdAgain.getText().toString();
                if (pwd.isEmpty()){
                    MyToast.show(getApplicationContext(), "密码不能为空");
                    return;
                }
                if (!pwd.equals(pwdAgain)) {
                    MyToast.show(getApplicationContext(), R.string.pwd_error_notice);
                    return;
                }
                changePwd(pwd);
                break;
        }
    }

    private void changePwd(String pwd) {
        loading.show();
        try{
            JSONObject object = new JSONObject();
            object.put("password",pwd);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Call<UserBean> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).modifyUserInfo(body);
            observable.enqueue(new SimpleCallBack<UserBean>(SetPwdActivity.this) {
                @Override
                public void onSuccess(Response<UserBean> response) {
                    loading.dismiss();
                    int status = response.body().getStatus();
                    if (status == 0) {
                        MyToast.show(SetPwdActivity.this, "设置密码成功");
                        finish();
                    }
                }

                @Override
                public void onFail(int errorCode, Response<UserBean> response) {
                    loading.dismiss();
                    try {
                        String s = response.errorBody().string();
                        Log.w("result", s);
                        JSONObject object = new JSONObject(s);
                        MyToast.show(SetPwdActivity.this, object.getString("msg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNetError(String s) {
                    loading.dismiss();
                    MyToast.show(getApplicationContext(),s);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }
}
