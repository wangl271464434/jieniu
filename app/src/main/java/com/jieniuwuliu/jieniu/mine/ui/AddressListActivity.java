package com.jieniuwuliu.jieniu.mine.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Address;
import com.jieniuwuliu.jieniu.bean.AddressList;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.mine.adapter.AddressAdater;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 地址列表
 */
public class AddressListActivity extends BaseActivity implements AddressAdater.CallBack {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.right)
    TextView right;
    private List<Address> list;
    private AddressAdater adapter;
    private String token;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void init() {
        title.setText("我的地址");
        right.setText("新增");
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new AddressAdater(this,list);
        rv.setAdapter(adapter);
        adapter.setCallBack(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        getData();
    }

    /**
     * 获取地址列表
     * */
    private void getData() {
        Call<AddressList> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getAddressList();
        call.enqueue(new Callback<AddressList>() {
            @Override
            public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                try{
                    switch (response.code()){
                        case 200:
                            list.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String error = response.errorBody().string();
                            JSONObject object = new JSONObject(error);
                            MyToast.show(AddressListActivity.this, object.getString("msg"));
                            break;
                            default:
                                Log.w("error",response.errorBody().string());
                                break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddressList> call, Throwable t) {
                Log.w("error",t.toString());
            }
        });
    }

    @OnClick({R.id.back, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                startAcy(AddAddressActivity.class);
                break;
        }
    }
    /**
     * 删除地址
     * */
    @Override
    public void delete(final int position) {
        if (list.get(position).isDefaultX()){
            MyToast.show(getApplicationContext(),"默认地址不能删除");
            return;
        }
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).deleteAddress(list.get(position).getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            MyToast.show(getApplicationContext(),"删除成功");
                            list.remove(list.get(position));
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String error = response.errorBody().string();
                            JSONObject object = new JSONObject(error);
                            MyToast.show(getApplicationContext(), object.getString("msg"));
                            break;
                            default:
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
    /**
     * 设为默认地址
     * */
    @Override
    public void setDefault(int position,boolean isChecked) {
        if (!isChecked){
            cancelDefault(position);
        }else{
            updateDefault(position);
        }
    }
    /**
     * 设置默认地址
     * */
    private void updateDefault(final int position) {
        Map<String,Object> map = new HashMap<>();
        map.put("default",true);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).updateAddress(list.get(position).getId(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()){
                    case 200:
                        list.get(position).setDefaultX(true);
                        adapter.notifyDataSetChanged();
                        MyToast.show(getApplicationContext(),"设置成功");
                        break;
                    case 400:
                        list.get(position).setDefaultX(false);
                        adapter.notifyDataSetChanged();
                        MyToast.show(getApplicationContext(),"设置失败");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 取消默认地址
     * */
    private void cancelDefault(final int position) {
        Map<String,Object> map = new HashMap<>();
        map.put("default",false);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).updateAddress(list.get(position).getId(),body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()){
                    case 200:
                        list.get(position).setDefaultX(false);
                        adapter.notifyDataSetChanged();
                        MyToast.show(getApplicationContext(),"取消成功");
                        break;
                    case 400:
                        list.get(position).setDefaultX(true);
                        adapter.notifyDataSetChanged();
                        MyToast.show(getApplicationContext(),"取消失败");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
