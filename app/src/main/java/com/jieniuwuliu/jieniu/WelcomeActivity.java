package com.jieniuwuliu.jieniu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.WelComeBean;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.view.Action;
import com.jieniuwuliu.jieniu.view.CountdownView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.tv_enter)
    CountdownView tvEnter;
    @BindView(R.id.gifView)
    GifImageView gifView;
    @BindView(R.id.img)
    ImageView img;

    private String token;
    private int userType;
//    private boolean isGuide;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        userType = (int) SPUtil.get(this, Constant.USERTYPE, Constant.USERTYPE, 0);
//        isGuide = (boolean) SPUtil.get(this, Constant.GUIDE, Constant.GUIDE, false);
//        tvEnter.start();
        tvEnter.setOnFinishAction(new Action() {
            @Override
            public void onAction() {
                enter();
            }
        });
        getImg();
    }

    private void getImg() {
        Call<WelComeBean> call = HttpUtil.getInstance().getApi(token).getImg();
        call.enqueue(new Callback<WelComeBean>() {
            @Override
            public void onResponse(Call<WelComeBean> call, Response<WelComeBean> response) {
                try {
                    String url = response.body().getData();
                    url = url.replace("get imgurlgif: ","");
                    Log.i("welcomeImg", url);
                    if(url.contains(".gif")){
                        gifView.setVisibility(View.VISIBLE);
                    }else{
                        img.setVisibility(View.VISIBLE);
                        GlideUtil.setImgUrl(WelcomeActivity.this,url,img);
                    }
                    tvEnter.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WelComeBean> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.tv_enter)
    public void onViewClicked() {
        enter();
    }

    /**
     * 进入具体界面
     */
    private void enter() {
        if (!token.equals("")) {
            if (userType == 5 || userType == 6) {
                startAcy(LoginActivity.class);
            } else {
                startAcy(MainActivity.class);
            }
        } else {
            startAcy(LoginActivity.class);
        }
      /*  if (isGuide) {

        } else {
            startAcy(GuideActivity.class);
        }*/
        finish();
    }
}
