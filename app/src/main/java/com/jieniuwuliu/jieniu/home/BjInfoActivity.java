package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.BJOrder;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XJImg;
import com.jieniuwuliu.jieniu.home.adapter.BJInfoAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class BjInfoActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.layout_img)
    LinearLayout layoutImg;
    private BJOrder.DataBean data;
    private String token;
    private List<Machine> list;
    private MyLoading loading;
    private BJInfoAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bj_info;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this, R.style.CustomDialog);
        list = new ArrayList<>();
        data = (BJOrder.DataBean) getIntent().getSerializableExtra("data");
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        GlideUtil.setImgUrl(this, data.getLogos(), img);
        tvName.setText(data.getCarbrand());
        tvTime.setText("发布时间：" + data.getCreatedAt());
        if (data.getRemarks().equals("")){
            tvRemark.setText("备注：无");
        }else{
            tvRemark.setText("备注："+data.getRemarks());
        }
        if (data.getPartsphoto().equals("")){
            layoutImg.setVisibility(View.GONE);
        }else{
            XJImg xjImg = (XJImg) GsonUtil.praseJsonToModel(data.getPartsphoto(),XJImg.class);
            if (xjImg.getCjUrl().equals("")&&xjImg.getCtUrl().equals("")&&xjImg.getPjUrl().equals("")){
                layoutImg.setVisibility(View.GONE);
            }else{
                layoutImg.setVisibility(View.VISIBLE);
                if (xjImg.getCjUrl().equals("")){
                    img1.setVisibility(View.GONE);
                }else {
                    img1.setVisibility(View.VISIBLE);
                    GlideUtil.setImgUrl(BjInfoActivity.this,xjImg.getCjUrl(),R.mipmap.loading,img1);
                }
                if (xjImg.getCtUrl().equals("")){
                    img2.setVisibility(View.GONE);
                }else {
                    img2.setVisibility(View.VISIBLE);
                    GlideUtil.setImgUrl(BjInfoActivity.this,xjImg.getCtUrl(),R.mipmap.loading,img2);
                }
                if (xjImg.getPjUrl().equals("")){
                    img3.setVisibility(View.GONE);
                }else {
                    img3.setVisibility(View.VISIBLE);
                    GlideUtil.setImgUrl(BjInfoActivity.this,xjImg.getPjUrl(),R.mipmap.loading,img3);
                }
            }
        }
        List<Object> objects = GsonUtil.praseJsonToList(data.getPartslist(), Machine.class);
        for (Object object : objects) {
            Machine machine = (Machine) object;
            list.add(machine);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new BJInfoAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.layout_back, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn:
                addBjInfo();
                break;
        }
    }
    private void addBjInfo() {
        loading.show();
        String json = GsonUtil.listToJson(list);
        try {
            JSONObject object = new JSONObject();
            object.put("ID", data.getID());
            object.put("Partslist", json);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).addBJInfo(body);
            call.enqueue(new SimpleCallBack<ResponseBody>(this) {
                @Override
                public void onSuccess(Response<ResponseBody> response) {
                    loading.dismiss();
                    finish();
                }

                @Override
                public void onFail(int errorCode, Response<ResponseBody> response) {
                    loading.dismiss();
                    try {
                        String s = response.errorBody().string();
                        Log.w("result", s);
                        JSONObject object = new JSONObject(s);
                        MyToast.show(getApplicationContext(), object.getString("msg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNetError(String s) {
                    loading.dismiss();
                    MyToast.show(getApplicationContext(), s);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
