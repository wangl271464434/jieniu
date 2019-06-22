package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
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

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void modify(int position) {
        Intent intent = new Intent();
        intent.setClass(this,ModifyAddressActivity.class);
        intent.putExtra("address",list.get(position));
        startActivity(intent);
    }
}
