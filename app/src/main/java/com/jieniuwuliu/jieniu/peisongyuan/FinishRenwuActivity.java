package com.jieniuwuliu.jieniu.peisongyuan;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.messageEvent.MessageEvent;
import com.jieniuwuliu.jieniu.mine.ui.AddPicActivity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishRenwuActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.img)
    ImageView img;
    private final int CAMERA_CODE = 1002;//请求相机的请求码
    private File pictureFile;
    private String imgUrl,token,orderNo;
    private MyLoading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_finish_renwu;
    }

    @Override
    protected void init() {
        title.setText("拍照上传");
        right.setText("拍照");
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        orderNo = getIntent().getStringExtra("orderNo");
    }


    @OnClick({R.id.back, R.id.right,R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
                        return;
                    }
                    int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                        return;
                    }
                    int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (read != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                        return;
                    }
                }
                pictureFile = FileUtil.createImageFile(this);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
                    uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                }else {
                    uri = Uri.fromFile(pictureFile);
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, CAMERA_CODE);
                break;
            case R.id.tv_sure:
                loading.show();
                update();
                break;
        }
    }
    /**
     * 上传图片到腾讯云
     * */
    private void update() {
        COSXMLUploadTask storeTask =  UpLoadFileUtil.getIntance(FinishRenwuActivity.this).upload("img",new File(imgUrl).getName(),imgUrl);
        storeTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Log.w("返回结果","Success: " +result.accessUrl);
                updateService(result.accessUrl);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                MyToast.show(getApplicationContext(),(exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
    }
    /**
     * 将结果反馈给服务端
     * */
    private void updateService(String accessUrl) {
        try {
            JSONObject object = new JSONObject();
            object.put("finishPhoto",accessUrl);
            object.put("status",5);
            String json = object.toString();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).updateOrder(orderNo,body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    loading.dismiss();
                    try {
                        switch (response.code()){
                            case 200:
                                finish();
                                break;
                            case 400:
                                String s = response.errorBody().string();
                                Log.w("result",s);
                                JSONObject object = new JSONObject(s);
                                MyToast.show(getApplicationContext(), object.getString("msg"));
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CODE://相机
                imgUrl = pictureFile.getPath();
                Log.w("img",imgUrl);
                GlideUtil.setLocalImgUrl(FinishRenwuActivity.this,imgUrl,img);
                break;
        }
    }
}
