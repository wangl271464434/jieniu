package com.jieniuwuliu.jieniu.qipeishang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Car;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.StoreInfoBean;
import com.jieniuwuliu.jieniu.mine.ui.FeedBackActivity;
import com.jieniuwuliu.jieniu.qipeishang.adapter.InfoImgAdapter;
import com.jieniuwuliu.jieniu.qipeishang.adapter.StoreCarAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

/**
 * 门店详情
 */
public class QPSORQXInfoActivity extends BaseActivity{


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.layout_car)
    LinearLayout layoutCar;
    @BindView(R.id.tv_yewu)
    TextView tvYewu;
    @BindView(R.id.tv_context)
    TextView tvContext;
    @BindView(R.id.layout_yewu)
    LinearLayout layoutYewu;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.img_right)
    ImageView imgRight;
    private String token;
    private Intent intent;
    private int id;
    private StoreCarAdapter adapter;
    private List<Car> cars;
    private List<String> imgs;
    private InfoImgAdapter imgAdapter;
    private StoreInfoBean storeBean;
    private AlertDialog dialog;
    private String[] permissions = new String[]{Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qi_pei_shang_info;
    }

    @Override
    protected void init() {
        imgRight.setImageResource(R.mipmap.ic_fenxiang);
        imgs = new ArrayList<>();
        GridLayoutManager imgManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(imgManager);
        imgAdapter = new InfoImgAdapter(this, imgs);
        recyclerView.setAdapter(imgAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        cars = new ArrayList<>();
        adapter = new StoreCarAdapter(this, cars);
        rv.setAdapter(adapter);
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        id = getIntent().getIntExtra("id", 0);
        getStoreInfo(token);
    }

    /**
     * 获取门店信息
     */
    private void getStoreInfo(String token) {
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getStoreInfo(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()) {
                        case 200:
                            ResponseBody body = response.body();
                            String json = body.string();
                            storeBean = (StoreInfoBean) GsonUtil.praseJsonToModel(new JSONObject(json).getString("data"), StoreInfoBean.class);
                            title.setText(storeBean.getNickname());
                            tvName.setText(storeBean.getNickname());
                            tvPerson.setText(storeBean.getAddress().getName());
                            if ("".equals(storeBean.getStoreinform())){
                                tvContent.setText("暂无简介");
                            }else {
                                tvContent.setText(storeBean.getStoreinform());
                            }
                            if ("".equals(storeBean.getWechat())) {
                                tvWechat.setText("未设置微信号");
                            } else {
//                                tvWechat.setText("已绑定");
                                tvWechat.setText("********");
                            }

                            tvAddress.setText(storeBean.getAddress().getAddress());
                            if (storeBean.isFollow()) {
                                tvFollow.setText("已关注");
                            } else {
                                tvFollow.setText("关注");
                            }
                            switch (storeBean.getPersonType()) {
                                case 1://汽配商
                                    layoutCar.setVisibility(View.VISIBLE);
                                    layoutYewu.setVisibility(View.GONE);
                                    break;
                                case 2://汽修商
                                    layoutYewu.setVisibility(View.VISIBLE);
                                    layoutCar.setVisibility(View.GONE);
                                    tvYewu.setText("主营业务：");
                                    tvContext.setText(storeBean.getYewu());
                                    break;
                                default:
                                    tvYewu.setText("经营范围：");
                                    layoutYewu.setVisibility(View.VISIBLE);
                                    layoutCar.setVisibility(View.GONE);
                                    tvContext.setText(storeBean.getYewu());
                                    break;
                            }
                            if (!storeBean.getShopPhoto().equals("")) {
                                GlideUtil.setImgUrl(QPSORQXInfoActivity.this, storeBean.getShopPhoto(), img);
                            }
                            if (!storeBean.getPhotos().equals("")) {
                                try {
                                    JSONArray array = new JSONArray(storeBean.getPhotos());
                                    for (int i = 0; i < array.length(); i++) {
                                        imgs.add(array.get(i).toString());
                                    }
                                    imgAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (storeBean.getFuwuCar() != null) {
                                if (storeBean.getFuwuCar().size() > 0) {
                                    cars.addAll(storeBean.getFuwuCar());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            break;
                        case 400:
                            try {
                                String s = response.errorBody().string();
                                JSONObject object = new JSONObject(s);
                                MyToast.show(QPSORQXInfoActivity.this, object.getString("msg"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 拨打电话
     */
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + storeBean.getAddress().getPhone());
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 添加关注
     */
    private void addFollow() {
        Map<String, Object> map = new HashMap<>();
        map.put("fUid", storeBean.getUid());
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).addFollow(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()) {
                        case 200:
                            MyToast.show(QPSORQXInfoActivity.this, "关注成功");
                            tvFollow.setText("已关注");
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            Log.w("result", s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(QPSORQXInfoActivity.this, object.getString("msg"));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 取消关注
     */
    private void cancelFollow() {
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).deleteFollow(storeBean.getUid());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()) {
                        case 200:
                            MyToast.show(QPSORQXInfoActivity.this, "取消成功");
                            tvFollow.setText("关注");
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            Log.w("result", s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(QPSORQXInfoActivity.this, object.getString("msg"));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                MyToast.show(this, "请允许拨号权限后再试");
            } else {//成功
//                callPhone();
            }
        }
    }

    @OnClick({R.id.layout_back, R.id.tv_follow, R.id.tv_more, R.id.tv_fuzhi, R.id.layout_report, R.id.layout_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.tv_follow:
                if (storeBean.isFollow()) {
                    cancelFollow();
                } else {
                    addFollow();
                }
                break;
            case R.id.tv_more:
                break;
            case R.id.tv_fuzhi:
                if ("".equals(storeBean.getWechat())) {
                    MyToast.show(this,"未绑定微信不能复制");
                    return;
                }
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Label", storeBean.getWechat());
                manager.setPrimaryClip(clipData);
                MyToast.show(this, "复制成功");
                break;
            case R.id.layout_report:
                intent = new Intent();
                intent.setClass(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_call:
                showPhoneDialog();
                break;
        }
    }
    //电话弹窗
    @SuppressLint("SetTextI18n")
    private void showPhoneDialog() {
        dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setContentView(R.layout.dialog_store_phone);
        TextView tvTelephone = dialog.findViewById(R.id.tv_telephone);
        TextView tvPhone = dialog.findViewById(R.id.tv_phone);
        if (storeBean.getLandline().equals("")){
            tvTelephone.setText("固定电话：暂无");
        }else{
            tvTelephone.setText("固定电话：  "+storeBean.getLandline());
        }
        if (storeBean.getAddress().getPhone().equals("")){
            tvPhone.setText("移动电话：暂无");
        }else{
            tvPhone.setText("移动电话：  "+storeBean.getAddress().getPhone());
        }
        tvTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(QPSORQXInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(QPSORQXInfoActivity.this, permissions, 100);
                        return;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + storeBean.getAddress().getPhone());
                intent.setData(data);
                startActivity(intent);
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(QPSORQXInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(QPSORQXInfoActivity.this, permissions, 100);
                        return;
                    }
                }
                Constant.CALLPHONE = storeBean.getAddress().getPhone();
                Constant.isCall = false;
                callPhone();
            }
        });
    }
}
