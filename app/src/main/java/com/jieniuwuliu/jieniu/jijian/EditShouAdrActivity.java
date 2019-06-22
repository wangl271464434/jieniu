package com.jieniuwuliu.jieniu.jijian;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.ContactInfo;
import com.jieniuwuliu.jieniu.bean.SearchStore;
import com.jieniuwuliu.jieniu.jijian.adapter.SearchStoreAdapter;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.messageEvent.WeightEvent;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 编辑收货地址
 */
public class EditShouAdrActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.et_company)
    EditText etCompany;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_address)
    EditText etAddress;
    private String name,phone,address,company;
    private MyLoading loading;
    private double lat,lng;
    private boolean isVip;
    private int id = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_shou_adr;
    }

    @Override
    protected void init() {
        title.setText("编辑收货地址");
//        right.setText("门店搜索");
        EventBus.getDefault().register(this);
        loading = new MyLoading(this,R.style.CustomDialog);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(CarEvent event) {
        Log.w("address",event.toString());
        if (event.getType()!=null){
            if (event.getType().equals("address")){
                lat = event.getPoint().getLatitude();
                lng = event.getPoint().getLongitude();
                tvCity.setText(event.getAddress());
            }
        }
        if (event.getStore()!=null){
            SearchStore.DataBean item = event.getStore();
            isVip = item.isVip();
            etCompany.setText(item.getNickname());
            etCompany.setSelection(etCompany.getText().length());
            etName.setText(item.getAddress().getName());
            etName.setSelection(etName.getText().length());
            etPhone.setText(item.getAddress().getPhone());
            etPhone.setSelection(etPhone.getText().length());
            tvCity.setText(item.getAddress().getAddress().replace("陕西省",""));
            lat = item.getAddress().getLat();
            lng = item.getAddress().getLng();
            id = item.getUid();
        }
    }
    @OnClick({R.id.back,R.id.tv_store_search,R.id.tv_city, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_store_search:
                startAcy(SearchStoreActivity.class);
                break;
            case R.id.tv_city:
                KeyboardUtil.hideSoftKeyboard(this);
                startAcy(ChooseAddressActivity.class);
                break;
            case R.id.btn:
                loading.show();
                company = etCompany.getText().toString();
                name = etName.getText().toString();
                phone = etPhone.getText().toString();
                if (company.isEmpty()||phone.isEmpty()){
                    MyToast.show(getApplicationContext(),"公司名称/联系电话不能为空");
                    return;
                }
                if (tvCity.getText().toString().equals("请选择")){
                    MyToast.show(getApplicationContext(),"请选择所在城市");
                    return;
                }
               /* if (etAddress.getText().toString().isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入具体地址");
                    return;
                }*/
                address = tvCity.getText().toString()+etAddress.getText().toString();
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setCompany(company);
                contactInfo.setName(name);
                contactInfo.setAddress(address);
                contactInfo.setPhone(phone);
                contactInfo.setLat(lat);
                contactInfo.setLng(lng);
                contactInfo.setVip(isVip);
                contactInfo.setId(id);
                WeightEvent event = new WeightEvent();
                event.setContactInfo(contactInfo);
                EventBus.getDefault().post(event);
                finish();
                loading.dismiss();
                break;
        }
    }
}
