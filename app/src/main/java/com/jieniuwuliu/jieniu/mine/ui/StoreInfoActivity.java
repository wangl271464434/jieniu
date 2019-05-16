package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.bean.StoreInfoBean;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;

import org.json.JSONObject;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.et_weixin)
    TextView etWeixin;
    private StoreInfoBean storeBean;
    private Intent intent;
    private String token;
    private int id;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_info;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        id = getIntent().getIntExtra("id",0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStoreInfo();
    }

    /**
     * 获取门店信息
     * */
    private void getStoreInfo() {
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getStoreInfo(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            ResponseBody body = response.body();
                            String json = body.string();
                            storeBean = (StoreInfoBean) GsonUtil.praseJsonToModel(new JSONObject(json).getString("data"),StoreInfoBean.class);
                            etStoreName.setText(storeBean.getNickname());
                            etContact.setText(storeBean.getAddress().getName());
                            etPhone.setText(storeBean.getAddress().getPhone());
                            etWeixin.setText(storeBean.getWechat());
                            etAddress.setText(storeBean.getAddress().getAddress());
                            switch (storeBean.getPersonType()){
                                case 1://汽配商
                                    tvType.setText("配件商");
                                    layout1.setVisibility(View.GONE);
                                    layout2.setVisibility(View.VISIBLE);
                                    String s = "";
                                    if (storeBean.getFuwuCar()!=null){
                                        for (int i =0;i<storeBean.getFuwuCar().size();i++){
                                            if (i!=0){
                                                s += ","+storeBean.getFuwuCar().get(i).getName();
                                            }else{
                                                s += storeBean.getFuwuCar().get(i).getName();
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

                            break;
                        case 400:
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(StoreInfoActivity.this, object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.back, R.id.tv_certify, R.id.et_store_name,R.id.tv_type,R.id.et_context,
            R.id.tv_car_type, R.id.et_address, R.id.layout_img, R.id.et_contact, R.id.et_phone,
            R.id.et_weixin,R.id.layout_add_pic})
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
                MyToast.show(StoreInfoActivity.this,"请重新进行认证");
                break;
            case R.id.tv_car_type:
                MyToast.show(StoreInfoActivity.this,"请重新进行认证");
                break;
            case R.id.et_address:
                startAcy(AddressListActivity.class);
                break;
            case R.id.layout_img:
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
            case R.id.et_weixin://微信
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,EditInfoActivity.class);
                intent.putExtra("title","绑定微信");
                intent.putExtra("info",storeBean.getWechat());
                startActivityForResult(intent,3);
                break;
            case R.id.layout_add_pic:
                intent = new Intent();
                intent.setClass(StoreInfoActivity.this,AddStorePicActivity.class);
                intent.putExtra("photos",storeBean.getPhotos());
                startActivity(intent);
                break;
        }
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
                    etWeixin.setText(info);
                    break;
            }
        }

    }
}
