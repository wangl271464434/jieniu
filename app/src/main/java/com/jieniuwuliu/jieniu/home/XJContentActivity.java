package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XJOrder;
import com.jieniuwuliu.jieniu.bean.XjInfo;
import com.jieniuwuliu.jieniu.home.adapter.XjContentAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class XJContentActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private XJOrder.DataBean data;
    private String token;
    private MyLoading loading;
    private List<XjInfo.DataBean> list;
    private XjContentAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xjcontent;
    }

    @Override
    protected void init() {
        data = (XJOrder.DataBean) getIntent().getSerializableExtra("data");
        loading = new MyLoading(this,R.style.CustomDialog);
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new XjContentAdapter(this,list);
        recyclerView.setAdapter(adapter);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        GlideUtil.setImgUrl(this,data.getLogos(),img);
        tvTime.setText(data.getCreatedat());
        tvName.setText(data.getCarbrand());
        List<Object> list = GsonUtil.praseJsonToList(data.getPartslist(), Machine.class);
        String info = "";
        for (int j = 0;j<list.size();j++){
            Machine machine = (Machine) list.get(j);
            if (j==0){
                info += machine.getName();
            }else{
                info += ","+machine.getName();
            }
        }
        tvInfo.setText("配件："+info);
        getData();
    }
    /**
     * 获取数据
     * */
    private void getData() {
        loading.show();
        Call<XjInfo> call = HttpUtil.getInstance().getApi(token).getXJOrderInfo(data.getId());
        call.enqueue(new SimpleCallBack<XjInfo>(this) {
            @Override
            public void onSuccess(Response<XjInfo> response) {
                loading.dismiss();
                XjInfo xjInfo = response.body();
                if (xjInfo.getData()!=null){
                    if (xjInfo.getData().size()>0){
                        list.addAll(xjInfo.getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFail(int errorCode, Response<XjInfo> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getApplicationContext(), object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e){
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

    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }

}
