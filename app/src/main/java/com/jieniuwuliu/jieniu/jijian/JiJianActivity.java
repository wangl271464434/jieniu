package com.jieniuwuliu.jieniu.jijian;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.jieniuwuliu.jieniu.FuWuActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Coupon;
import com.jieniuwuliu.jieniu.bean.Order;
import com.jieniuwuliu.jieniu.bean.OrderBean;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.messageEvent.WeightEvent;
import com.jieniuwuliu.jieniu.mine.ui.AddressListActivity;
import com.jieniuwuliu.jieniu.mine.ui.MyTicketActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 填写寄件信息
 */
public class JiJianActivity extends BaseActivity {
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_jianmian)
    TextView tvJianMian;
    @BindView(R.id.tv_baojia)
    TextView tvBaojia;
    @BindView(R.id.tv_fa_name)
    TextView tvFaName;
    @BindView(R.id.tv_fa_phone)
    TextView tvFaPhone;
    @BindView(R.id.tv_fa_address)
    TextView tvFaAddress;
    @BindView(R.id.layout_shou)
    LinearLayout layoutShou;
    @BindView(R.id.tv_shou_name)
    TextView tvShouName;
    @BindView(R.id.tv_shou_phone)
    TextView tvShouPhone;
    @BindView(R.id.tv_shou_address)
    TextView tvShouAddress;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_daishou)
    TextView tvDaishou;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.tv_psy)
    TextView tvPsy;
    @BindView(R.id.img_ji_vip)
    ImageView imgJiVip;
    @BindView(R.id.img_shou_vip)
    ImageView imgShouVip;
    private String type = "";
    private String weightType = "";
    private Coupon.DataBean data;
    private String token;
    private UserBean.DataBean user;
    private LatLng start,end;
    private int yunfei = 0;
    private int baojiaPrice = 0,weightPrice = 0,juliPrice = 0,numPrice=0,youhuiPrice = 0,daishouPrice = 0,baojiaMoney = 0;
    private boolean flag = false;//判断是否免运费
    private MyLoading loading;
    private String info = "";
    private int kuaidiId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ji_jian;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        type = getIntent().getStringExtra("type");
        tvType.setText(type);
        tvMoney.setText(""+yunfei);
        data = (Coupon.DataBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            if (data.getUseMoney() == 0) {
                tvJianMian.setText("免运费");
                flag = true;
                youhuiPrice = 0;
            } else {
                tvJianMian.setText("-" + data.getMoney() / 100);
                flag = false;
                youhuiPrice = data.getMoney()/100;
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getUserInfo();
    }

    private void getUserInfo() {
        Call<UserBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getUserInfo();
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                loading.dismiss();
                switch (response.code()){
                    case 200://成功
                        if (response.body().getStatus() == 0){
                            user = response.body().getData();
                            if (user.isVip()){
                                imgJiVip.setVisibility(View.VISIBLE);
                            }else {
                                imgJiVip.setVisibility(View.GONE);
                            }
                            tvFaName.setText(user.getNickname());
                            tvFaPhone.setText(user.getAddress().getPhone());
                            tvFaAddress.setText(user.getAddress().getAddress());
                            start = new LatLng(user.getAddress().getLat(),user.getAddress().getLng());
                            tvMoney.setText(""+getYunFeiPrice());
                            tvTotalMoney.setText(""+getTotalPrice());
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
                loading.dismiss();
                MyToast.show(getApplicationContext(),"网错错误");
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        data = (Coupon.DataBean) intent.getSerializableExtra("data");
        if (data != null) {
            if (data.getUseMoney() == 0) {
                tvJianMian.setText("免运费");
                flag = true;
                youhuiPrice = 0;
            } else {
                tvJianMian.setText("-" + data.getMoney() / 100);
                flag = false;
                youhuiPrice = data.getMoney()/100;
            }
        }
        tvMoney.setText(""+getYunFeiPrice());
        tvTotalMoney.setText(""+getTotalPrice());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(WeightEvent event) {
        if (!event.getType().equals("")) {
            weightType = event.getType();
            info = event.getInfo();
            if (event.getType().equals("大于10kg")){
                weightPrice = 5;
            }else{
                weightPrice = 0;
            }
            tvMoney.setText(""+getYunFeiPrice());
            tvTotalMoney.setText(""+getTotalPrice());
        }
        if (event.getNum() != 0) {
            if (event.getNum()>1){
                numPrice = (event.getNum()-1)*5;
            }
            tvNum.setText(event.getNum() + "件");
            tvMoney.setText(""+getYunFeiPrice());
            tvTotalMoney.setText(""+getTotalPrice());
        }
        if (event.getContactInfo()!=null){
            layoutShou.setVisibility(View.VISIBLE);
            tvShouName.setText(event.getContactInfo().getCompany());
            tvShouAddress.setText(event.getContactInfo().getAddress());
            tvShouPhone.setText(event.getContactInfo().getPhone());
            end = new LatLng(event.getContactInfo().getLat(),event.getContactInfo().getLng());
            //计算收货地址和发货地址的驾车距离
            double distance = AMapUtils.calculateLineDistance(start,end);
            if (event.getContactInfo().isVip()){
                imgShouVip.setVisibility(View.VISIBLE);
                flag = true;
            }else{
                imgShouVip.setVisibility(View.GONE);
                flag = false;
                if (user.getPersonType() ==2){//判断是否是汽修厂
                    juliPrice = 5;
                }else{
                    juliPrice = 15;
                }
                if (distance/1000>=20){
                    juliPrice = juliPrice+((int)(distance/1000)-20);
                }
            }
            tvMoney.setText(""+getYunFeiPrice());
            tvTotalMoney.setText(""+getTotalPrice());
        }
        if (event.getUser()!=null){
            kuaidiId = event.getUser().getId();
            tvPsy.setText(event.getUser().getNickname());
        }
    }

    @OnClick({R.id.back, R.id.tv_ji_adr, R.id.tv_fuwu, R.id.layout_num, R.id.layout_psy,R.id.layout_ticket,
            R.id.layout_baojia,R.id.layout_daishou,R.id.layout_shou_address,R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_ji_adr:
                startAcy(AddressListActivity.class);
                break;
            case R.id.layout_shou_address://编辑收货地址
                startAcy(EditShouAdrActivity.class);
                break;
            case R.id.layout_psy:
                startAcy(ChoosePsyActivity.class);
                break;
            case R.id.layout_num:
                startAcy(JiJianNumActivity.class);
                break;
            case R.id.layout_ticket:
                startAcy(MyTicketActivity.class);
                break;
            case R.id.layout_baojia:
                showBaoJia();
                break;
            case R.id.layout_daishou:
                showDaiShou();
                break;
            case R.id.tv_fuwu:
                startAcy(FuWuActivity.class);
                break;
            case R.id.submit:
                if (tvPsy.getText().toString().equals("请选择")){
                    MyToast.show(getApplicationContext(),"请选择网点");
                    return;
                }
                if (tvShouAddress.getText().toString().isEmpty()){
                    MyToast.show(getApplicationContext(),"请选输入收货地址");
                    return;
                }
                if (tvNum.getText().toString().isEmpty()){
                    MyToast.show(getApplicationContext(),"请选择物品数量");
                    return;
                }
                if (!checkBox.isChecked()){
                    MyToast.show(getApplicationContext(),"请同意软件服务协议");
                    return;
                }
                updateData();
                break;
        }
    }
    /**
     * 下订单
     * */
    private void updateData() {
        loading.show();
        Order order = new Order();
        order.setInfo(info);
        order.setKuaidiID(kuaidiId);
        order.setFromUid(user.getId());
        order.setFromName(tvFaName.getText().toString());
        order.setFromLat(start.latitude);
        order.setFromLng(start.longitude);
        order.setFromPhone(tvFaPhone.getText().toString());
        order.setFromAddress(tvFaAddress.getText().toString());
        if (type.equals("上门取件")){
            order.setSendType(1);
        }else{
            order.setSendType(2);
        }
        order.setToName(tvShouName.getText().toString());
        order.setToLat(end.latitude);
        order.setToLng(end.longitude);
        order.setToPhone(tvShouPhone.getText().toString());
        order.setToAddress(tvShouAddress.getText().toString());
        order.setWeight(weightType);
        order.setNumber(Integer.valueOf(tvNum.getText().toString().replace("件","")));
        if (!tvBaojia.getText().toString().isEmpty()){
            order.setBaojiaMoney(Integer.valueOf(tvBaojia.getText().toString().replace("¥",""))*100);
            order.setBaojiajine(baojiaMoney*100);
        }else{
            order.setBaojiaMoney(0);
        }
        order.setTotalMoney(getTotalPrice()*100);
        order.setYunfeiMoney(getYunFeiPrice()*100);
        if (data !=null){
            order.setCouponID(data.getId());
        }
        if (!tvDaishou.getText().toString().isEmpty()){
            order.setDaishouMoney(Integer.valueOf(tvDaishou.getText().toString())*100);
        }else{
            order.setDaishouMoney(0);
        }
        String json = GsonUtil.objectToJson(order);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).addOrder(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    switch (response.code()){
                        case 200:
                            String json = response.body().string();
                            Log.i("json",json);
                            OrderBean orderBean = (OrderBean) GsonUtil.praseJsonToModel(json,OrderBean.class);
                            Intent intent = new Intent();
                            intent.setClass(JiJianActivity.this,PayTypeActivity.class);
                            intent.putExtra("orderNo",orderBean.getData().getOrderNumber());
                            intent.putExtra("price",orderBean.getData().getTotalMoney());
                            startActivity(intent);
                            finish();
//                            MyToast.show(getApplicationContext(), "下单成功");
                         /*   startAcy(PayTypeActivity.class);
                            finish();*/
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Log.i("error",t.toString());
                MyToast.show(getApplicationContext(),"网络出现错误");
            }
        });
    }

    /**
     * 保价弹框
     */
    private void showBaoJia() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.setContentView(R.layout.dialog_baojia);
        dialog.setCanceledOnTouchOutside(true);
        //给AlertDialog设置4个圆角
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_shape);
        final EditText etNum = dialog.findViewById(R.id.et_num);
        final TextView tvNum = dialog.findViewById(R.id.tv_num);
        TextView tvSure = dialog.findViewById(R.id.tv_sure);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (s.toString().equals("0")){
                        tvNum.setText("0");
                    }else {
                        String num = getBaoJiaString(s.toString());
                        if (!num.equals("不保价")) {
                            tvNum.setText(""+num);
                        } else {
                            MyToast.show(getApplicationContext(), "5000及5000以上不保价");
                        }
                    }
                }else{
                    tvNum.setText("0");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etNum.getText().toString().isEmpty()){
                    if (Integer.valueOf(etNum.getText().toString()) < 5000) {
                        baojiaMoney = Integer.valueOf(etNum.getText().toString());
                        tvBaojia.setText(tvNum.getText().toString());
                        baojiaPrice = Integer.valueOf(tvNum.getText().toString().replace("¥",""));
                        tvTotalMoney.setText(""+getTotalPrice());
                        dialog.dismiss();
                    } else {
                        MyToast.show(getApplicationContext(), "5000及5000以上不保价");
                    }
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBaojia.setText("");
                baojiaPrice = 0;
                tvTotalMoney.setText(""+getTotalPrice());
                dialog.dismiss();
            }
        });
    }
    /**
     * 代收弹框
     */
    private void showDaiShou() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.setContentView(R.layout.dialog_daishou);
        dialog.setCanceledOnTouchOutside(true);
        //给AlertDialog设置4个圆角
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_shape);
        final EditText etNum = dialog.findViewById(R.id.et_num);
        TextView tvSure = dialog.findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etNum.getText().toString();
                if (s.isEmpty()){
                    daishouPrice = 0;
                }else{
                    daishouPrice = Integer.valueOf(s);
                    tvDaishou.setText(s);
                }
                tvTotalMoney.setText(""+getTotalPrice());
                dialog.dismiss();
            }
        });
    }
    /**
     * 计算运费的方法
     * */
    private int getYunFeiPrice(){
        if (flag){
            return 0;
        }else{
            if (user.isVip()){
                return 0;
            }else{
                yunfei = juliPrice+weightPrice+numPrice-youhuiPrice;
                return yunfei;
            }
        }
    }
    /**
     * 总价方法
     * */
    private int getTotalPrice(){
        return getYunFeiPrice()+baojiaPrice;
    }
    /**
     * 保价规则
     */
    private String getBaoJiaString(String s) {
        String a = "";
        int i = Integer.valueOf(s);
        if (i < 500) {
            a = "2";
        } else if (i < 1000) {
            a = "4";
        } else if (i < 1500) {
            a = "6";
        } else if (i < 2000) {
            a = "8";
        } else if (i < 2500) {
            a = "10";
        } else if (i < 3000) {
            a = "15";
        } else if (i < 3500) {
            a = "18";
        } else if (i < 4000) {
            a = "25";
        } else if (i < 4500) {
            a = "28";
        } else if (i < 5000) {
            a = "30";
        } else {
            a = "不保价";
        }
        return a;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
