package com.jieniuwuliu.jieniu.jijian;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.KeyboardUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.SearchStore;
import com.jieniuwuliu.jieniu.jijian.adapter.SearchStoreAdapter;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 搜索门店
 */
public class SearchStoreActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.empty)
    TextView empty;
    private MyLoading loading;
    private String token;
    private List<SearchStore.DataBean> list;
    private SearchStoreAdapter adapter;
    private int page = 1,pageNum = 10;
    private String info;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_store;
    }

    @Override
    protected void init() {
        info = getIntent().getStringExtra("info");
        list = new ArrayList<>();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new SearchStoreAdapter(this,list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        refreshLayout.setNoMoreData(true);
        if (!info.equals("")){
            etSearch.setText(info);
            etSearch.setSelection(info.length());
            empty.setVisibility(View.GONE);
            search(info);
        }else{
            empty.setText("未搜索到内容，请返回添加门店");
            empty.setVisibility(View.VISIBLE);
        }
    }
    @OnClick({R.id.back, R.id.img_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_search:
                KeyboardUtil.hideSoftKeyboard(this);
               info = etSearch.getText().toString();
                if (info.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入搜索内容");
                    return;
                }
                list.clear();
                search(info);
                break;
        }
    }
    /**
     * 搜索
     * */
    private void search(String info) {
        loading.show();
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).searchStore(info,page,pageNum);
        call.enqueue(new SimpleCallBack<ResponseBody>(SearchStoreActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    if (refreshLayout!=null){
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                    SearchStore searchStore = (SearchStore) GsonUtil.praseJsonToModel(response.body().string(),SearchStore.class);
                    list.addAll(searchStore.getData());
                    adapter.notifyDataSetChanged();
                    if (searchStore.getData().size()<10){
                        refreshLayout.setNoMoreData(true);
                    }else{
                        refreshLayout.setNoMoreData(false);
                    }
                    if (list.size()!=0){
                        empty.setVisibility(View.GONE);
                    }else{
                        empty.setText("未搜索到内容，请返回添加门店");
                        empty.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
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

    @Override
    public void onItemClick(View view, int position) {
        CarEvent event = new CarEvent();
        event.setStore(list.get(position));
        EventBus.getDefault().post(event);
        finish();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        empty.setVisibility(View.VISIBLE);
        refreshLayout.finishRefresh();
        refreshLayout.setNoMoreData(true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        search(info);
    }
}
