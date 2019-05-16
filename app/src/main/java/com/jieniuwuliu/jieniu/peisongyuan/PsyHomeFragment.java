package com.jieniuwuliu.jieniu.peisongyuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.ScanQCActivity;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.PSOrderInfo;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.peisongyuan.adapter.RenwuAdapter;
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

public class PsyHomeFragment extends BaseFragment implements OnItemClickListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private RenwuAdapter adapter;
    private Intent intent;
    private String type = "daijie";// paisong  wancheng
    private String token;
    private int page = 1,pageNum = 10;
    private MyLoading loading;
    private List<PSOrderInfo.DataBean> list;
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.psy_home;
    }

    @Override
    protected void init() {
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        list = new ArrayList<>();
        loading = new MyLoading(getActivity(),R.style.CustomDialog);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(manager);
        adapter = new RenwuAdapter(getActivity(),list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        token = (String) SPUtil.get(getActivity(), Constant.TOKEN, Constant.TOKEN, "");

    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        getData();
    }
    /**
     * 获取数据
     * */
    private void getData() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getPSorderList(type,page,pageNum);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                            }
                            PSOrderInfo psOrderInfo = (PSOrderInfo) GsonUtil.praseJsonToModel(response.body().string(),PSOrderInfo.class);
                            if (psOrderInfo.getData().size()<10){
                                refreshLayout.setNoMoreData(true);
                            }
                            list.addAll(psOrderInfo.getData());
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            JSONObject object = new JSONObject(s);
                            MyToast.show(getActivity(), object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_scan,R.id.radio_btn1, R.id.radio_btn2, R.id.radio_btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_scan:
                intent = new Intent();
                intent.setClass(getActivity(),ScanQCActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.radio_btn1://未接单
                type = "daijie";
                page = 1;
                list.clear();
                getData();
                break;
            case R.id.radio_btn2://派送中
                type = "paisong";
                page = 1;
                list.clear();
                getData();
                break;
            case R.id.radio_btn3://已完成
                type = "wancheng";
                page = 1;
                list.clear();
                getData();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        intent = new Intent();
        intent.setClass(getActivity(),RenwuInfoActivity.class);
        intent.putExtra("data",list.get(position));
        intent.putExtra("type",type);
        getActivity().startActivity(intent);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page =1;
        list.clear();
        getData();
    }
}
