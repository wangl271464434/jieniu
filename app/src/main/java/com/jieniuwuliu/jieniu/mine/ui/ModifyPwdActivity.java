package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.LoginActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.UserBean;
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

public class ModifyPwdActivity extends BaseActivity {

    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_again_pwd)
    EditText etAgainPwd;
    @BindView(R.id.img_eye1)
    ImageView imgEye1;
    @BindView(R.id.img_eye2)
    ImageView imgEye2;
    @BindView(R.id.img_eye3)
    ImageView imgEye3;
    private String oldPwd, newPwd, againPwd;
    private boolean flag1 = false,flag2 = false,flag3=false;
    private MyLoading loading;
    private Intent intent;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
    }

    @OnClick({R.id.back, R.id.img_eye1, R.id.img_eye2, R.id.img_eye3,R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                oldPwd = etOldPwd.getText().toString();
                newPwd = etNewPwd.getText().toString();
                againPwd = etAgainPwd.getText().toString();
                if (oldPwd.isEmpty() || newPwd.isEmpty()) {
                    MyToast.show(ModifyPwdActivity.this, "旧密码/新密码不能为空");
                    return;
                }
                if (!newPwd.equals(againPwd)) {
                    MyToast.show(ModifyPwdActivity.this, R.string.pwd_error_notice);
                    return;
                }
                changePwd(oldPwd, newPwd);
                break;
            case R.id.img_eye1:
                if (flag1){//密码可见
                    etOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgEye1.setImageResource(R.mipmap.ic_setting_invisible);
                    etOldPwd.setSelection(etOldPwd.getText().length());
                    flag1 = false;
                }else{//密码不可见
                    etOldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgEye1.setImageResource(R.mipmap.ic_setting_visible);
                    etOldPwd.setSelection(etOldPwd.getText().length());
                    flag1 = true;
                }
                break;
            case R.id.img_eye2:
                if (flag2){//密码可见
                    etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgEye2.setImageResource(R.mipmap.ic_setting_invisible);
                    etNewPwd.setSelection(etNewPwd.getText().length());
                    flag2 = false;
                }else{//密码不可见
                    etNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgEye2.setImageResource(R.mipmap.ic_setting_visible);
                    etNewPwd.setSelection(etNewPwd.getText().length());
                    flag2 = true;
                }
                break;
            case R.id.img_eye3:
                if (flag3){//密码可见
                    etAgainPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgEye3.setImageResource(R.mipmap.ic_setting_invisible);
                    etAgainPwd.setSelection(etAgainPwd.getText().length());
                    flag3 = false;
                }else{//密码不可见
                    etAgainPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgEye3.setImageResource(R.mipmap.ic_setting_visible);
                    etAgainPwd.setSelection(etAgainPwd.getText().length());
                    flag3 = true;
                }
                break;
        }
    }

    /**
     * 修改密码
     */
    private void changePwd(String oldPwd, String newPwd) {
        loading.show();
        Map<String, Object> map = new HashMap<>();
        map.put("password", newPwd);
        map.put("oldPassword", oldPwd);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), GsonUtil.mapToJson(map));
        Call<UserBean> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).modifyUserInfo(body);
        observable.enqueue(new SimpleCallBack<UserBean>(ModifyPwdActivity.this) {
            @Override
            public void onSuccess(Response<UserBean> response) {
                loading.dismiss();
                int status = response.body().getStatus();
                if (status == 0) {
                    MyToast.show(ModifyPwdActivity.this, "修改密码成功");
                    intent = new Intent();
                    intent.setClass(ModifyPwdActivity.this,LoginActivity.class);
                    intent.putExtra("restart",true);
                    startActivity(intent);
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
                    MyToast.show(ModifyPwdActivity.this, object.getString("msg"));
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
    }

}
