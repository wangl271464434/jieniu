package com.jieniuwuliu.jieniu.luntan;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.KeyboardUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.LunTanBean;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.luntan.adapter.AppearTextAdapter;
import com.jieniuwuliu.jieniu.messageEvent.LuntanEvent;
import com.jieniuwuliu.jieniu.messageEvent.VideoEvent;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 发表论坛
 */
public class AppearTextActivity extends BaseActivity implements OnItemClickListener, PicDialog.CallBack, AddStorePicAdapter.CallBack, AppearTextAdapter.CallBack {

    @BindView(R.id.et_context)
    EditText etContext;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_appear)
    TextView tvAppear;
    private String path,type;
    private AppearTextAdapter adapter;
    private List<String> list;
    private final int PIC_CODE = 1001;//请求系统相册的请求码
    private final int CAMERA_CODE = 1002;//请求相机的请求码
    private List<Uri> mPictureList = new ArrayList<>();
    private String info;
    private List<String> imgList;//要上传的图片数组
    private UserBean.DataBean user;
    private LunTanBean lunTanBean;
    private String token;
    private MyLoading loading;
    private String videoUrl,imgUrl;//上传得到视频和图片的链接
    private  File pictureFile;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_appear_text;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        loading = new MyLoading(this,R.style.CustomDialog);
        imgList = new ArrayList<>();
        list = new ArrayList<>();
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        user = (UserBean.DataBean) getIntent().getSerializableExtra("user");
        type = getIntent().getStringExtra("type");
        path = getIntent().getStringExtra("path");
        if (path!=null){
            list.add(path);
        }else{
            list = (List<String>) getIntent().getSerializableExtra("list");
        }
        if (type.equals("text")){
            rv.setVisibility(View.GONE);
        }else{
            GridLayoutManager manager = new GridLayoutManager(this, 3);
            rv.setLayoutManager(manager);
            adapter = new AppearTextAdapter(this,list,type);
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            adapter.setCallBack(this);
            ((SimpleItemAnimator)rv.getItemAnimator()).setSupportsChangeAnimations(false);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(VideoEvent event){
        if (!event.getImgUrl().equals("")){
            imgUrl = event.getImgUrl();
        }
        if (!event.getVideoUrl().equals("")){
            videoUrl =event.getVideoUrl();
        }
        if (!videoUrl.equals("")||!imgUrl.equals("")){
            updateData();
        }
    }
    @OnClick({R.id.back, R.id.tv_appear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_appear:
                KeyboardUtil.hideSoftKeyboard(this);
                info = etContext.getText().toString();
                if (type.equals("text")){
                    if (info.isEmpty()){
                        MyToast.show(getApplicationContext(),"发表内容不能为空");
                        return;
                    }
                }else{
                    if (info.isEmpty()&& list.size()==0){
                        MyToast.show(getApplicationContext(),"发表内容不能为空");
                        return;
                    }
                }
                tvAppear.setEnabled(false);
                loading.show();
                lunTanBean = new LunTanBean();
                lunTanBean.setUid(user.getId());
                lunTanBean.setInfo(info);
                update();
                break;
        }
    }
    /**
     * 发表论坛
     * */
    private void update() {
        switch (type){
            case "img":
                updateImg();
                break;
            case "video":
                lunTanBean.setType(3);
                updateVideo();
                break;
            case "text":
                lunTanBean.setType(1);
                updateData();
                break;
        }

    }
    /**
     * 上传视频
     * */
    private void updateVideo() {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        Log.i("video","要上传的视频路径："+list.get(0));
        media.setDataSource(list.get(0));// videoPath 本地视频的路径
        Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(list.get(0), MediaStore.Images.Thumbnails.MICRO_KIND);
        // 图片Bitmap转file
        File file = FileUtil.createImageFile(this,bitmap);
        COSXMLUploadTask imgTask =  UpLoadFileUtil.getIntance(AppearTextActivity.this).upload("img",file.getName(),file.getPath());
        imgTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, final CosXmlResult result) {
                Log.w("返回结果","Success: " +result.accessUrl);
                lunTanBean.setVideoImage(result.accessUrl);
                VideoEvent event = new VideoEvent();
                event.setImgUrl(result.accessUrl);
                EventBus.getDefault().post(event);
            }
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
        COSXMLUploadTask videoTask =  UpLoadFileUtil.getIntance(AppearTextActivity.this).upload("video",new File(list.get(0)).getName(),list.get(0));
        videoTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, final CosXmlResult result) {
                Log.w("返回结果","Success: " +result.accessUrl);
                lunTanBean.setVideo(result.accessUrl);
                VideoEvent event = new VideoEvent();
                event.setVideoUrl(result.accessUrl);
                EventBus.getDefault().post(event);
            }
            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
    }
    /**
     * 提交数据
     * */
    private void updateData() {
        if (lunTanBean ==null){
            return;
        }
        String json = GsonUtil.objectToJson(lunTanBean);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).addForums(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    loading.dismiss();
                    switch (response.code()){
                        case 200:
                            MyToast.show(getApplicationContext(), "发表成功");
                            LuntanEvent event = new LuntanEvent();
                            event.setSuccess(true);
                            EventBus.getDefault().post(event);
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
                Log.e("error","fail is reason:"+t.toString());
                loading.dismiss();
                MyToast.show(getApplicationContext(),"网络请求失败");
            }
        });
    }

    /**
     * 上传图片
     * */
    private void updateImg() {
        for (String s:list){
            COSXMLUploadTask storeTask =  UpLoadFileUtil.getIntance(AppearTextActivity.this).upload("img",new File(s).getName(),s);
            storeTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, final CosXmlResult result) {
                    Log.w("返回结果","Success: " +result.accessUrl);
                    imgList.add(result.accessUrl);
                    if (imgList.size()==list.size()){
//                                updateData(data);
                        if (imgList.size()==1){
                            lunTanBean.setType(1);
                        }else{
                            lunTanBean.setType(2);
                        }
                        lunTanBean.setPhotos(GsonUtil.listToJson(imgList));
                        updateData();
                    }
                }
                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    Log.w("返回结果", "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
//        MyToast.show(getApplicationContext(),"点击查看");
    }
    /**
     * 相册
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
        Matisse.from(AppearTextActivity.this)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)//参数1 显示资源类型 参数2 是否可以同时选择不同的资源类型 true表示不可以 false表示可以
//            .theme(R.style.Matisse_Dracula) //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .countable(true) //是否显示数字
                .capture(false)  //是否可以拍照
//                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                .maxSelectable(9-list.size())  //最大选择资源数量
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
            }
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
            }
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
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
    /**
     * 添加图片
     * */
    @Override
    public void addPic(int position) {
        new PicDialog(this,this);
    }
    /**
     * 删除图片
     * */
    @Override
    public void deletePic(int position) {
        list.remove(list.get(position));
        adapter.setData(list);
        adapter.notifyItemRemoved(position);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PIC_CODE://相册
                if (data!=null){
                    mPictureList = Matisse.obtainResult(data);
                    if (mPictureList.size()!=0){
                        for (Uri uri:mPictureList){
                            list.add(FileUtil.getRealFilePath(AppearTextActivity.this,uri));
                        }
                        adapter.setData(list);
                    }
                }
                break;
            case CAMERA_CODE://相机
                list.add(pictureFile.getPath());
                adapter.setData(list);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lunTanBean = null;
    }
}
