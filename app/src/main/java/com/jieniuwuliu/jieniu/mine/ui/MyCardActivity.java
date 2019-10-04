package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.util.TimeUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
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
    private long time = 3*24*60*60*1000;
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
        call.enqueue(new SimpleCallBack<UserBean>(MyCardActivity.this) {
            @Override
            public void onSuccess(Response<UserBean> response) {
                user = response.body().getData();
                if (user.isVip()) {
                    tvTime.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.GONE);
                    tvTime.setText("有效期至："+user.getVipTime());
                    if (TimeUtil.getMiliSecond(user.getVipTime()) - System.currentTimeMillis() <= time){
                        btn.setVisibility(View.VISIBLE);
                        btn.setText("立即续费");
                    }
                }else{
                    tvTime.setVisibility(View.GONE);
                    btn.setVisibility(View.VISIBLE);
                    btn.setText("立即办理");
                }
            }

            @Override
            public void onFail(int errorCode, Response<UserBean> response) {
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                MyToast.show(getApplicationContext(),s);
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
                MyToast.show(getApplicationContext(),"请您前往线下办理月卡业务……");
                break;
        }
    }
}
