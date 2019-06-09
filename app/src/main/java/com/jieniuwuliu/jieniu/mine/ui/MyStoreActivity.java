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
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
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
        call.enqueue(new SimpleCallBack<ResponseBody>(MyStoreActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {

            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(MyStoreActivity.this, object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {

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
