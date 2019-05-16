package com.jieniuwuliu.jieniu.luntan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FileUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.base.BaseActivity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 拍照或者拍摄视频
 */
public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback {
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.img_stop)
    ImageView imgStop;
    @BindView(R.id.layout_finish)
    RelativeLayout layoutFinish;
    @BindView(R.id.img_camera)
    ImageView imgCamera;
    @BindView(R.id.surface)
    SurfaceView surface;
    private Camera camera;
    private Camera.Parameters params;
    private SurfaceHolder holder;
    private int cameraType = 0;//0后摄 1前摄
    private String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean hasPermissionDismiss = false;
    private int width,height;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void init() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;         // 屏幕宽度
        height = dm.heightPixels;       // 屏幕高度
        holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        initPermission();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (camera != null) {
            camera.startPreview();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (camera != null) {
            camera.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    /**
     * 权限
     * */
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permissions,100);
                return;
            }
        }else{
            initCamera();
        }
    }
    /**
     * 初始化相机
     * */
    private void initCamera() {
       if (camera == null){
           try {
               camera = Camera.open(cameraType);
               camera.setPreviewDisplay(holder);
               camera.setDisplayOrientation(90);//预览旋转90度
               initParams();
               camera.startPreview();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
    /**
     * 设置参数
     * */
    private void initParams(){
        params = camera.getParameters();
//        params.setPictureSize(width, height);//设置照片分辨率
        params.setPictureSize(1024,768);
        params.set("jpeg-quality", 100);//设置照片质量
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);// 连续对焦模式
        params.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
        camera.setParameters(params);
    }
    public void switchFrontCamera() {
        if (cameraType == 1) {//1为前摄
            cameraType = 0;
            reStartCamera(cameraType);
        } else {
            cameraType = 1;
            reStartCamera(cameraType);
        }
    }

    //重新打开预览
    public void reStartCamera(int i) {
        if (camera != null) {
            camera.stopPreview();//停掉原来摄像头的预览
            camera.release();//释放资源
            camera = null;//取消原来摄像头
        }
        try {
            camera = Camera.open(i);//打开当前选中的摄像头
            camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
            camera.setDisplayOrientation(90);// 屏幕方向
            initParams();
            camera.startPreview();//开始预览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //release释放
    private void releaseCamera(){
        //如果相机不为空 释放相机   初始化
        if (camera!=null){
            camera.release();
            camera=null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
            if (hasPermissionDismiss){
                MyToast.show(getApplicationContext(),"请赋予权限后再试");
                finish();
            }else{
                initCamera();
            }
        }
    }
    @OnClick({R.id.img_close, R.id.img_cancel, R.id.img_stop, R.id.img_qiehuan, R.id.img_ok, R.id.img_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_qiehuan:
              switchFrontCamera();
                break;
            case R.id.img_cancel:
                layoutFinish.setVisibility(View.GONE);
//                imgPreview.setVisibility(View.GONE);
                break;
            case R.id.img_stop:
                break;
            case R.id.img_ok:
                layoutFinish.setVisibility(View.GONE);
                break;
            case R.id.img_camera://拍照
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success){
                            camera.takePicture(null,null,callback);
                        }else {
                            camera.takePicture(null,null,callback);
                        }
                    }
                });

                break;
        }
    }
    private Camera.PictureCallback callback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file = FileUtil.createImageFile(CameraActivity.this);
            try {
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (cameraType == 0){
                    bmp = rotateBitmapByDegree(bmp, 90);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
