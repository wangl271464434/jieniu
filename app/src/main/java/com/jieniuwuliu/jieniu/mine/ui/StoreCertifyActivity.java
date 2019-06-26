package com.jieniuwuliu.jieniu.mine.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jieniuwuliu.jieniu.CarTypeActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.adapter.ListAdapter;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Address;
import com.jieniuwuliu.jieniu.bean.Car;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.SortModel;
import com.jieniuwuliu.jieniu.bean.StoreCerity;
import com.jieniuwuliu.jieniu.bean.WorkType;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 门店认证
 */
public class StoreCertifyActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.layout3)
    LinearLayout layout3;
    @BindView(R.id.et_store_name)
    EditText etStoreName;
    @BindView(R.id.edit_context)
    EditText editContext;
    @BindView(R.id.et_context)
    TextView etContext;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_weixin)
    EditText etWeixin;
    private int personType = 2;
    private String storeName,context="",address,contanct,phone,weixin,carType="",stroreImgUrl = "",zizhiImgUrl = "";
    private String token,storeUrl = "",zizhiUrl = "";
    private List<Car> cars;//服务车型数组
    private List<String> carTypeList;//服务车型数组
    private List<SortModel> list;//车型
    private List<WorkType> workTypes;//业务
    private  Intent intent;
    private List<String> typeList;
    private double lat,lng;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_certify;
    }

    @Override
    protected void init() {
        title.setText("门店认证");
        EventBus.getDefault().register(this);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        cars = new ArrayList<>();
        carTypeList = new ArrayList<>();
        list = new ArrayList<>();
        workTypes = new ArrayList<>();
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
            case "address":
                tvCity.setText(carEvent.getAddress());
                lat = carEvent.getPoint().getLatitude();
                lng = carEvent.getPoint().getLongitude();
                break;
        }
    }
    @OnClick({R.id.back, R.id.tv_type, R.id.et_context, R.id.tv_city,R.id.layout2, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_type://门店类型
                showTypeDialog();
                break;
            case R.id.et_context://主营业务
               startAcy(WorkTypeActivity.class);
                break;
            case R.id.layout2://服务车型
                showCarType();
                break;
            case R.id.tv_city:
                KeyboardUtil.hideSoftKeyboard(this);
                startAcy(ChooseAddressActivity.class);
                break;
            case R.id.submit://提交
                storeName = etStoreName.getText().toString();
                switch (personType){
                    case 2:
                        context = etContext.getText().toString();
                        if (context.equals("请选择")){
                            MyToast.show(getApplicationContext(),"请选择主营业务");
                            return;
                        }
                        break;
                    case 3:
                        context = editContext.getText().toString();
                        break;
                    case 4:
                        context = editContext.getText().toString();
                        break;
                }
                if (tvCity.getText().toString()!=null){
                    if (tvCity.getText().toString().equals("请选择")){
                        MyToast.show(getApplicationContext(),"请选择所在区域");
                        return;
                    }else{
                        address = tvCity.getText().toString()+etAddress.getText().toString();
                    }
                }else{
                    MyToast.show(getApplicationContext(),"请选择所在区域");
                    return;
                }
                contanct = etContact.getText().toString();
                weixin = etWeixin.getText().toString();
                phone = etPhone.getText().toString();
                if (storeName.isEmpty()||contanct.isEmpty()||phone.isEmpty()){
                    MyToast.show(getApplicationContext(),"门店名称/联系人/电话不能为空");
                    return;
                }
                final StoreCerity storeCerity = new StoreCerity();
                storeCerity.setNickname(storeName);
                storeCerity.setPersonType(personType);
                storeCerity.setYewu(context);
                storeCerity.setWechat(weixin);
                if (carTypeList.size()!=0){
                    storeCerity.setFuwuCars(GsonUtil.listToJson(carTypeList));
                }
                Address adr = new Address();
                adr.setLat(lat);
                adr.setLng(lng);
                adr.setAddress(address);
                adr.setPhone(phone);
                adr.setName(contanct);
                storeCerity.setAddress(adr);
                Intent intent = new Intent();
                intent.setClass(StoreCertifyActivity.this,AddPicActivity.class);
                intent.putExtra("storeCerity",storeCerity);
                startActivity(intent);
                finish();
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
                if (carType.equals("大型汽车")){
                    list.clear();
                }
                carType = "小型汽车";
                dialog.dismiss();
                intent = new Intent();
                intent.setClass(StoreCertifyActivity.this,CarTypeActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("type",carType);
                startActivity(intent);
            }
        });
        tvBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carType.equals("小型汽车")){
                    list.clear();
                }
                carType = "大型汽车";
                dialog.dismiss();
                intent = new Intent();
                intent.setClass(StoreCertifyActivity.this,CarTypeActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("type",carType);
                startActivity(intent);
            }
        });
    }

    /**
     * 门店类型
     * */
    private void showTypeDialog() {
        typeList = new ArrayList<>();
        typeList.add("汽修厂");
        typeList.add("配件商");
        typeList.add("汽车用品");
        typeList.add("汽保工具");
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setContentView(R.layout.dialog_list);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv);
        ListAdapter adapter = new ListAdapter(this,typeList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                tvType.setText(typeList.get(position));
                switch (typeList.get(position)){
                    case "汽修厂":
                        personType = 2;
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        break;
                    case "配件商":
                        personType = 1;
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.GONE);
                        break;
                    case "汽车用品":
                        personType = 3;
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                        break;
                    case "汽保工具":
                        personType = 4;
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                        break;
                }
                dialog.dismiss();
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
