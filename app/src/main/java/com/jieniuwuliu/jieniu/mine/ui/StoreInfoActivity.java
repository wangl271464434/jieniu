package com.jieniuwuliu.jieniu.mine.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.CarTypeActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.SortModel;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.bean.StoreCerity;
import com.jieniuwuliu.jieniu.bean.StoreInfoBean;
import com.jieniuwuliu.jieniu.bean.WorkType;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 门店资料
 */
public class StoreInfoActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_certify)
    TextView tvCertify;
    @BindView(R.id.et_store_name)
    TextView etStoreName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_yewu)
    TextView tvYeWu;
    @BindView(R.id.et_context)
    TextView etContext;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.et_address)
    TextView etAddress;
    @BindView(R.id.et_contact)
    TextView etContact;
    @BindView(R.id.et_phone)
    TextView etPhone;
    @BindView(R.id.et_telephone)
    TextView etTelephone;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private StoreInfoBean storeBean;
    private Intent intent;
    private String token,carType="";
    private int id,userType;
    private MyLoading loading;
    private List<SortModel> list;//车型
    private List<String> carTypeList;//服务车型数组
    private List<WorkType> workTypes;//业务
    private StoreCerity storeCerity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_info;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        userType = (int) SPUtil.get(this,Constant.USERTYPE,Constant.USERTYPE,0);
        id = getIntent().getIntExtra("id",0);
        list = new ArrayList<>();
        carTypeList = new ArrayList<>();
        workTypes = new ArrayList<>();
        getStoreInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(CarEvent carEvent) {
        switch (carEvent.getType()){
            case "car":
                list.clear();
                tvCarType.setText("");
                list.addAll(carEvent.getSortModelList());
                String s = "";
                for (int i=0;i<list.size();i++){
                    if (i==0){
                        s += list.get(i).getName();
                    }else{
                        s +=","+list.get(i).getName();
                    }
                    carTypeList.add(list.get(i).getName());
                }
                tvCarType.setText(s);
                break;
            case "work":
                workTypes.clear();
                etContext.setText("");
                workTypes.addAll(carEvent.getList());
                String s1 = "";
                if (workTypes.size()>0){
                    for (int i=0;i<workTypes.size();i++){
                        if (i==0){
                            s1 += workTypes.get(i).getName();
                        }else{
                            s1 +=","+workTypes.get(i).getName();
                        }
                    }
                }else{
                    s1 = "请选择";
                }
                etContext.setText(s1);
                break;
        }
        uploadData();
    }

    private void uploadData() {
        loading.show();
        storeCerity = new StoreCerity();
        storeCerity.setYewu(etContext.getText().toString());
        storeCerity.setFuwuCars(GsonUtil.listToJson(carTypeList));
        String json = GsonUtil.objectToJson(storeCerity);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> observable = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).modifyStoreInfo(body);
        observable.enqueue(new SimpleCallBack<ResponseBody>(StoreInfoActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                MyToast.show(StoreInfoActivity.this,"修改成功");
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(StoreInfoActivity.this, object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e){
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
     * 获取门店信息
     * */
    private void getStoreInfo() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getStoreInfo(id);
        call.enqueue(new SimpleCallBack<ResponseBody>(StoreInfoActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    ResponseBody body = response.body();
                    String json = body.string();
                    storeBean = (StoreInfoBean) GsonUtil.praseJsonToModel(new JSONObject(json).getString("data"),StoreInfoBean.class);
                    etStoreName.setText(storeBean.getNickname());
                    etContact.setText(storeBean.getAddress().getName());
                    etPhone.setText(storeBean.getAddress().getPhone());
                    if (storeBean.getLandline().equals("")){
                        etTelephone.setText("暂无");
                    }else{
                        etTelephone.setText(storeBean.getLandline());
                    }
                    if (storeBean.getStoreinform().equals("")){
                        tvContent.setText("暂无");
                    }else{
                        tvContent.setText(storeBean.getStoreinform());
                    }
                    etAddress.setText(storeBean.getAddress().getAddress().replace("陕西省",""));
                    switch (storeBean.getPersonType()){
                        case 1://汽配商
                            tvType.setText("配件商");
                            layout1.setVisibility(View.GONE);
                            layout2.setVisibility(View.VISIBLE);
                            String s = "";
                            if (storeBean.getFuwuCar()!=null){
                                for (int i =0;i<storeBean.getFuwuCar().size();i++){
                                    if (i==0){
                                        s += storeBean.getFuwuCar().get(i).getName();
                                    }else{
                                        s += ","+storeBean.getFuwuCar().get(i).getName();

                                    }
                                }
                            }
                            tvCarType.setText(s);
                            break;
                        case 2://汽修商
                            tvType.setText("汽修厂");
                            layout1.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.GONE);
                            tvYeWu.setText("主营业务：");
                            etContext.setText(storeBean.getYewu());
                            break;
                        case 3://汽车用品
                            tvType.setText("汽车用品");
                            layout1.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.GONE);
                            tvYeWu.setText("经营范围：");
                            etContext.setText(storeBean.getYewu());
                            break;
                        case 4://汽保工具
                            tvType.setText("汽保工具");
                            layout1.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.GONE);
                            tvYeWu.setText("经营范围：");
                            etContext.setText(storeBean.getYewu());
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(StoreInfoActivity.this, object.getString("msg"));
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

    @OnClick({R.id.back, R.id.tv_certify, R.id.et_store_name,R.id.tv_type,R.id.et_context,
            R.id.tv_car_type, R.id.et_address, R.id.et_contact, R.id.et_phone,
            R.id.et_telephone,R.id.layout_content,R.id.layout_add_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_certify://重新认证
                startAcy(StoreCertifyActivity.class);
                break;
            case R.id.et_store_name:
                MyToast.show(StoreInfoActivity.this,"请重新进行认证");
                break;
            case R.id.tv_type:
                MyToast.show(StoreInfoActivity.this,"请重新进行认证");
                break;
            case R.id.et_context://主营业务
                if (userType == 2){
                    startAcy(WorkTypeActivity.class);
                }else{
                    intent = new Intent();
                    intent.setClass(StoreInfoActivity.this,EditInfoActivity.class);
                    intent.putExtra("title","经营范围");
                    intent.putExtra("aid",storeBean.getAddress().getId());
                    intent.putExtra("info",storeBean.getYewu());
                    startActivityForResult(intent,4);
                }
                break;
            case R.id.tv_car_type:
                showCarType();
                break;
            case R.id.et_address:
                MyToast.show(StoreInfoActivity.this,"请重新进行认证");
                break;
            case R.id.et_contact://联系人
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,EditInfoActivity.class);
                intent.putExtra("title","联系人");
                intent.putExtra("aid",storeBean.getAddress().getId());
                intent.putExtra("info",storeBean.getAddress().getName());
                startActivityForResult(intent,1);
                break;
            case R.id.et_phone://电话
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,EditInfoActivity.class);
                intent.putExtra("title","联系电话");
                intent.putExtra("aid",storeBean.getAddress().getId());
                intent.putExtra("info",storeBean.getAddress().getPhone());
                startActivityForResult(intent,2);
                break;
            case R.id.et_telephone://微信
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,EditInfoActivity.class);
                intent.putExtra("title","固定电话");
                intent.putExtra("info",storeBean.getLandline());
                startActivityForResult(intent,3);
                break;
            case R.id.layout_content://简介
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,EditInfoActivity.class);
                intent.putExtra("title","门店简介");
                intent.putExtra("info",storeBean.getStoreinform());
                startActivityForResult(intent,4);
                break;
            case R.id.layout_add_pic:
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,AddStorePicActivity.class);
                intent.putExtra("photos",storeBean.getPhotos());
                startActivity(intent);
                break;
        }
    }
    /**
     * 车型选择弹框
     * */
    private void showCarType() {
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
        dialog.setContentView(R.layout.car_type_dialog);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvSmall = dialog.findViewById(R.id.tv_small_car);
        TextView tvBig = dialog.findViewById(R.id.tv_big_car);
        tvSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = "小型汽车";
                dialog.dismiss();
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,CarTypeActivity.class);
                intent.putExtra("type",carType);
                startActivity(intent);
            }
        });
        tvBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carType = "大型汽车";
                dialog.dismiss();
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,CarTypeActivity.class);
                intent.putExtra("type",carType);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            String info = data.getStringExtra("info");
            switch (requestCode){
                case 0:
                    etContext.setText(info);
                    break;
                case 2:
                    etPhone.setText(info);
                    break;
                case 3:
                    etTelephone.setText(info);
                    break;
                case 4:
                    tvContent.setText(info);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
