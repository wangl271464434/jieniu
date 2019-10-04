package com.jieniuwuliu.jieniu.mine.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.bean.WeChatInfo;
import com.jieniuwuliu.jieniu.bean.WeChatToken;
import com.jieniuwuliu.jieniu.messageEvent.WeChatEvent;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***
 * 绑定微信
 * */
public class BindWechatActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_wechat_name)
    TextView tvWechatName;
    @BindView(R.id.tv_bind)
    TextView tvBind;
    private String token;
    private IWXAPI api;
    private MyLoading loading;
    private String wxName,unionid,openid;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_wechat;
    }

    @Override
    protected void init() {
         title.setText("绑定微信");
         wxName = getIntent().getStringExtra("wxName");
        unionid = getIntent().getStringExtra("unionid");
        openid = getIntent().getStringExtra("openid");
        if (unionid!=null){
            if (unionid.equals("-1")||unionid.equals("")){
                tvWechatName.setText("未绑定");
                tvBind.setText("绑定微信");
            }else{
                tvWechatName.setText(wxName);
                tvBind.setText("解除绑定");
            }
        }else{
            tvWechatName.setText("未绑定");
            tvBind.setText("绑定微信");
        }

         loading = new MyLoading(this,R.style.CustomDialog);
         EventBus.getDefault().register(this);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WeChatEvent event) {
        Log.i("wechat",event.toString());
        if (!event.getCode().equals("")){
            loading.show();
            getAccessToken(event.getCode());
        }
    }
    /**
     * 获取微信access_token
     *
     * @param code*/
    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constant.WXAPPID+"&secret="+Constant.WXSERCET+"&code="+code+"&grant_type=authorization_code";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyToast.show(getApplicationContext(),"获取accesstoken失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                WeChatToken weChatToken = (WeChatToken) GsonUtil.praseJsonToModel(s,WeChatToken.class);
                openid = weChatToken.getOpenid();
                getWeChatInfo(weChatToken.getAccess_token(),openid);
            }
        });
    }
    /**
     * 获取个人信息
     * @param access_token
     * @param openid
     * */
    private void getWeChatInfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyToast.show(getApplicationContext(),"获取个人信息失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                WeChatInfo info = (WeChatInfo) GsonUtil.praseJsonToModel(s,WeChatInfo.class);
                Log.i("wechatinfo",info.toString());
                wxName = info.getNickname();
                unionid = info.getUnionid();
                bindInfo();
            }
        });
    }

    private void bindInfo() {
        try{
            JSONObject object = new JSONObject();
            object.put("wxName",wxName);
            object.put("unionid",unionid);
            object.put("openid",openid);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),object.toString());
            retrofit2.Call<UserBean> observable = HttpUtil.getInstance().createRetrofit().create(HttpApi.class).modifyUserInfo(body);
            observable.enqueue(new SimpleCallBack<UserBean>(BindWechatActivity.this) {
                @Override
                public void onSuccess(retrofit2.Response<UserBean> response) {
                    loading.dismiss();
                    if (unionid.equals("-1")){
                        MyToast.show(getApplicationContext(),"解绑成功");
                        tvWechatName.setText("未绑定");
                        tvBind.setText("绑定微信");
                    }else{
                        MyToast.show(getApplicationContext(),"绑定成功");
                        tvWechatName.setText(wxName);
                        tvBind.setText("解除绑定");
                    }
                }

                @Override
                public void onFail(int errorCode, retrofit2.Response<UserBean> response) {
                    loading.dismiss();
                    try {
                        String s = response.errorBody().string();
                        Log.w("result", s);
                        JSONObject object = new JSONObject(s);
                        MyToast.show(BindWechatActivity.this, object.getString("msg"));
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

    @OnClick({R.id.back, R.id.tv_bind,R.id.img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_bind:
                String info = tvBind.getText().toString();
                if (info.equals("绑定微信")){
                    bindWeChat();
                }else{
                    unBindWeChat();
                }
                break;
            case R.id.img:
                show();
                break;
        }
    }
    /**
     * 显示保存图片的弹窗
     * */
    private void show() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setBackgroundDrawableResource(R.drawable.bg_white_shape);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setContentView(R.layout.save_img_dialog);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvSave = dialog.findViewById(R.id.tv_save);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveImg();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void saveImg() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.jn_ggh);
        saveImageToGallery(this,bmp);
        MyToast.show(getApplicationContext(),"保存成功");
    }

    private void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }

    /**
     * 解绑微信
     * */
    private void unBindWeChat() {
        wxName = "-1";
        unionid = "-1";
        openid = "-1";
        bindInfo();
    }

    /**
     * 绑定微信
     * */
    private void bindWeChat() {
        if (api == null){
            api = WXAPIFactory.createWXAPI(this, Constant.WXAPPID, true);
        }
        SendAuth.Req req = new SendAuth.Req();
         req.scope = "snsapi_userinfo";
        req.state = "wx_login_duzun";
        api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
