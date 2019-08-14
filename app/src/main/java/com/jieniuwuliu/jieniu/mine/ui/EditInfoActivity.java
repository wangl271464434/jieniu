package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
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
 * 修改门店信息
 */
public class EditInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_context)
    EditText etContext;
    private String title,info,token;
    private int aid;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        title = getIntent().getStringExtra("title");
        info = getIntent().getStringExtra("info");
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        aid = getIntent().getIntExtra("aid",0);
        tvTitle.setText("修改"+title);
        if (title.equals("联系电话")){
            etContext.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        etContext.setText(info);
        etContext.setSelection(etContext.getText().length());
    }

    @OnClick({R.id.back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_save:
                String context = etContext.getText().toString();
                if (context.isEmpty()){
                    MyToast.show(EditInfoActivity.this,"修改的内容不能为空");
                    return;
                }
                if (title.equals("联系电话")||title.equals("联系人")){
                    updateAdr(context);
                }else {
                    update(context);
                }
                break;
        }
    }
    /**
     * 修改联系电话和联系人
     * */
    private void updateAdr(final String context) {
        loading.show();
        Map<String,Object> map = new HashMap<>();
        switch (title){
            case "联系电话":
                map.put("phone",context);
                break;
            case "联系人":
                map.put("name",context);
                break;
        }
        map.put("default",true);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), GsonUtil.mapToJson(map));
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).updateAddress(aid,body);
        call.enqueue(new SimpleCallBack<ResponseBody>(EditInfoActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    Log.w("json",response.body().toString());
                    Intent intent = new Intent();
                    intent.putExtra("info",context);
                    setResult(RESULT_OK,intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try{
                    String s = response.errorBody().string();
                    Log.w("result", s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(EditInfoActivity.this, object.getString("msg"));
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

    /**
     * 修改主营业务和微信
     * */
    private void update(final String context) {
        loading.show();
        Map<String,Object> map = new HashMap<>();
        switch (title){
            case "主营业务":
                map.put("yewu",context);
                break;
            case "固定电话":
                map.put("landline",context);
                break;
            case "经营范围":
                map.put("yewu",context);
                break;
            case "门店简介":
                map.put("storeinform",context);
                break;
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), GsonUtil.mapToJson(map));
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).modifyStoreInfo(body);
        call.enqueue(new SimpleCallBack<ResponseBody>(EditInfoActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                Intent intent = new Intent();
                intent.putExtra("info",context);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result", s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(EditInfoActivity.this, object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
}
