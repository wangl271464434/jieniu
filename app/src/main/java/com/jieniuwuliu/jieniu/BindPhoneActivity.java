package com.jieniuwuliu.jieniu;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LoginBean;
import com.jieniuwuliu.jieniu.messageEvent.WeChatEvent;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.RegularUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.TimeCountUtil;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    private String phone,code,openid,unionid;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        openid = getIntent().getStringExtra("openid");
        unionid = getIntent().getStringExtra("unionid");
    }
    @OnClick({R.id.layout_back, R.id.tv_code, R.id.tv_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.tv_code:
                phone = etPhone.getText().toString();
                if (phone.isEmpty()){
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
            case R.id.tv_bind:
                code = etCode.getText().toString();
                if (code.isEmpty()||phone.isEmpty()){
                    MyToast.show(getApplicationContext(),"手机号或者验证码不能为空");
                    return;
                }
                weChatLogin();
                break;
        }
    }
    /**
     * 获取验证码
     */
    private void getPhoneCode() {
        loading.show();
        Call<ResponseBody> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).code(phone,"3");
        observable.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                switch (response.code()){
                    case 200:
                        MyToast.show(BindPhoneActivity.this, "验证码已发送，请注意查收");
                        break;
                    case 400:
                        try{
                            String json = response.errorBody().string();
                            MyToast.show(getApplicationContext(),new JSONObject(json).getString("data"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
            }
        });
    }
    /**
     * 上传微信信息
     * */
    private void weChatLogin() {
        loading.show();
        Call<LoginBean> call = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).weChatBindPhone(openid,unionid,phone,code);
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
                        SPUtil.put(getApplicationContext(), Constant.LOGINTYPE, Constant.LOGINTYPE, 3);
                        Intent intent = new Intent();
                        intent.putExtra("weChatBind",true);
                        setResult(RESULT_OK,intent);
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
}
