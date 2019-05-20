package com.jieniuwuliu.jieniu.mine.ui;

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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.messageEvent.MessageEvent;
import com.jieniuwuliu.jieniu.bean.StoreCerity;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.jieniuwuliu.jieniu.view.PicDialog;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 添加图片
 */
public class AddPicActivity extends BaseActivity implements PicDialog.CallBack {

    @BindView(R.id.store_img)
    ImageView storeImg;
    @BindView(R.id.zizhi_img)
    ImageView zizhiImg;

    private int type;//0表示门店照片1表示营业执照
    private final int PIC_CODE = 1001;//请求系统相册的请求码
    private final int CAMERA_CODE = 1002;//请求相机的请求码
    private String storeImgUrl="",zizhiImgUrl="";
    private static final String FILE_PROVIDER_AUTHORITY = "cn.fonxnickel.officialcamerademo.fileprovider";
    private StoreCerity storeCerity;
    private String token,storeUrl = "",zizhiUrl = "";
    private MyLoading dialog ;
    private  File pictureFile;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_pic;
    }

    @Override
    protected void init() {
        dialog = new MyLoading(this,R.style.CustomDialog);
        storeCerity = (StoreCerity) getIntent().getSerializableExtra("storeCerity");
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        Log.w("返回结果",messageEvent.toString());
        switch (messageEvent.getType()){
            case "store":
                storeUrl = messageEvent.getStoreUrl();
                break;
            case "zizhi":
                zizhiUrl = messageEvent.getZizhiUrl();
                break;
        }
        if (!storeUrl.equals("")&&!zizhiUrl.equals("")){
            storeCerity.setShopPhoto(storeUrl);
            storeCerity.setZizhiPhoto(zizhiUrl);
            storeCerity.setAuth(3);
            update();
        }
    }
    /**
     * 提交数据
     * */
    private void update() {
        String json = GsonUtil.objectToJson(storeCerity);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> observable = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).modifyStoreInfo(body);
        observable.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                dialog.cancel();
                switch (response.code()) {
                    case 200:
                        MyToast.show(AddPicActivity.this,"提交成功");
                        finish();
                        break;
                    case 400:
                        try {
                            String s = response.errorBody().string();
                            Log.w("result",s);
                            JSONObject object = new JSONObject(s);
                            MyToast.show(AddPicActivity.this, object.getString("msg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.w("error", t.toString());
            }
        });
    }

    @OnClick({R.id.back, R.id.tv_sure, R.id.store_img, R.id.zizhi_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_sure://完成
                if (storeImg.equals("")||zizhiImgUrl.equals("")){
                    MyToast.show(AddPicActivity.this,"请选择门店照片和营业执照照片");
                    return;
                }
                dialog.show();
                //上传门店照片
                COSXMLUploadTask storeTask =  UpLoadFileUtil.getIntance(AddPicActivity.this).upload("img",new File(storeImgUrl).getName(),storeImgUrl);
                storeTask.setCosXmlResultListener(new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        Log.w("返回结果","Success: " +result.accessUrl);
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.setType("store");
                        messageEvent.setStoreUrl(result.accessUrl);
                        EventBus.getDefault().post(messageEvent);
                    }

                    @Override
                    public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                        Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                        MyToast.show(getApplicationContext(),(exception == null ? serviceException.getMessage() : exception.toString()));
                    }
                });
                //上传营业执照
                COSXMLUploadTask zizhiTask =  UpLoadFileUtil.getIntance(AddPicActivity.this).upload("img",new File(zizhiImgUrl).getName(),zizhiImgUrl);
                zizhiTask.setCosXmlResultListener(new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        Log.w("返回结果","Success: " +result.accessUrl);
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.setType("zizhi");
                        messageEvent.setZizhiUrl(result.accessUrl);
                        EventBus.getDefault().post(messageEvent);
                    }

                    @Override
                    public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                        Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                        MyToast.show(getApplicationContext(),(exception == null ? serviceException.getMessage() : exception.toString()));
                    }
                });
                break;
            case R.id.store_img://添加门店照片
                type = 0;
                showPicDialog();
                break;
            case R.id.zizhi_img://添加资质照片
                type = 1;
                showPicDialog();
                break;
        }
    }

    private void showPicDialog() {
        PicDialog picDialog = new PicDialog(this,this);
    }
    /**
     * 打开系统相册
     * */
    @Override
    public void openPic() {
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
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PIC_CODE);
    }
    /**
     * 打开照相机
     * */
    @Override
    public void openCamera() {
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PIC_CODE://相册
                String imgUrl;
                Log.w("imgurl", "onActivityResult:相册 " + data.getData().toString());
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream inputStream = resolver.openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    Cursor cursor = getContentResolver().query(data.getData(),
                            new String[]{MediaStore.Images.ImageColumns.DATA},//
                            null, null, null);
                    if (cursor == null){
                        imgUrl = data.getData().getPath();
                    }else {
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        imgUrl = cursor.getString(index);
                        cursor.close();
                    }
                    switch (type){
                        case 0:
                            storeImgUrl = imgUrl;
                            storeImg.setImageBitmap(bitmap);
                            break;
                        case 1:
                            zizhiImgUrl = imgUrl;
                            zizhiImg.setImageBitmap(bitmap);
                            break;
                    }
//                    mIvDispaly.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case CAMERA_CODE://相机
                switch (type){
                    case 0:
                        storeImgUrl = pictureFile.getPath();
                        Log.w("img",storeImgUrl);
                       GlideUtil.setLocalImgUrl(AddPicActivity.this,storeImgUrl,storeImg);
                        break;
                    case 1:
                        zizhiImgUrl =  pictureFile.getPath();
                        Log.w("img",zizhiImgUrl);
                        GlideUtil.setLocalImgUrl(AddPicActivity.this,storeImgUrl,storeImg);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
