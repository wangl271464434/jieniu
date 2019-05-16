package com.jieniuwuliu.jieniu;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.RegularUtil;
import com.jieniuwuliu.jieniu.Util.TimeCountUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.CodeBean;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_again)
    EditText etPwdAgain;
    @BindView(R.id.img_eye1)
    ImageView imgEye1;
    @BindView(R.id.img_eye2)
    ImageView imgEye2;
    private String phone,code,pwd,pwdAgain;
    private boolean flag1 = false,flag2 = false;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
    }

    @OnClick({R.id.back, R.id.tv_code,R.id.img_eye1, R.id.img_eye2, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_code:
                phone = etPhone.getText().toString();
                if (phone.isEmpty()){
                    MyToast.show(this,R.string.phone_empty_notice);
                    return;
                }
                if (!RegularUtil.isMobileNO(phone)){
                    MyToast.show(this,R.string.phone_error_notice);
                    return;
                }
                TimeCountUtil.countDown(this,tvCode,60*1000,1000,"重新获取");
                getPhoneCode(phone);
                break;
            case R.id.btn:
                phone = etPhone.getText().toString();
                code = etCode.getText().toString();
                pwd = etPwd.getText().toString();
                pwdAgain = etPwdAgain.getText().toString();
                if (phone.isEmpty()||pwd.isEmpty()||code.isEmpty()){//非空判断
                    MyToast.show(ForgetPwdActivity.this,"手机号/密码/验证码不能为空！！！");
                    return;
                }
                if (!pwd.equals(pwdAgain)){//判断两次输入的密码是否相等
                    MyToast.show(ForgetPwdActivity.this,R.string.pwd_error_notice);
                    return;
                }
                forgetPwd(phone,pwd,code);
                break;
            case R.id.img_eye1://显示密码
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
        }
    }
    /**
     * 忘记密码
     * */
    private void forgetPwd(String phone, String pwd, String code) {
        loading.show();
        Map<String, Object> map = new HashMap();
        map.put("phone", phone);
        map.put("password", pwd);
        map.put("code", code);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), GsonUtil.mapToJson(map));
        Call<ResponseBody> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).forgetPwd(body);
        observable.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                switch (response.code()) {
                    case 200:
                        MyToast.show(ForgetPwdActivity.this, "修改成功");
                        finish();
                        break;
                    case 400:
                        try {
                            String s = response.errorBody().string();
                            Log.w("result",s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(ForgetPwdActivity.this, object.getString("msg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.w("error", t.toString());
                loading.dismiss();
                MyToast.show(getApplicationContext(),"网络请求错误，请再试一次");
            }
        });
    }

    //获取验证码
    private void getPhoneCode(String phone) {
        loading.show();
        Call<CodeBean> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).code(phone);
        observable.enqueue(new Callback<CodeBean>() {
            @Override
            public void onResponse(Call<CodeBean> call, Response<CodeBean> response) {
                loading.dismiss();
                int status = response.body().getStatus();
                if (status == 0){
                    MyToast.show(ForgetPwdActivity.this,"验证码已发送，请注意查收");
                }
            }
            @Override
            public void onFailure(Call<CodeBean> call, Throwable t) {
                loading.dismiss();
            }
        });
    }
}
