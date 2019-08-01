package com.jieniuwuliu.jieniu.qipeishang;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Car;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.StoreInfoBean;
import com.jieniuwuliu.jieniu.qipeishang.adapter.StoreCarAdapter;
import com.jieniuwuliu.jieniu.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 门店详情
 */
public class QPSORQXInfoActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.layout_yewu)
    LinearLayout layoutYewu;
    @BindView(R.id.layout_car)
    LinearLayout layoutCar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_yewu)
    TextView tvYewu;
    @BindView(R.id.tv_context)
    TextView tvContext;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv)
    RecyclerView rv;
    private String token;
    private Intent intent;
    private int id;
    private StoreCarAdapter adapter;
    private List<Car> cars;
    private StoreInfoBean storeBean;
    private List<String> imgUrls;
    private String[] permissions = new String[]{Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS};
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qi_pei_shang_info;
    }

    @Override
    protected void init() {
        imgUrls = new ArrayList<>();
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
                            tvName.setText(storeBean.getNickname());
                            name.setText(storeBean.getAddress().getName());
                            String phoneStr = storeBean.getAddress().getPhone();
                            if (phoneStr.length() == 11) {
                                String str1 = phoneStr.substring(0, 3);
                                String str2 = phoneStr.substring(8, 11);
                                phoneStr = str1 + "******" + str2;
                            } else {
                                String str1 = phoneStr.substring(0, 3);
                                String str2 = phoneStr.substring(5, 8);
                                phoneStr = str1 + "**" + str2;
                            }
                            phone.setText(phoneStr);
                            tvWechat.setText(storeBean.getWechat());
                            address.setText(storeBean.getAddress().getAddress());
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
                                imgUrls.add(storeBean.getShopPhoto());
                            }
                            if (!storeBean.getPhotos().equals("")) {
                                try {
                                    JSONArray array = new JSONArray(storeBean.getPhotos());
                                    for (int i = 0; i < array.length(); i++) {
                                        imgUrls.add(array.get(i).toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setViewPage(imgUrls);
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
     * 设置轮播图
     * */
    private void setViewPage(List<String> imgUrls) {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imgUrls);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @OnClick({R.id.back, R.id.tv_fuzhi, R.id.tv_follow, R.id.btn, R.id.msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_follow://关注/取消关注
                if (storeBean.isFollow()) {
                    cancelFollow();
                } else {
                    addFollow();
                }
                break;
            case R.id.tv_fuzhi://复制微信号
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Label", storeBean.getWechat());
                manager.setPrimaryClip(clipData);
                MyToast.show(this, "复制成功");
                break;
            case R.id.btn://打电话
                if (Build.VERSION.SDK_INT >= 23){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,permissions,100);
                        return;
                    }
                }
                Constant.CALLPHONE = storeBean.getAddress().getPhone();
                Constant.isCall = false;
                callPhone();
                break;
            case R.id.msg:
                MyToast.show(getApplicationContext(),"该功能暂未开放");
                break;
        }
    }
    /**拨打电话*/
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + storeBean.getAddress().getPhone());
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 添加关注
     * */
    private void addFollow() {
        Map<String,Object> map = new HashMap<>();
        map.put("fUid",storeBean.getUid());
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).addFollow(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            MyToast.show(QPSORQXInfoActivity.this, "关注成功");
                            tvFollow.setText("已关注");
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            Log.w("result",s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(QPSORQXInfoActivity.this, object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
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
     * */
    private void cancelFollow() {
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).deleteFollow(storeBean.getUid());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    switch (response.code()){
                        case 200:
                            MyToast.show(QPSORQXInfoActivity.this, "取消成功");
                            tvFollow.setText("关注");
                            break;
                        case 400:
                            String s = response.errorBody().string();
                            Log.w("result",s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(QPSORQXInfoActivity.this, object.getString("msg"));
                            break;
                    }
                }catch (Exception e){
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
        if (requestCode == 100){
            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                MyToast.show(this,"请允许拨号权限后再试");
            } else {//成功
//                callPhone();
            }
        }
    }
}
