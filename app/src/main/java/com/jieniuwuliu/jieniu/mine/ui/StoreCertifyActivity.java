package com.jieniuwuliu.jieniu.mine.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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
    private String province = "陕西省";
    private String city = "西安市";
    private String country = "雁塔区";
    private List<SortModel> list;//车型
    private List<WorkType> workTypes;//业务
    private  Intent intent;
    private List<String> typeList;

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
                intent = new Intent();
                intent.setClass(StoreCertifyActivity.this,CarTypeActivity.class);
                intent.putExtra("list", (Serializable) list);
                startActivity(intent);
                break;
            case R.id.tv_city:
                KeyboardUtil.hideSoftKeyboard(this);
                inintCityPicker();
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
                address = tvCity.getText().toString()+etAddress.getText().toString();
                contanct = etContact.getText().toString();
                weixin = etWeixin.getText().toString();
                phone = etPhone.getText().toString();
                if (storeName.isEmpty()||contanct.isEmpty()||phone.isEmpty()){
                    MyToast.show(getApplicationContext(),"门店名称/联系人/电话不能为空");
                    return;
                }
                if (tvCity.getText().toString().equals("请选择")){
                    MyToast.show(getApplicationContext(),"请选择城市");
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
                //根据地址搜索经纬度
                GeocodeSearch search = new GeocodeSearch(this);
                GeocodeQuery query = new GeocodeQuery(address,city);
                search.getFromLocationNameAsyn(query);
                search.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult geocodeResult, int i) {
                        Log.w("result",geocodeResult.toString());
                    }
                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                        Log.w("result",geocodeResult.toString());
                        LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                        Double lat = latLonPoint.getLatitude();
                        Double lng = latLonPoint.getLongitude();
                        Log.w("经纬度",lat+","+lng);
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
                    }
                });
                break;
        }
    }
    /**
     *
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

    /**
     * 城市选择
     * */
    private void inintCityPicker() {
        CityPicker cityPicker = new CityPicker.Builder(this)
                .textSize(20).title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#0CB6CA")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province(province)
                .city(city)
                .district(country)
                .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                .provinceCyclic(true)//省份滚轮是否循环显示
                .cityCyclic(false)//城市滚轮是否循环显示
                .districtCyclic(false)//地区（县）滚轮是否循环显示
                .visibleItemsCount(7)//滚轮显示的item个数
                .itemPadding(10)//滚轮item间距
                .onlyShowProvinceAndCity(false)
                .build();
            cityPicker.show();
            cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                @Override
                public void onSelected(String... citySelected) {
                    //省份
                    province = citySelected[0];
                    //城市
                    city = citySelected[1];
                    //区县（如果设定了两级联动，那么该项返回空）
                    country = citySelected[2];
                    tvCity.setText(province+city+country);
                }
                @Override
                public void onCancel() {

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