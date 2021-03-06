package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Coupon;
import com.jieniuwuliu.jieniu.jijian.JiJianActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.mine.adapter.TicketAdater;
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
import retrofit2.Call;
import retrofit2.Response;

/**
 * 优惠券
 */
public class MyTicketActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener, OnLoadMoreListener{
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    private TicketAdater adapter;
    private List<Coupon.DataBean> list;
    private String token;
    private int page = 1,pageNum = 10;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_ticket;
    }

    @Override
    protected void init() {
        title.setText("优惠券");
        loading = new MyLoading(this,R.style.CustomDialog);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new TicketAdater(this,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        getCoupons();
    }
    /**
     * 获取优惠券列表
     * */
    private void getCoupons() {
        loading.show();
        Call<Coupon> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getCoupons(page,pageNum,1);
        call.enqueue(new SimpleCallBack<Coupon>(MyTicketActivity.this) {
            @Override
            public void onSuccess(Response<Coupon> response) {
                loading.dismiss();
                try{
                    if (refreshLayout!=null){
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                    if (response.body().getData().size()!=0){
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }else{
                        refreshLayout.setNoMoreData(true);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<Coupon> response) {
                loading.dismiss();
                try{
                    if (refreshLayout!=null){
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                    String error = response.errorBody().string();
                    JSONObject object = new JSONObject(error);
                    MyToast.show(MyTicketActivity.this, object.getString("msg"));
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


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (list.get(position).isUse()){
            MyToast.show(MyTicketActivity.this,"不能使用这张优惠券");
        }else if (list.get(position).isExpried()){
            MyToast.show(MyTicketActivity.this,"这张优惠券已过期");
        }else{
            Intent intent = new Intent();
            intent.setClass(this,JiJianActivity.class);
            intent.putExtra("data",list.get(position));
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        list.clear();
        page = 1;
        getCoupons();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getCoupons();
    }
}
