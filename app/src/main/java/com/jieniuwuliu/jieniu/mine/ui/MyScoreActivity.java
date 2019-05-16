package com.jieniuwuliu.jieniu.mine.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.LoginActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyScoreActivity extends BaseActivity {

    @BindView(R.id.score)
    TextView tvScore;
    @BindView(R.id.btn)
    Button btn;
    private int score;
    private String token;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_score;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        score = getIntent().getIntExtra("score",0);
        tvScore.setText(String.valueOf(score));
    }
    @OnClick({R.id.back, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn://兑换
                convertCoupon(1);
                break;
        }
    }
    /**
     * 兑换优惠券
     * */
    private void convertCoupon(int num) {
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).convertCoupon(num);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            String result = response.body().string();
                            JSONObject resultObject = new JSONObject(result);
                            MyToast.show(MyScoreActivity.this, resultObject.getString("msg"));
                            finish();
                            break;
                        case 400:
                            String error = response.errorBody().string();
                            JSONObject object = new JSONObject(error);
                            MyToast.show(MyScoreActivity.this, object.getString("msg"));
                            break;
                        default:
                            Log.w("error",response.errorBody().string());
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
