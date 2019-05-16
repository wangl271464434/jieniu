package com.jieniuwuliu.jieniu.mine.ui;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.AddAdr;
import com.jieniuwuliu.jieniu.bean.Address;
import com.jieniuwuliu.jieniu.messageEvent.AddressEvent;
import com.jieniuwuliu.jieniu.bean.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加地址
 */
public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_address)
    EditText etAddress;
    private String name,phone,address;
    private double lat,lng;
    private String token;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void init() {
        title.setText("编辑地址");
        EventBus.getDefault().register(this);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(AddressEvent event) {
        Log.w("address",event.toString());
        lat = event.getPoint().getLatitude();
        lng = event.getPoint().getLongitude();
        tvAddress.setText(event.getAddress());
    }

    @OnClick({R.id.back, R.id.tv_address, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_address:
                startAcy(ChooseAddressActivity.class);
                break;
            case R.id.btn_save:
                name = etName.getText().toString();
                phone = etPhone.getText().toString();
                address = tvAddress.getText().toString();
                if (name.isEmpty()||phone.isEmpty()||address.isEmpty()){
                    MyToast.show(getApplicationContext(),"联系人/联系电话/地址不能为空");
                    return;
                }
                update();
                break;
        }
    }
    /**
     * 提交数据
     * */
    private void update() {
        Address adr = new Address();
        adr.setAddress(address+etAddress.getText().toString());
        adr.setName(name);
        adr.setPhone(phone);
        adr.setLat(lat);
        adr.setLng(lng);
        String json = GsonUtil.objectToJson(adr);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<AddAdr> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).addAddress(body);
        call.enqueue(new Callback<AddAdr>() {
            @Override
            public void onResponse(Call<AddAdr> call, Response<AddAdr> response) {
                Log.w("result",response.toString());
                try{
                    switch (response.code()){
                        case 200:
                            MyToast.show(getApplicationContext(), "添加成功");
                            finish();
                            break;
                        case 400:
                            String error = response.errorBody().string();
                            JSONObject object = new JSONObject(error);
                            MyToast.show(getApplicationContext(), object.getString("msg"));
                            break;
                    }
                }catch (Exception e){e.printStackTrace();}

            }

            @Override
            public void onFailure(Call<AddAdr> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
