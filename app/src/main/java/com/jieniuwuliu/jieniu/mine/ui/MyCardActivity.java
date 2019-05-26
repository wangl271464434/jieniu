package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 月卡
 */
public class MyCardActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn)
    Button btn;
    private String token;
    private UserBean.DataBean user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_card;
    }

    @Override
    protected void init() {
        title.setText("月卡服务");
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 获取信息
     */
    private void getData() {
        Call<UserBean> call = HttpUtil.getInstance().getApi(token).getUserInfo();
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                switch (response.code()) {
                    case 200://成功
                        user = response.body().getData();
                        if (user.isVip()) {
                            tvTime.setVisibility(View.VISIBLE);
                            btn.setVisibility(View.GONE);
                            tvTime.setText("有效期至："+user.getVipTime());
                        }else{
                            tvTime.setVisibility(View.GONE);
                            btn.setVisibility(View.VISIBLE);
                        }

                        break;
                    case 400://错误
                        try {
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getApplicationContext(), object.getString("msg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.back, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn:
                Intent intent = new Intent();
                intent.setClass(this,MonthCardPayActivity.class);
                if (user.getPersonType()==2){
                    intent.putExtra("money","300.00");
                }else{
                    intent.putExtra("money","700.00");
                }
               startActivity(intent);
                break;
        }
    }
}
