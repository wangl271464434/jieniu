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
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Address;
import com.jieniuwuliu.jieniu.bean.AddressList;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.mine.adapter.AddressAdater;
import com.jieniuwuliu.jieniu.view.MyLoading;

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
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void init() {
        title.setText("我的地址");
        right.setText("新增");
        loading = new MyLoading(this,R.style.CustomDialog);
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
        loading.show();
        Call<AddressList> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getAddressList();
        call.enqueue(new SimpleCallBack<AddressList>(AddressListActivity.this) {
            @Override
            public void onSuccess(Response<AddressList> response) {
                loading.dismiss();
                try{
                    list.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<AddressList> response) {
                loading.dismiss();
                try{
                    String error = response.errorBody().string();
                    JSONObject object = new JSONObject(error);
                    MyToast.show(AddressListActivity.this, object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
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
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).deleteAddress(list.get(position).getId());
        call.enqueue(new SimpleCallBack<ResponseBody>(AddressListActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),"删除成功");
                list.remove(list.get(position));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String error = response.errorBody().string();
                    JSONObject object = new JSONObject(error);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
            }
        });
    }
    /**
     * 设为默认地址
     * */
    @Override
    public void setDefault(int position,boolean isChecked) {
        for (int i = 0;i<list.size();i++){
            if (i == position){
                list.get(i).setDefaultX(isChecked);
            }else{
                list.get(i).setDefaultX(false);
            }
        }
        adapter.notifyDataSetChanged();
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
        loading.show();
        Map<String,Object> map = new HashMap<>();
        map.put("default",true);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).updateAddress(list.get(position).getId(),body);
        call.enqueue(new SimpleCallBack<ResponseBody>(AddressListActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                list.get(position).setDefaultX(true);
                adapter.notifyDataSetChanged();
                MyToast.show(getApplicationContext(),"设置成功");
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                list.get(position).setDefaultX(false);
                adapter.notifyDataSetChanged();
                MyToast.show(getApplicationContext(),"设置失败");
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
            }
        });
    }

    /**
     * 取消默认地址
     * */
    private void cancelDefault(final int position) {
        loading.show();
        Map<String,Object> map = new HashMap<>();
        map.put("default",false);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).updateAddress(list.get(position).getId(),body);
        call.enqueue(new SimpleCallBack<ResponseBody>(AddressListActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                list.get(position).setDefaultX(false);
                adapter.notifyDataSetChanged();
                MyToast.show(getApplicationContext(),"取消成功");
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                list.get(position).setDefaultX(true);
                adapter.notifyDataSetChanged();
                MyToast.show(getApplicationContext(),"取消失败");
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(getApplicationContext(),s);
            }
        });
    }
}
