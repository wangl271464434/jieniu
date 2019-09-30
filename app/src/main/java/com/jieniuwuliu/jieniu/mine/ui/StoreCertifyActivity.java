package com.jieniuwuliu.jieniu.mine.ui;

import android.app.AlertDialog;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.CarTypeActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.QPType;
import com.jieniuwuliu.jieniu.mine.adapter.QpTypeAdapter;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.KeyboardUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 门店认证
 */
public class StoreCertifyActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout_part)
    LinearLayout layoutPart;
    @BindView(R.id.tv_part)
    TextView tvPart;
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
    @BindView(R.id.et_telephone)
    EditText etTelephone;
    private int personType = 2;
    private String storeName,context="",address,contanct,phone,telephone,carType="",stroreImgUrl = "",zizhiImgUrl = "";
    private String token,storeUrl = "",zizhiUrl = "";
    private List<Car> cars;//服务车型数组
    private List<String> carTypeList;//服务车型数组
    private List<SortModel> list;//车型
    private List<WorkType> workTypes;//业务
    private  Intent intent;
    private List<String> typeList;
    private double lat,lng;
    private int partscity = 0;
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
    @OnClick({R.id.back, R.id.tv_type,R.id.tv_part, R.id.et_context, R.id.tv_city,R.id.layout2, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_type://门店类型
                showTypeDialog();
                break;
            case R.id.tv_part://归属地
                getQpList();
                break;
            case R.id.et_context://主营业务
               startAcy(WorkTypeActivity.class);
                break;
            case R.id.layout2://服务车型
                intent = new Intent();
                intent.setClass(StoreCertifyActivity.this,CarTypeActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("type",personType);
                startActivity(intent);
//                showCarType();
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
                telephone = etTelephone.getText().toString();
                phone = etPhone.getText().toString();
                if (storeName.isEmpty()||contanct.isEmpty()||phone.isEmpty()){
                    MyToast.show(getApplicationContext(),"门店名称/联系人/电话不能为空");
                    return;
                }
                final StoreCerity storeCerity = new StoreCerity();
                storeCerity.setNickname(storeName);
                storeCerity.setPersonType(personType);
                storeCerity.setYewu(context);
                storeCerity.setLandline(telephone);
                storeCerity.setPartscity(partscity);
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
    //归属地
    private void showPartDialog(List<QPType.DataBean> data) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.8);
        window.setAttributes(params);
        dialog.setContentView(R.layout.dialog_qptype_list);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv);
        ImageView img = dialog.findViewById(R.id.img_close);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        for (QPType.DataBean dataBean:data){//移除全部汽配城
            if ("全部汽配城".equals(dataBean.getNickname())){
                data.remove(dataBean);
            }
        }
        QpTypeAdapter adapter = new QpTypeAdapter(this,data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                partscity = data.get(position).getId();
                tvPart.setText(data.get(position).getNickname());
                dialog.dismiss();
            }
        });
    }
    /**
     * 获取汽配城归属
     * */
    private void getQpList() {
        Call<QPType> call = HttpUtil.getInstance().getApi(token).getQpList();
        call.enqueue(new Callback<QPType>() {
            @Override
            public void onResponse(Call<QPType> call, Response<QPType> response) {
                try {
                    if (response.code() == 200){
                        showPartDialog(response.body().getData());
                    }else{
                        String s = response.errorBody().string();
                        JSONObject object = new JSONObject(s);
                        MyToast.show(getApplicationContext(),object.getString("msg"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<QPType> call, Throwable t) {
                MyToast.show(getApplicationContext(),"网络连接失败");
            }
        });
    }

    /**
     * 门店类型
     * */
    private void showTypeDialog() {
        typeList = new ArrayList<>();
        typeList.add("汽修厂");
        typeList.add("轿车客车");
        typeList.add("货车轻卡");
        typeList.add("汽车用品");
        typeList.add("汽保工具");
        typeList.add("单项易损");
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
                        layoutPart.setVisibility(View.GONE);
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        break;
                    case "轿车客车":
                        personType = 1;
                        layoutPart.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.GONE);
                        break;
                    case "汽车用品":
                        personType = 3;
                        layoutPart.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                        break;
                    case "汽保工具":
                        personType = 4;
                        layoutPart.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.VISIBLE);
                        break;
                    case "货车轻卡":
                        personType = 8;
                        layoutPart.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        layout3.setVisibility(View.GONE);
                        break;
                    case "单项易损":
                        personType = 9;
                        layoutPart.setVisibility(View.VISIBLE);
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
