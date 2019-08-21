package com.jieniuwuliu.jieniu.mine.ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.Util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.luntan.AppearTextActivity;
import com.jieniuwuliu.jieniu.mine.adapter.AddStorePicAdapter;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.jieniuwuliu.jieniu.view.PicDialog;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
 * 添加门店照片
 */
public class AddStorePicActivity extends BaseActivity implements AddStorePicAdapter.CallBack, PicDialog.CallBack {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.rv)
    RecyclerView rv;
    private AddStorePicAdapter adapter;
    private List<String> list;
    private List<Object> objects;
    private String token;
    private int REQUEST_CODE_CHOOSE = 1000;
    private String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PIC_CODE = 1001;//请求系统相册的请求码
    private final int CAMERA_CODE = 1002;//请求相机的请求码
    private String photos;
    private List<Uri> mPictureList = new ArrayList<>();
    private MyLoading loading;
    private  File pictureFile;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_store_pic;
    }

    @Override
    protected void init() {
        title.setText("添加门店照片");
        right.setText("保存");
        loading = new MyLoading(this,R.style.CustomDialog);
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        photos = getIntent().getStringExtra("photos");
        list = new ArrayList<>();
        objects = new ArrayList<>();
        if (!photos.equals("")){
            try {
                JSONArray array = new JSONArray(photos);
                for (int i = 0;i<array.length();i++){
                    list.add(array.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        GridLayoutManager manager = new GridLayoutManager(this,3);
        rv.setLayoutManager(manager);
        adapter = new AddStorePicAdapter(this,list);
        rv.setAdapter(adapter);
        adapter.setCallBack(this);
    }

    @OnClick({R.id.back, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                loading.show();
                updateImg();
                break;
        }
    }
    /**
     * 删除图片
     * */
    @Override
    public void deletePic(int position) {
        if(list.get(position).contains("http://")){
            UpLoadFileUtil.getIntance(AddStorePicActivity.this).deleteImg(list.get(position));
        }
        list.remove(position);
        adapter.setData(list);
    }
    /**
     * 上传图片
     * */
    private void updateImg() {
        final List<String> data = new ArrayList<>();
        for (String s:list){
            if (s.contains("http://")){
                data.add(s);
                if (data.size()==list.size()){
                    updateData(data);
                }
            }else {
                COSXMLUploadTask storeTask =  UpLoadFileUtil.getIntance(AddStorePicActivity.this).upload("img",FileUtil.getFileName("storeInfo"),s);
                storeTask.setCosXmlResultListener(new CosXmlResultListener() {
                        @Override
                        public void onSuccess(CosXmlRequest request, final CosXmlResult result) {
                            Log.w("返回结果","Success: " +result.accessUrl);
                            data.add(result.accessUrl);
                            if (data.size()==list.size()){
                                updateData(data);
                            }
                        }
                        @Override
                        public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                            Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                        }
                    });
            }
        }

    }

    /**
     * 保存图片
     *
     * @param data
     * */
    private void updateData(List<String> data) {
        Map<String,Object> map = new HashMap<>();
        map.put("photos",GsonUtil.listToJson(data));
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> observable = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).modifyStoreInfo(body);
        observable.enqueue(new SimpleCallBack<ResponseBody>(AddStorePicActivity.this) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                MyToast.show(AddStorePicActivity.this,"提交成功");
                finish();
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(AddStorePicActivity.this, object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e){
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
     * 添加图片
     * */
    @Override
    public void addPic(int position) {
        PicDialog picDialog = new PicDialog(this,this);
    }

    /**
     * 打开相册
     * */
    @Override
    public void openPic() {
        if (Build.VERSION.SDK_INT >= 23) {
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED){
                // 弹窗询问 ，让用户自己判断
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }
        Matisse.from(AddStorePicActivity.this)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)//参数1 显示资源类型 参数2 是否可以同时选择不同的资源类型 true表示不可以 false表示可以
//            .theme(R.style.Matisse_Dracula) //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .countable(true) //是否显示数字
                .capture(false)  //是否可以拍照
//                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                .maxSelectable(6-list.size())  //最大选择资源数量
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K)) //添加自定义过滤器
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.size_120dp)) //设置列宽
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //设置屏幕方向
                .thumbnailScale(0.75f)  //图片缩放比例
                .imageEngine(new GlideEngine())  //选择图片加载引擎
                .forResult(PIC_CODE);
    }
    /**
     * 拍照
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PIC_CODE://相册
                if (data!=null){
                    mPictureList = Matisse.obtainResult(data);
                    if (mPictureList.size()!=0){
                        for (Uri uri:mPictureList){
                            list.add(FileUtil.getRealFilePath(AddStorePicActivity.this,uri));
                        }
                        adapter.setData(list);
                    }
                }
                break;
            case CAMERA_CODE://相机
                list.add(pictureFile.getAbsolutePath());
                adapter.setData(list);
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}
