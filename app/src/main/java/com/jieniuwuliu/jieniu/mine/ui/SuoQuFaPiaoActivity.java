package com.jieniuwuliu.jieniu.mine.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.adapter.FaPiaoAdater;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuoQuFaPiaoActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    private FaPiaoAdater adapter;
    private String token;
    private MyLoading loading;
    private List<OrderInfo> list;
    private List<OrderInfo> fapiaoList;
    private int page = 1,pageNum = 10;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_suo_qu_fa_piao;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        list = new ArrayList<>();
        fapiaoList = new ArrayList<>();
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new FaPiaoAdater(this,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        getData();
    }
    /**
     * 获取开票订单列表
     * */
    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getFaPiaoList(page,pageNum);
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

    @OnClick({R.id.back,R.id.tv_all, R.id.btn_fapiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_all:
                for (OrderInfo orderInfo:list){
                    orderInfo.setInvoice(true);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_fapiao:
                for (OrderInfo orderInfo:list){
                    if (orderInfo.isInvoice()){
                        fapiaoList.add(orderInfo);
                    }
                }
                if (fapiaoList.size()==0){
                    MyToast.show(getApplicationContext(),"请选择所要开发票的订单");
                    return;
                }
                startAcy(EditFapiaoActivity.class);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        list.clear();
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++ ;
        getData();
    }
}
