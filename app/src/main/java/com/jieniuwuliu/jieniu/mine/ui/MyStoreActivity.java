package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.StoreInfoBean;
import com.jieniuwuliu.jieniu.qipeishang.QPSORQXInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 我的门店
 * */
public class MyStoreActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.layout_yewu)
    LinearLayout layoutYewu;
    @BindView(R.id.layout_car)
    LinearLayout layoutCar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_context)
    TextView tvContext;
    @BindView(R.id.img)
    ImageView img;
    private String token;
    private Intent intent;
    private int id;
    private StoreInfoBean storeBean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_store;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        id = getIntent().getIntExtra("id",0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStoreInfo(token);
    }

    /**
     * 获取门店信息
     */
    private void getStoreInfo(String token) {
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getStoreInfo(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()){
                    case 200:
                       /* storeBean = response.body().getData();
                        tvName.setText(storeBean.getNickname());
                        name.setText(storeBean.getAddress().getName());
                        phone.setText(storeBean.getAddress().getPhone());
                        switch (storeBean.getPersonType()){
                            case 1://汽配商
                                layoutCar.setVisibility(View.VISIBLE);
                                layoutYewu.setVisibility(View.GONE);
                                break;
                            case 2://汽修商
                                layoutYewu.setVisibility(View.VISIBLE);
                                layoutCar.setVisibility(View.GONE);
                                tvContext.setText(storeBean.getYewu());
                                break;
                        }
                        GlideUtil.setImgUrl(MyStoreActivity.this,storeBean.getShopPhoto(),img);
                        address.setText(storeBean.getAddress().getAddress());*/

                        break;
                    case 400:
                        try {
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(MyStoreActivity.this, object.getString("msg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    @OnClick({R.id.back, R.id.tv_edit,R.id.tv_fuzhi, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_edit:
                intent = new Intent();
                intent.setClass(MyStoreActivity.this,QPSORQXInfoActivity.class);
                intent.putExtra("storeInfo",storeBean);
                startActivity(intent);
                break;
            case R.id.tv_fuzhi:
                break;
            case R.id.btn:
                break;
        }
    }
}
