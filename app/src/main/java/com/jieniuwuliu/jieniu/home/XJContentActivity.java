package com.jieniuwuliu.jieniu.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jieniuwuliu.jieniu.home.adapter.XJBJListAdapter;
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
import butterknife.OnLongClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class XJContentActivity extends BaseActivity implements XjContentAdapter.CallBack {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.layout_no)
    LinearLayout layoutNo;
    @BindView(R.id.tv_no)
    TextView tvNo;
    private XJOrder.DataBean data;
    private String token;
    private MyLoading loading;
    private List<XjInfo.DataBean> list;
    private XjContentAdapter adapter;
    public static int state;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xjcontent;
    }

    @Override
    protected void init() {
        data = (XJOrder.DataBean) getIntent().getSerializableExtra("data");
        if ("暂无车架号".equals(data.getCarvin())||"".equals(data.getCarvin())){
            layoutNo.setVisibility(View.GONE);
        }else{
            layoutNo.setVisibility(View.VISIBLE);
            tvNo.setText(data.getCarvin());
        }
        state = data.getStype();
        loading = new MyLoading(this,R.style.CustomDialog);
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new XjContentAdapter(this,list);
        adapter.setCallBack(this);
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
                        tvEmpty.setVisibility(View.GONE);
                        list.addAll(xjInfo.getData());
                        adapter.notifyDataSetChanged();
                    }else{
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                }else{
                    tvEmpty.setVisibility(View.VISIBLE);
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
    @OnLongClick(R.id.tv_no)
    public boolean onLongClicked(View view){
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Label", data.getCarvin());
        manager.setPrimaryClip(clipData);
        MyToast.show(this, "复制成功");
        return false;
    }
    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void sureInfo(XjInfo.DataBean item) {
        loading.show();
        try {
            String json = GsonUtil.listToJson(Constant.LIST);
            JSONObject object = new JSONObject();
            object.put("ID",data.getId());
            object.put("Pid",item.getPid());
            object.put("Partslist",json);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).putXJOrderInfo(body);
            call.enqueue(new SimpleCallBack<ResponseBody>(this) {
                @Override
                public void onSuccess(Response<ResponseBody> response) {
                    loading.dismiss();
                    MyToast.show(getApplicationContext(),"购买成功");
                    finish();
                }

                @Override
                public void onFail(int errorCode, Response<ResponseBody> response) {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
