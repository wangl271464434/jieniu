package com.jieniuwuliu.jieniu.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.VinCar;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class XJCarTypeActivity extends BaseActivity {
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.layout_vin)
    LinearLayout layoutVin;
    private String vin,token;
    private Intent intent;
    private MyLoading loading;
    private VinCar vinCar;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xjcar_type;
    }
    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
    }
    @OnClick({R.id.layout_back, R.id.layout_search,R.id.layout_vin, R.id.tv_shoudong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_search:
                vin = edit.getText().toString();
                if (vin.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入vin码");
                    return;
                }
                KeyboardUtil.hideSoftKeyboard(this);
                searchCar(vin);
                break;
            case R.id.layout_vin:
                String carNum = edit.getText().toString();
                intent = new Intent();
                intent.setClass(this,XjInfoActivity.class);
                intent.putExtra("data",vinCar.getData());
                intent.putExtra("carNo",carNum);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_shoudong:
                intent = new Intent();
                intent.setClass(this,XjAddCarTypeActivity.class);
                startActivity(intent);
                break;
        }
    }
    /**
     * 根据vin搜索
     * */
    private void searchCar(String vin) {
        loading.show();
        Call<VinCar> call = HttpUtil.getInstance().getApi(token).selectVin(vin);
        call.enqueue(new SimpleCallBack<VinCar>(this) {
            @Override
            public void onSuccess(Response<VinCar> response) {
                loading.dismiss();
                vinCar = response.body();
                GlideUtil.setImgUrl(XJCarTypeActivity.this,vinCar.getData().getLogos(),img);
                tvName.setText(vinCar.getData().getCartype());
                layoutVin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(int errorCode, Response<VinCar> response) {
                loading.dismiss();
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("data"));
                    layoutVin.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
                layoutVin.setVisibility(View.GONE);
            }
        });


    }
}
