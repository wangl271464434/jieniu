package com.jieniuwuliu.jieniu.mine.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;

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
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.et_context)
    EditText etContext;
    private String token;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
    }

    @OnClick({R.id.back, R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.commit:
                String info = etContext.getText().toString();
                if (info.isEmpty()){
                    MyToast.show(getApplicationContext(),"请输入您的意见或者建议！！！");
                    return;
                }
                submit(info);
                break;
        }
    }
      /**
       * 提交意见
       *
       * @param info*/
    private void submit(String info) {
        Map<String,Object> map = new HashMap<>();
        map.put("info",info);
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).feedBack(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()){
                    case 200:
                        MyToast.show(getApplicationContext(),"提交成功");
                        finish();
                        break;
                    case 400:
                        MyToast.show(getApplicationContext(),"提交失败");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyToast.show(getApplicationContext(),"请求失败");
            }
        });
    }
}
