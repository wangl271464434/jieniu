package com.jieniuwuliu.jieniu.jijian;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Address;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.ContactInfo;
import com.jieniuwuliu.jieniu.bean.SearchStore;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.messageEvent.WeightEvent;
import com.jieniuwuliu.jieniu.mine.ui.AddPicActivity;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.mine.ui.StoreCertifyActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 编辑收货地址
 */
public class EditShouAdrActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
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
    private String token;
    private List<SearchStore.DataBean> list;
    private SearchStoreAdapter adapter;
    private PopupWindow popupWindow;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_shou_adr;
    }

    @Override
    protected void init() {
        title.setText("编辑收货地址");
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        etCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String info = etCompany.getText().toString();
                Log.i("editinfo",info);
                if (popupWindow == null){
                    if (info.length()>1){
                        search(info);
                    }
                }else{
                    popupWindow = null;
                }
            }
        });
    }
    /**
     * 根据公司名搜索
     * */
    private void search(String info) {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).searchStore(info);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            list.clear();
                            SearchStore searchStore = (SearchStore) GsonUtil.praseJsonToModel(response.body().string(),SearchStore.class);
                            if (searchStore.getData().size()>0){
                                list.addAll(searchStore.getData());
                                showSearchList();
                            }
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
                Log.i("error",t.toString());
            }
        });
    }
    /**
     * 显示
     * */
    private void showSearchList() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.search_store, null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bg_white_shape));
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        // 设置好参数之后再show
        popupWindow.showAsDropDown(etCompany);
        RecyclerView recyclerView = contentView.findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new SearchStoreAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                popupWindow.dismiss();
                SearchStore.DataBean item = list.get(position);
                etCompany.setText(item.getNickname());
                etCompany.setSelection(etCompany.getText().length());
                etName.setText(item.getAddress().getName());
                etName.setSelection(etName.getText().length());
                etPhone.setText(item.getAddress().getPhone());
                etPhone.setSelection(etPhone.getText().length());
                tvCity.setText(item.getAddress().getAddress());
                lat = item.getAddress().getLat();
                lng = item.getAddress().getLng();
                etCompany.setFocusable(true);
                etCompany.setFocusableInTouchMode(true);
                etCompany.requestFocus();
                etCompany.requestFocusFromTouch();
            }
        });
        etCompany.setFocusable(true);
        etCompany.setFocusableInTouchMode(true);
        etCompany.requestFocus();
        etCompany.requestFocusFromTouch();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(CarEvent event) {
        Log.w("address",event.toString());
        if (event.getType().equals("address")){
            lat = event.getPoint().getLatitude();
            lng = event.getPoint().getLongitude();
            tvCity.setText(event.getAddress());
        }
    }
    @OnClick({R.id.back, R.id.tv_city, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
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
                WeightEvent event = new WeightEvent();
                event.setContactInfo(contactInfo);
                EventBus.getDefault().post(event);
                finish();
                loading.dismiss();
                break;
        }
    }
}
