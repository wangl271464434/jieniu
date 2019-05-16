package com.jieniuwuliu.jieniu.peisongyuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.PSYUser;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.PSYEvent;
import com.jieniuwuliu.jieniu.peisongyuan.adapter.PSYListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
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

public class PSYListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, OnItemClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    private int page = 1,num = 10;
    private String token;
    private List<PSYUser.DataBean> list;
    private PSYListAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_psylist;
    }

    @Override
    protected void init() {
        title.setText("配送员列表");
        list = new ArrayList<>();
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new PSYListAdapter(this,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        getData();
    }
    /**
     * 获取配送员列表
     * */
    private void getData() {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getKuaiDiList(page,num);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            if (refreshLayout!=null){
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                            }
                            PSYUser user = (PSYUser) GsonUtil.praseJsonToModel(response.body().string(),PSYUser.class);
                            list.addAll(user.getData());
                            if (user.getData().size()<10){
                                refreshLayout.setNoMoreData(true);
                            }
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            Log.w("result",s);
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
                Log.e("error",t.toString());
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page =1;
        list.clear();
        getData();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.putExtra("id",list.get(position).getId());
        setResult(RESULT_OK,intent);
        finish();
    }
}
