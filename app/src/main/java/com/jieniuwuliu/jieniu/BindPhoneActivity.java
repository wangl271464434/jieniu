package com.jieniuwuliu.jieniu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.CodeBean;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.RegularUtil;
import com.jieniuwuliu.jieniu.util.TimeCountUtil;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private String phone,code;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
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
                break;
        }
    }
    /**
     * 获取验证码
     */
    private void getPhoneCode() {
        loading.show();
        Call<CodeBean> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).code(phone,"4");
        observable.enqueue(new Callback<CodeBean>() {
            @Override
            public void onResponse(Call<CodeBean> call, Response<CodeBean> response) {
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
            public void onFailure(Call<CodeBean> call, Throwable t) {
                loading.dismiss();
            }
        });
    }
}
