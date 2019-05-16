package com.jieniuwuliu.jieniu.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;

public class PicDialog implements View.OnClickListener {
    private Context context;
    private AlertDialog dialog;
    private CallBack callBack;
    public PicDialog(Activity context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        show();
    }

    private void show() {
        dialog = new AlertDialog.Builder(context).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setContentView(R.layout.pic_dialog);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvPic = dialog.findViewById(R.id.tv_picture);
        TextView tvCamera = dialog.findViewById(R.id.tv_camera);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvPic.setOnClickListener(this);
        tvCamera.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_picture:
                callBack.openPic();
                dialog.dismiss();
                break;
            case R.id.tv_camera:
                callBack.openCamera();
                dialog.dismiss();
                break;
            case R.id.tv_cancel:
                dialog.dismiss();
                break;
        }
    }
    public interface CallBack{
        void  openPic();//打开相册
        void  openCamera();//打开相机
    }
}
