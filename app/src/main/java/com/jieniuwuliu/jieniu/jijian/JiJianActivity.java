package com.jieniuwuliu.jieniu.jijian;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.jieniuwuliu.jieniu.FuWuActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Coupon;
import com.jieniuwuliu.jieniu.bean.Order;
import com.jieniuwuliu.jieniu.bean.OrderBean;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.SearchStore;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.messageEvent.WeightEvent;
import com.jieniuwuliu.jieniu.mine.ui.MyTicketActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 填写寄件信息
 */
public class JiJianActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {
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
    private LatLonPoint start,end;
    private int yunfei = 0;
    private int baojiaPrice = 0,weightPrice = 0,juliPrice = 0,numPrice=0,youhuiPrice = 0,daishouPrice = 0,baojiaMoney = 0;
    private boolean flag = false;//判断是否免运费
    private MyLoading loading;
    private String info = "";
    private int kuaidiId,toUid = 0,id;
    private OrderInfo orderInfo;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ji_jian;
    }

    @Override
    protected void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        type = getIntent().getStringExtra("type");
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("order");
        int userType = (int) SPUtil.get(this,Constant.USERTYPE,Constant.USERTYPE,0);
        if (userType ==2){//判断是否是汽修厂
            juliPrice = 5;
        }else{
            juliPrice = 10;
        }
        tvType.setText(type);
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
        getUserInfo();
        if (orderInfo != null){
            setData();
        }
    }

    private void setData() {
        layoutShou.setVisibility(View.VISIBLE);
        if (orderInfo.getToUid()!=0){
            search(orderInfo.getToName());
        }else{
            tvShouName.setText(orderInfo.getToName());
            tvShouAddress.setText(orderInfo.getToAddress().replace("陕西省",""));
            tvShouPhone.setText(orderInfo.getToPhone());
            end = new LatLonPoint(orderInfo.getToLat(),orderInfo.getToLng());
            RouteSearch routeSearch = new RouteSearch(JiJianActivity.this);
            routeSearch.setRouteSearchListener(JiJianActivity.this);
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start, end);
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                    RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
            routeSearch.calculateDriveRouteAsyn(query);
        }
    }
    /**
     * 搜索
     * */
    private void search(String info) {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).searchStore(info,1,10);
        call.enqueue(new SimpleCallBack<ResponseBody>(JiJianActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try {
                    SearchStore searchStore = (SearchStore) GsonUtil.praseJsonToModel(response.body().string(),SearchStore.class);
                    Log.i("门店",searchStore.getData().size()+"");
                    if (searchStore.getData().size()>0){
                        SearchStore.DataBean data = searchStore.getData().get(0);
                        if (data.isVip()){
                            imgShouVip.setVisibility(View.VISIBLE);
                            flag = true;
                        }else {
                            imgShouVip.setVisibility(View.GONE);
                            flag = false;
                        }
                        tvShouName.setText(data.getNickname());
                        tvShouAddress.setText(data.getAddress().getAddress().replace("陕西省",""));
                        tvShouPhone.setText(data.getAddress().getPhone());
                        end = new LatLonPoint(data.getAddress().getLat(),data.getAddress().getLng());
                        toUid = orderInfo.getToUid();
                        RouteSearch routeSearch = new RouteSearch(JiJianActivity.this);
                        routeSearch.setRouteSearchListener(JiJianActivity.this);
                        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start, end);
                        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                                RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
                        routeSearch.calculateDriveRouteAsyn(query);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                MyToast.show(getApplicationContext(),s);
            }
        });
    }
    private void getUserInfo() {
        Call<UserBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getUserInfo();
        call.enqueue(new SimpleCallBack<UserBean>(JiJianActivity.this) {
            @Override
            public void onSuccess(Response<UserBean> response) {
                loading.dismiss();
                try{
                    if (response.body().getStatus() == 0){
                        user = response.body().getData();
                        id = user.getId();
                        if (user.isVip()){
                            imgJiVip.setVisibility(View.VISIBLE);
                        }else {
                            imgJiVip.setVisibility(View.GONE);
                        }
                        tvFaName.setText(user.getNickname());
                        tvFaPhone.setText(user.getAddress().getPhone());
                        tvFaAddress.setText(user.getAddress().getAddress().replace("陕西省",""));
                        start = new LatLonPoint(user.getAddress().getLat(),user.getAddress().getLng());
                        tvMoney.setText(""+getYunFeiPrice());
                    }
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onFail(int errorCode, Response<UserBean> response) {
                loading.dismiss();
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
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
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
        }
        if (event.getNum() != 0) {
            numPrice = (event.getNum()-1)*5;
            tvNum.setText(event.getNum() + "件");
            tvMoney.setText(""+getYunFeiPrice());
        }
        if (event.getContactInfo()!=null){
            layoutShou.setVisibility(View.VISIBLE);
            tvShouName.setText(event.getContactInfo().getCompany());
            tvShouAddress.setText(event.getContactInfo().getAddress().replace("陕西省",""));
            tvShouPhone.setText(event.getContactInfo().getPhone());
            end = new LatLonPoint(event.getContactInfo().getLat(),event.getContactInfo().getLng());
            //计算收货地址和发货地址的驾车距离
//            double distance = AMapUtils.calculateLineDistance(start,end);
            if (event.getContactInfo().isVip()){
                imgShouVip.setVisibility(View.VISIBLE);
                flag = true;
            }else{
                imgShouVip.setVisibility(View.GONE);
                flag = false;
            }
            toUid = event.getContactInfo().getId();
            RouteSearch routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start, end);
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                    RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
            routeSearch.calculateDriveRouteAsyn(query);
        }
        if (event.getUser()!=null){
            kuaidiId = event.getUser().getId();
            tvPsy.setText(event.getUser().getNickname());
        }
    }

    @OnClick({R.id.back, R.id.tv_fuwu, R.id.layout_num, R.id.layout_psy,R.id.layout_ticket,
            R.id.layout_baojia,R.id.layout_daishou,R.id.layout_shou_address,R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
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
        order.setFromUid(id);
        order.setFromName(tvFaName.getText().toString());
        order.setFromLat(start.getLatitude());
        order.setFromLng(start.getLongitude());
        order.setFromPhone(tvFaPhone.getText().toString());
        order.setFromAddress(tvFaAddress.getText().toString());
        if (type.equals("上门取件")){
            order.setSendType(1);
        }else{
            order.setSendType(2);
        }
        order.setToUid(toUid);
        order.setToName(tvShouName.getText().toString());
        order.setToLat(end.getLatitude());
        order.setToLng(end.getLongitude());
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
        call.enqueue(new SimpleCallBack<ResponseBody>(JiJianActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    String json = response.body().string();
                    Log.i("json",json);
                    OrderBean orderBean = (OrderBean) GsonUtil.praseJsonToModel(json,OrderBean.class);
                    Intent intent = new Intent();
                    intent.setClass(JiJianActivity.this,PayTypeActivity.class);
                    intent.putExtra("orderNo",orderBean.getData().getOrderNumber());
                    intent.putExtra("price",orderBean.getData().getTotalMoney());
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                }catch (Exception e){
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

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                        if (driveRouteResult.getPaths().size() > 0) {
                            DrivePath drivePath = driveRouteResult.getPaths().get(0);
                            if (drivePath == null) {
                                return;
                            }
                            Double distance = Double.valueOf(drivePath.getDistance());
                            if (distance/1000>=20){
                                juliPrice = juliPrice+((int)(distance/1000)-20);
                            }
                            tvMoney.setText(""+getYunFeiPrice());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
