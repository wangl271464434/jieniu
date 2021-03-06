package com.jieniuwuliu.jieniu.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.MainActivity;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.FileUtil;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.HttpUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.util.SimpleCallBack;
import com.jieniuwuliu.jieniu.util.UpLoadFileUtil;
import com.jieniuwuliu.jieniu.api.HttpApi;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.DianZan;
import com.jieniuwuliu.jieniu.bean.LunTanResult;
import com.jieniuwuliu.jieniu.bean.PingLun;
import com.jieniuwuliu.jieniu.bean.UnReadMsg;
import com.jieniuwuliu.jieniu.bean.UserBean;
import com.jieniuwuliu.jieniu.luntan.AppearTextActivity;
import com.jieniuwuliu.jieniu.luntan.MsgListActivity;
import com.jieniuwuliu.jieniu.luntan.VideoActivity;
import com.jieniuwuliu.jieniu.luntan.adapter.LuntanAdater;
import com.jieniuwuliu.jieniu.messageEvent.LuntanEvent;
import com.jieniuwuliu.jieniu.view.MyLoading;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class LunTanFragment extends BaseFragment implements  LuntanAdater.CallBack, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.img_camera)
    ImageView imgCamera;
    @BindView(R.id.layout_title)
    RelativeLayout layoutTitle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img_msg)
    ImageView imgMsg;
    @BindView(R.id.layout_msg)
    RelativeLayout layoutMsg;
    private LuntanAdater adapter;
    private Intent intent;
    private final int PIC_CODE = 1001;//请求系统相册的请求码
    private final int CAMERA_CODE = 1002;//请求相机的请求码
    private final int VIDEO_CODE = 1003;//请求系统相册的请求码
    private final int PIC_VIDEO_CODE = 1004;//请求系统相册的请求码
    private Uri fileUri;
    private String token;
    private UserBean.DataBean user;
    private int page = 1,pageNum = 10;
    private int msgPage = 1,msgNum = 10;
    private List<Uri> mPictureList = new ArrayList<>();
    private Fragment fragment;
    private List<LunTanResult.DataBean> list;
    private MyLoading loading;
    private  File pictureFile;
    private  File videoFile;
    private int height = 400;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private int overallXScroll = 0;
    private Badge badge;
    private boolean isFresh = false;
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.luntan;
    }
    @OnClick({R.id.layout_camera,R.id.layout_msg})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_camera:
                showPopupWindow(imgCamera);
                break;
            case R.id.layout_msg:
                intent = new Intent();
                intent.setClass(getActivity(),MsgListActivity.class);
                startActivity(intent);
                break;
        }
    }
    @OnLongClick(R.id.layout_camera)
    public boolean onLongClick(){
        Intent intent = new Intent();
        intent.setClass(getActivity(),AppearTextActivity.class);
        intent.putExtra("type","text");
        intent.putExtra("user",user);
        startActivity(intent);
        return true;
    }
    @Override
    protected void init() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        fragment = this;
        badge = new QBadgeView(getActivity()).bindTarget(layoutMsg);
        list = new ArrayList<>();
        token = (String) SPUtil.get(getActivity(),Constant.TOKEN,Constant.TOKEN,"");
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(manager);
        adapter = new LuntanAdater(getActivity(),list);
        rv.setAdapter(adapter);
        ((SimpleItemAnimator)rv.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setCallBack(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
                if (overallXScroll <= 0) {   //设置标题的背景颜色
                    layoutTitle.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    title.setVisibility(View.GONE);
                } else if (overallXScroll > 0 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) overallXScroll / height;
                    float alpha = (255 * scale);
                    layoutTitle.setBackgroundColor(Color.argb((int) alpha, 41, 193, 246));
                    title.setVisibility(View.GONE);
                } else {
                    title.setVisibility(View.VISIBLE);
                    layoutTitle.setBackgroundColor(Color.argb((int) 255, 41, 193, 246));
                }
            }
        });
        getUserInfo();
        getLunTanList();
    }

    @Override
    public void onResume() {
        super.onResume();
        layoutTitle.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        title.setVisibility(View.GONE);
        getUnReadMsgList();
    }

    @Override
    public void onStop() {
        super.onStop();
        overallXScroll = 0;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final LuntanEvent event) {
        Log.i("luntan",event.toString());
        if (event.isSuccess()){
//            isFresh = true;
            page = 1;
            list.clear();
            adapter.notifyDataSetChanged();
            getLunTanList();
//            getLunTanList();
        }
    }
    /**
     * 未读消息列表
     * */
    private void getUnReadMsgList() {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getUnReadList(msgPage,msgNum);
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    UnReadMsg unReadMsg = (UnReadMsg) GsonUtil.praseJsonToModel(response.body().string(),UnReadMsg.class);
                    if (unReadMsg.getData()!=null){
                        if (unReadMsg.getData().size()>0){
                            badge.setBadgeNumber(-1);
                        }else {
                            badge.hide(true);
                            MainActivity.badge.hide(true);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try {
                    String error = response.errorBody().string();
                    JSONObject object = new JSONObject(error);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNetError(String s) {

            }
        });
    }
    /**
     * 获取论坛列表
     * */
    private void getLunTanList() {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).getForumsList(page,pageNum);
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    if (refreshLayout!=null){
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                    String json = response.body().string();
                    LunTanResult result = (LunTanResult) GsonUtil.praseJsonToModel(json,LunTanResult.class);
                    if (result.getData().size()!=0){
                        if (result.getData().size()<10){
                            refreshLayout.setNoMoreData(true);
                        }else{
                            refreshLayout.setNoMoreData(false);
                        }
                        list.addAll(result.getData());
                        adapter.notifyDataSetChanged();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try{
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                MyToast.show(getActivity(),s);
            }
        });
    }

    /**
     * 获取用户信息
     * */
    private void getUserInfo() {
        Call<UserBean> call = HttpUtil.getInstance().createRetrofit(token).create(HttpApi.class).getUserInfo();
        call.enqueue(new SimpleCallBack<UserBean>(getActivity()) {
            @Override
            public void onSuccess(Response<UserBean> response) {
                try{
                    if (response.body().getStatus() == 0){
                        user = response.body().getData();
                        adapter.setUser(user);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<UserBean> response) {
                try {
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNetError(String s) {
            }
        });
    }

    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.luntan_dialog, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
               280, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bg_white_shape));

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
        // 设置按钮的点击事件
        TextView tvCamera = contentView.findViewById(R.id.tv_camera);
        TextView tvVideo = contentView.findViewById(R.id.tv_video);
        TextView tvPic = contentView.findViewById(R.id.tv_pic);
        TextView tvPicVideo = contentView.findViewById(R.id.tv_pic_video);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
                        return;
                    }
                    int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                        return;
                    }
                    int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (read != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                        return;
                    }
                }
                Uri uri = null;
                pictureFile =FileUtil.createImageFile(getActivity());
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
                    uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                }else {
                    uri = Uri.fromFile(pictureFile);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        });
        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
                        return;
                    }
                    int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                        return;
                    }
                    int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (read != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                        return;
                    }
                }
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                try {
                    videoFile =FileUtil.createMediaFile(getActivity());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        ContentValues contentValues = new ContentValues(1);
                        contentValues.put(MediaStore.Images.Media.DATA, videoFile.getAbsolutePath());
                        fileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    }else {
                        fileUri = Uri.fromFile(videoFile);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
                    // start the Video Capture Intent
                    startActivityForResult(intent, VIDEO_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tvPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (Build.VERSION.SDK_INT >= 23) {
                    int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (read != PackageManager.PERMISSION_GRANTED){
                        // 弹窗询问 ，让用户自己判断
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                        return;
                    }
                }
                Matisse.from(fragment)
                        .choose(MimeType.ofImage())
                        .showSingleMediaType(true)//参数1 显示资源类型 参数2 是否可以同时选择不同的资源类型 true表示不可以 false表示可以
//            .theme(R.style.Matisse_Dracula) //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                        .countable(true) //是否显示数字
                        .capture(false)  //是否可以拍照
//                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                        .maxSelectable(9)  //最大选择资源数量
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K)) //添加自定义过滤器
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.size_120dp)) //设置列宽
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //设置屏幕方向
                        .thumbnailScale(0.75f)  //图片缩放比例
                        .imageEngine(new GlideEngine())  //选择图片加载引擎
                        .forResult(PIC_CODE);
            }
        });
        tvPicVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (Build.VERSION.SDK_INT >= 23) {
                    int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (read != PackageManager.PERMISSION_GRANTED){
                        // 弹窗询问 ，让用户自己判断
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                        return;
                    }
                }
                Matisse.from(fragment)
                        .choose(MimeType.ofVideo())
                        .showSingleMediaType(true)//参数1 显示资源类型 参数2 是否可以同时选择不同的资源类型 true表示不可以 false表示可以
//            .theme(R.style.Matisse_Dracula) //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                        .countable(true) //是否显示数字
                        .capture(false)  //是否可以拍照
//                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                        .maxSelectable(1)  //最大选择资源数量
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K)) //添加自定义过滤器
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.size_120dp)) //设置列宽
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //设置屏幕方向
                        .thumbnailScale(0.75f)  //图片缩放比例
                        .imageEngine(new GlideEngine())  //选择图片加载引擎
                        .forResult(PIC_VIDEO_CODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case PIC_CODE://相册
                    mPictureList = Matisse.obtainResult(data);
                    List<String> list = new ArrayList<>();
                    for (Uri uri:mPictureList){
                        Log.i("path",FileUtil.getRealFilePath(getActivity(),uri));
                        list.add(FileUtil.getRealFilePath(getActivity(),uri));
                    }
                    intent = new Intent();
                    intent.setClass(getActivity(),AppearTextActivity.class);
                    intent.putExtra("type","img");
                    intent.putExtra("list", (Serializable) list);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    break;
                case CAMERA_CODE://相机
                    if (resultCode == RESULT_OK){
                        intent = new Intent();
                        intent.setClass(getActivity(),AppearTextActivity.class);
                        intent.putExtra("type","img");
                        intent.putExtra("path",pictureFile.getPath());
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }
                    break;
                case VIDEO_CODE://录像
                    if (resultCode == RESULT_OK){
                        intent = new Intent();
                        intent.setClass(getActivity(),AppearTextActivity.class);
                        intent.putExtra("type","video");
                        intent.putExtra("path",videoFile.getAbsolutePath());
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }
                    break;
                case PIC_VIDEO_CODE://录像
                    mPictureList = Matisse.obtainResult(data);
                    String path = FileUtil.getRealFilePath(getActivity(),mPictureList.get(0));
                    intent = new Intent();
                    intent.setClass(getActivity(),AppearTextActivity.class);
                    intent.putExtra("type","video");
                    intent.putExtra("path",path);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
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
    /**
     * 点赞
     * */
    @Override
    public void dianZan(final int position) {
        Map<String,Object> map = new HashMap();
        map.put("fid",list.get(position).getId());
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).addDianZan(body);
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    String json = response.body().string();
                    Log.i("dianzan",json);
                    DianZan dianZan = new DianZan();
                    dianZan.setName(user.getNickname());
                    list.get(position).getDianzan().add(dianZan);
                    adapter.notifyItemChanged(position+1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {

            }
        });
    }
    /**
     * 取消点赞
     * */
    @Override
    public void cancelDianZan(final int position) {
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).deleteDianZan(list.get(position).getId());
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    for (DianZan dianZan:list.get(position).getDianzan()){
                        if (dianZan.getName().equals(user.getNickname())){
                            list.get(position).getDianzan().remove(dianZan);
                        }
                    }
                    adapter.notifyItemChanged(position+1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNetError(String s) {

            }
        });
    }

    /**
     * 评论
     * */
    @Override
    public void PingLun(int position) {
        showPingLunDialog(position,0,1);
    }
    /**
     * 评论对话框
     * */
    private void showPingLunDialog(final int position, final int userPostion, final int type) {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        Window window = dialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        window.setBackgroundDrawableResource(R.drawable.bg_white_shape);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (defaultDisplay.getWidth()*0.8);
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.setContentView(R.layout.dialog_send_msg);
        dialog.setCanceledOnTouchOutside(true);
        //给AlertDialog设置4个圆角
        final EditText etContext = dialog.findViewById(R.id.et_context);
        TextView tvSendMsg = dialog.findViewById(R.id.tv_send);
        if (type==2){
            etContext.setHint("@"+list.get(position).getPinglun().get(userPostion).getName());
        }
        tvSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = etContext.getText().toString();
                if (info.isEmpty()){
                    MyToast.show(getActivity(),"请输入要发送的内容");
                    return;
                }
                if (type==1){
                    sendMsg(position,userPostion,type,info,"");
                }else {
                    sendMsg(position,userPostion,type,info, String.valueOf(list.get(position).getPinglun().get(userPostion).getUid()));
                }
                dialog.dismiss();
            }
        });

    }
    /**
     * 发送评论
     * */
    private void sendMsg(final int position, final int userPosition, final int type, final String info, final String ruid) {
        Map<String,Object> map = new HashMap();
        map.put("fid",list.get(position).getId());
        map.put("info",info);
        if (type == 2){
            map.put("ruid",Integer.valueOf(ruid));
        }
        String json = GsonUtil.mapToJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).addPingLun(body);
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    PingLun  pingLun = new PingLun();
                    pingLun.setInfo(info);
                    if (type == 2){
                        pingLun.setRuid(Integer.valueOf(ruid));
                        pingLun.setRname(list.get(position).getPinglun().get(userPosition).getName());
                    }else {
                        pingLun.setRname("");
                    }
                    pingLun.setName(user.getNickname());
                    list.get(position).getPinglun().add(pingLun);
                    adapter.notifyItemChanged(position+1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {

            }
        });
    }

    /**
     * 跳转到播放视频页
     * */
    @Override
    public void playVideo(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(),VideoActivity.class);
        intent.putExtra("video", list.get(position).getVideo());
        getActivity().startActivity(intent);
    }

    /**
     * 回复某个人的消息
     * */
    @Override
    public void msgToUser(int msgPosition, int userPosition) {
           showPingLunDialog(msgPosition,userPosition,2);
    }

    /**
     * 删除某条论坛
     * */
    @Override
    public void deleteLunTan(final int position) {
        UpLoadFileUtil.getIntance(getActivity()).deleteList(list.get(position));
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).deleteForums(list.get(position).getId());
        call.enqueue(new SimpleCallBack<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try{
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    MyToast.show(getActivity(), "删除成功");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                try{
                    String s = response.errorBody().string();
                    JSONObject object = new JSONObject(s);
                    MyToast.show(getActivity(), object.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                MyToast.show(getActivity(), s);
            }
        });
    }

    /**
     * 下拉刷新
     * */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        layoutTitle.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        title.setVisibility(View.GONE);
        page = 1;
        list.clear();
        getLunTanList();
    }
    /**
     * 上拉加载
     * */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getLunTanList();
    }
}
