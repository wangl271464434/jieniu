package com.jieniuwuliu.jieniu;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.WelComeBean;
import com.jieniuwuliu.jieniu.listener.OnDownloadListener;
import com.jieniuwuliu.jieniu.util.DownloadUtils;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.view.Action;
import com.jieniuwuliu.jieniu.view.CountdownView;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;
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
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.gifView)
    GifImageView gifView;

    private String token, url,welComePath;
    private int userType;

    //    private boolean isGuide;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this, Constant.TOKEN, Constant.TOKEN, "");
        welComePath = (String) SPUtil.get(this, Constant.WELCOMEPATH, Constant.WELCOMEPATH, "");
        userType = (int) SPUtil.get(this, Constant.USERTYPE, Constant.USERTYPE, 0);
        getImg();
        CountDownTimer timer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                if (url!=null){
                    if (url.contains(".gif")) {
                        img.setVisibility(View.GONE);
                        gifView.setVisibility(View.VISIBLE);
                    } else {
                        gifView.setVisibility(View.GONE);
                        GlideUtil.setImgUrl(WelcomeActivity.this, url, img);
                    }
                }
                tvEnter.setVisibility(View.VISIBLE);
                tvEnter.start();
            }
        };
        timer.start();
        tvEnter.setOnFinishAction(new Action() {
            @Override
            public void onAction() {
                enter();
            }
        });
    }
    private String getAppPath() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return storageDir.getPath();
    }

    private void getImg() {
        Call<WelComeBean> call = HttpUtil.getInstance().getApi(token).getImg();
        call.enqueue(new Callback<WelComeBean>() {
            @Override
            public void onResponse(Call<WelComeBean> call, Response<WelComeBean> response) {
                try {
                    url = response.body().getData();
                    url = url.replace("get imgurlgif: ", "");
                    Log.i("welcomeImg", url);
                    if (url.contains(".gif")){
                        if (welComePath.equals(url)){
                            GifDrawable gifDrawable = new GifDrawable(getAppPath() + "/welcome.gif");
                            gifView.setBackground(gifDrawable);
                        }else{
                            downloadGif();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<WelComeBean> call, Throwable t) {

            }
        });
    }
    /**下载GIF*/
    private void downloadGif() {
        SPUtil.put(this,Constant.WELCOMEPATH,Constant.WELCOMEPATH,url);
        DownloadUtils downloadUtils = new DownloadUtils();
        downloadUtils.download(url, getAppPath() + "/welcome.gif");
        downloadUtils.setOnDownloadListener(new OnDownloadListener() {
            @Override
            public void onDownloadConnect(int fileSize) {

            }

            @Override
            public void onDownloadUpdate(int position) {

            }

            @Override
            public void onDownloadComplete(String url) {
                try {
                    GifDrawable gifDrawable = new GifDrawable(getAppPath() + "/welcome.gif");
                    gifView.setBackground(gifDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDownloadError(Exception e) {

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
