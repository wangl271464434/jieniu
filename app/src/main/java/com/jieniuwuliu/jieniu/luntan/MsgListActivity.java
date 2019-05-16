package com.jieniuwuliu.jieniu.luntan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
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
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LoginBean;
import com.jieniuwuliu.jieniu.bean.UnReadMsg;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.luntan.adapter.UnReadAdapter;
import com.jieniuwuliu.jieniu.mine.ui.MyTicketActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

public class MsgListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, OnItemClickListener {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rv)
    RecyclerView rv;
    private String token;
    private int page =1,num = 10;
    private MyLoading loading;
    private UnReadAdapter adapter;
    private List<UnReadMsg.DataBean> list;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_list;
    }

    @Override
    protected void init() {
        title.setText("消息列表");
        list = new ArrayList<>();
        loading = new MyLoading(this,R.style.CustomDialog);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new UnReadAdapter(this,list);
        rv.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.line_divider));
        rv.addItemDecoration(divider);
        adapter.setOnItemClickListener(this);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        getUnReadMsgList();
    }
    /**
     * 未读消息列表
     * */
    private void getUnReadMsgList() {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getUnReadList(page,num);
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
                            UnReadMsg unReadMsg = (UnReadMsg) GsonUtil.praseJsonToModel(response.body().string(),UnReadMsg.class);
                            if (unReadMsg.getData()!=null){
                                if (unReadMsg.getData().size()<10){
                                    refreshLayout.setNoMoreData(true);
                                }
                                list.addAll(unReadMsg.getData());
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        case 400:
                            String error = response.errorBody().string();
                            JSONObject object = new JSONObject(error);
                            MyToast.show(MsgListActivity.this, object.getString("msg"));
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

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        list.clear();
        getUnReadMsgList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getUnReadMsgList();
    }

    @Override
    public void onItemClick(View view, int position) {
      /*  Intent intent = new Intent();
        intent.setClass(MsgListActivity.this,LuntanInfoActivity.class);
        intent.putExtra("data",list.get(position));
        startActivity(intent);*/
        updateData(list.get(position));

    }
    /**
     * 蒋某一条未读消息设置为已读消息
     * */
    private void updateData(UnReadMsg.DataBean dataBean) {
        loading.show();
        Map<String,Object> map = new HashMap<>();
        map.put("cid",dataBean.getId());
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getReadMsg(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    switch (response.code()){
                        case 200:
                            Intent intent = new Intent();
                            intent.setClass(MsgListActivity.this,LuntanInfoActivity.class);
                            intent.putExtra("data",dataBean);
                            startActivity(intent);
                            list.remove(dataBean);
                            adapter.notifyDataSetChanged();
                            break;
                        case 400:
                            String error = response.errorBody().string();
                            JSONObject object = new JSONObject(error);
                            MyToast.show(MsgListActivity.this, object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Log.i("ERROR","error is:"+t.toString());
            }
        });

    }
}
