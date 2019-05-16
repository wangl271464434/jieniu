package com.jieniuwuliu.jieniu.jijian;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.adapter.JiJianSelectAdater;
import com.jieniuwuliu.jieniu.mine.ui.ChooseAddressActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
 * 寄件查询
 */
public class JiJianSelectActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener, OnRefreshListener, OnLoadMoreListener{
    @BindView(R.id.tv_shaixuan)
    TextView tvShaixuan;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    private JiJianSelectAdater adapter;
    private Dialog dialog;
    private int state = 0,type= 0;//运输状态  1 总站 2运输中 3中转站  4配送中  5完成
    private int page =1,pageNum= 10;
    private MyLoading loading;
    private String token;
    private List<OrderInfo> list;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_jijian_select;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        loading = new MyLoading(this,R.style.CustomDialog);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new JiJianSelectAdater(this,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        getData();
    }
    /**
     * 获取我的寄件列表
     * */
    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getMyOrderList(type,state,page,pageNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                            }
                            String json = response.body().string();
                            OrderResult orderResult = (OrderResult) GsonUtil.praseJsonToModel(json,OrderResult.class);
                            if (orderResult.getData().size()<10){
                                refreshLayout.setNoMoreData(true);
                            }
                            list.addAll(orderResult.getData());
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String s = response.errorBody().string();
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
               loading.dismiss();
                MyToast.show(getApplicationContext(),"网络请求错误");
            }
        });
    }

    @OnClick({R.id.back,R.id.img_search, R.id.tv_shaixuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_search:
                String info = etSearch.getText().toString();
                if (info.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入订单号进行查询");
                    return ;
                }
                selectOrder(info);
                KeyboardUtil.hideSoftKeyboard(JiJianSelectActivity.this);
                break;
            case R.id.tv_shaixuan://筛选
                setDialog();
                break;
        }
    }
    /**筛选弹框*/
    private void setDialog() {
        state = 5;
        type =2;
        dialog = new Dialog(this, R.style.Dialog);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.dialog_shaixuan, null);
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
//        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(viewDialog,layoutParams);
        dialog.show();
        RadioButton btn1 = dialog.findViewById(R.id.btn_state_buxian);
        RadioButton btn2 = dialog.findViewById(R.id.btn_state_wancheng);
        RadioButton btn3 = dialog.findViewById(R.id.btn_state_peisong);
        RadioButton btn4 = dialog.findViewById(R.id.btn_type_buxian);
        RadioButton btn5 = dialog.findViewById(R.id.btn_type_shou);
        RadioButton btn6 = dialog.findViewById(R.id.btn_type_ji);
        TextView tvSure  = dialog.findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_state_buxian:
                state = 0;
                break;
            case R.id.btn_state_wancheng:
                state = 5;
                break;
            case R.id.btn_state_peisong:
                state = 4;
                break;
            case R.id.btn_type_buxian:
                type = 0;
                break;
            case R.id.btn_type_shou:
                type = 2;
                break;
            case R.id.btn_type_ji:
                type = 1;
                break;
            case R.id.tv_sure:
                dialog.dismiss();
                list.clear();
                page = 1;
                getData();
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        list.clear();
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }
    /**
     * 查询某一条订单
     * */
    public void selectOrder(String info){
        list.clear();
        Call<ResponseBody> call =HttpUtil.getInstance().getApi(token).selectOrder(info);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                            }
                            String json = new JSONObject(response.body().string()).getString("data");
                            OrderInfo dataBean = (OrderInfo) GsonUtil.praseJsonToModel(json,OrderInfo.class);
                            list.add(dataBean);
                            adapter.notifyDataSetChanged();
                            refreshLayout.setNoMoreData(true);
                            break;
                        case 400:
                            String s = response.errorBody().string();
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
                MyToast.show(getApplicationContext(),"网络原因，获取失败");
            }
        });
    }
}
