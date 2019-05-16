package com.jieniuwuliu.jieniu.jijian;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.AliPayUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.AliPayResult;
import com.jieniuwuliu.jieniu.bean.PayResult;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
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
 * 支付类型选择
 */
public class PayTypeActivity extends BaseActivity {

    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.radio_btn1)
    RadioButton radioBtn1;
    @BindView(R.id.radio_btn2)
    RadioButton radioBtn2;
    @BindView(R.id.radio_btn3)
    RadioButton radioBtn3;
    private int payType = 1;//默认是微信支付
    private String token;
    private String orderNo;//订单号
    private int price;//价格单位分；
    private MyLoading loading;
    private static final int SDK_PAY_FLAG = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_type;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        radioBtn1.setChecked(true);
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        orderNo = getIntent().getStringExtra("orderNo");
        price = getIntent().getIntExtra("price",0);
        float a  = price/100;
        tvMoney.setText("¥ "+String.format("%.2f",a));
    }

    @OnClick({R.id.back, R.id.radio_btn1, R.id.radio_btn2,  R.id.radio_btn3,R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.radio_btn1://微信
                payType = 1;
                radioBtn1.setChecked(true);
                radioBtn2.setChecked(false);
                radioBtn3.setChecked(false);
                break;
            case R.id.radio_btn2://支付宝
                payType = 2;
                radioBtn1.setChecked(false);
                radioBtn2.setChecked(true);
                radioBtn3.setChecked(false);
                break;
            case R.id.radio_btn3://货到付款
                payType = 3;
                radioBtn1.setChecked(false);
                radioBtn2.setChecked(false);
                radioBtn3.setChecked(true);
                break;
            case R.id.btn_sure:
                switch (payType){
                    case 1:
                        wxPay();
                        break;
                    case 2:
                        zfbPay();
                        break;
                    case 3:
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
        Call<AliPayResult> call = HttpUtil.getInstance().getApi(token).getAliInfo("0.01",orderNo,"快递费用");
        call.enqueue(new Callback<AliPayResult>() {
            @Override
            public void onResponse(Call<AliPayResult> call, Response<AliPayResult> response) {
                loading.dismiss();
                try {
                    switch (response.code()){
                        case 200:
                           String privateKey = response.body().getData().getPrivateKey();
                           String appId = response.body().getData().getAppid();
                           String info = response.body().getData().getValue();
                           String orderInfo =  AliPayUtil.pay(privateKey,info);
                            aliPay(orderInfo);
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            Log.w("result",s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getApplicationContext(), object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AliPayResult> call, Throwable t) {
                loading.dismiss();
                MyToast.show(getApplicationContext(), "网络连接失败，请检查网络");
            }
        });
    }
    @SuppressLint("CheckResult")
    void aliPay(final String payInfo) {
        Observable.create(new ObservableOnSubscribe<Map<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, String>> emitter) throws Exception {
                PayTask alipay = new PayTask(PayTypeActivity.this);
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
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        }
                    }
                });
    }
}