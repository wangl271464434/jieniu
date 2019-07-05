package com.jieniuwuliu.jieniu.mine.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.AliPayUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.Util.TimeUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.AliPayResult;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.PayResult;
import com.jieniuwuliu.jieniu.jijian.PayTypeActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 月卡支付
 */
public class MonthCardPayActivity extends BaseActivity {
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.radio_btn1)
    RadioButton radioBtn1;
    @BindView(R.id.radio_btn2)
    RadioButton radioBtn2;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private String token;
    private int payType = 1;
    private MyLoading loading;
    private String money;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_month_card_pay;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        money = getIntent().getStringExtra("money");
        tvMoney.setText("¥ "+money);
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
    }

    @OnClick({R.id.back, R.id.radio_btn1, R.id.radio_btn2, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.radio_btn1:
                payType = 1;
                radioBtn1.setChecked(true);
                radioBtn2.setChecked(false);
                break;
            case R.id.radio_btn2:
                payType = 2;
                radioBtn1.setChecked(false);
                radioBtn2.setChecked(true);
                break;
            case R.id.btn_sure:
                switch (payType){
                    case 1:
                        MyToast.show(this,"请选择支付宝支付");
//                        wxPay();
                        break;
                    case 2:
                        zfbPay();
                        break;
                }
                break;
        }
    }
    /**
     * 微信支付
     * */
    private void wxPay() {
    }
    /**
     * 支付宝支付
     * */
    private void zfbPay() {
        loading.show();
        Call<AliPayResult> call = HttpUtil.getInstance().getApi(token).getAliInfo(money,"",Constant.MONTH_CARD);
        call.enqueue(new SimpleCallBack<AliPayResult>(MonthCardPayActivity.this) {
            @Override
            public void onSuccess(Response<AliPayResult> response) {
                loading.dismiss();
             /*   String privateKey = response.body().getData().getPrivateKey();
                String appId = response.body().getData().getAppid();
                String notify = response.body().getData().getNotify();
                String order_no = response.body().getData().getOut_trade_no();
                Map<String,String> map = AliPayUtil.buildOrderParamMap(appId,Constant.MONTH_CARD,money,order_no,notify);
                String orderParam = AliPayUtil.buildOrderParam(map);
                String sign =  AliPayUtil.pay(privateKey,map);
                String orderInfo = orderParam +"&"+sign;*/
                String orderInfo = response.body().getData().getAuthInfo();
                aliPay(orderInfo);
            }

            @Override
            public void onFail(int errorCode, Response<AliPayResult> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                }catch (IOException e){
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
    @SuppressLint("CheckResult")
    void aliPay(final String payInfo) {
        Observable.create(new ObservableOnSubscribe<Map<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, String>> emitter) throws Exception {
                PayTask alipay = new PayTask(MonthCardPayActivity.this);
                emitter.onNext(alipay.payV2(payInfo, true));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Map<String, String>>() {
                    @Override
                    public void accept(Map<String, String> stringStringMap) throws Exception {
                        PayResult aliPayResult = new PayResult(stringStringMap);
                        String resultInfo = aliPayResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = aliPayResult.getResultStatus();
                        if (TextUtils.equals(resultStatus, "9000")) {
                            finish();
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        }
                    }
                });
    }
}
