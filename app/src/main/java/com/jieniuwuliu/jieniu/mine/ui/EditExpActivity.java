package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class EditExpActivity extends BaseActivity {
    @BindView(R.id.et_context)
    EditText etContext;
    private MyLoading loading;
    private String info,token;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_exp;
    }

    @Override
    protected void init() {
        loading = new MyLoading(this,R.style.CustomDialog);
        info = getIntent().getStringExtra("info");
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
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
                    MyToast.show(EditExpActivity.this,"修改的内容不能为空");
                    return;
                }
                update(context);
                break;
        }
    }

    private void update(String context) {
        loading.show();
        try {
            JSONObject object = new JSONObject();
            object.put("storeinform",context);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).modifyStoreInfo(body);
            call.enqueue(new SimpleCallBack<ResponseBody>(EditExpActivity.this) {
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
                        MyToast.show(EditExpActivity.this, object.getString("msg"));
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
