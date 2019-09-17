package com.jieniuwuliu.jieniu.mine.ui;

import android.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MyScoreActivity extends BaseActivity {

    @BindView(R.id.score)
    TextView tvScore;
    @BindView(R.id.btn)
    Button btn;
    private int score;
    private String token;
    private MyLoading loading;
    private int num = 1;
    private int total;//可兑换的张数
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_score;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
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
//                convertCoupon(1);
                total = score/100;
                if (total>0){
                    showCouponDialog();
                }else{
                    MyToast.show(getApplicationContext(),"积分不足");
                }
                break;
        }
    }
    /**
     * 通告
     * */
    private void showCouponDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setBackgroundDrawableResource(R.drawable.bg_white_shape);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.7);
        window.setAttributes(params);
        dialog.setContentView(R.layout.dialog_coupon);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvNum = dialog.findViewById(R.id.tv_num);
        TextView tvSure = dialog.findViewById(R.id.tv_sure);
        ImageView imgJia = dialog.findViewById(R.id.img_jia);
        ImageView imgJian = dialog.findViewById(R.id.img_jian);
        tvNum.setText(num+"");
        imgJia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num<total){
                    num++;
                    tvNum.setText(num+"");
                }else{
                    MyToast.show(getApplicationContext(),"已经到达最大张数");
                }
            }
        });
        imgJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num>1){
                    num--;
                    tvNum.setText(num+"");
                }else{
                    MyToast.show(getApplicationContext(),"兑换张数不得小于1张");
                }
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                convertCoupon(num);
            }
        });
    }
    /**
     * 兑换优惠券
     * */
    private void convertCoupon(int num) {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).convertCoupon(num);
        call.enqueue(new SimpleCallBack<ResponseBody>(MyScoreActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String result = response.body().string();
                    JSONObject resultObject = new JSONObject(result);
                    MyToast.show(MyScoreActivity.this, resultObject.getString("msg"));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String error = response.errorBody().string();
                    JSONObject object = new JSONObject(error);
                    MyToast.show(MyScoreActivity.this, object.getString("msg"));
                } catch (Exception e) {
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
