package com.jieniuwuliu.jieniu.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;

public class UpdateManager {
	private Activity mContext;
	//apk下载地址
	private String apkUrl = "https://jieniu-1254151230.cos.ap-beijing.myqcloud.com/app/jieniu.apk";
	 //apk路径
    private static final String saveFileName = "jieniu";
    //进度条
    private ProgressBar mProgress;
	private AlertDialog dialog;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private Thread downLoadThread;
	private TextView tvPrecent,tvNum;
	private boolean interceptFlag = false;
    private File ApkFile;
    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
    	@SuppressLint("SetTextI18n")
		public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				Log.e("msg", "进度---="+msg.what);
				tvPrecent.setText(progress+"%");
				tvNum.setText(progress+"/100");
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
    	};
    };
	public UpdateManager(Activity context) {
		this.mContext = context;
	}

	//外部接口让主Activity调用
	public void checkUpdateInfo(){
		showDownloadDialog();
	}

	private void showDownloadDialog(){
		dialog = new AlertDialog.Builder(mContext).create();
		Window window = dialog.getWindow();
		WindowManager m = mContext.getWindowManager();
		Display defaultDisplay = m.getDefaultDisplay();
		window.setBackgroundDrawableResource(R.drawable.bg_white_shape);
		window.setGravity(Gravity.CENTER);
		dialog.show();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = (int) (defaultDisplay.getWidth()*0.8);
		window.setAttributes(params);
		dialog.setContentView(R.layout.update_progress);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		mProgress = dialog.findViewById(R.id.progress);
		tvPrecent = dialog.findViewById(R.id.tv);
		tvNum = dialog.findViewById(R.id.tv_num);
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
				ApkFile = File.createTempFile(saveFileName,".apk",file);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){
		    			setPermission(ApkFile.getPath());
		    			//下载完成通知安装
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	/**
	 * 提升读写权限
	 * @param path 文件路径
	 * @return
	 * @throws IOException
	 */
	private void setPermission(String path) {
		String command = "chmod " + "777" + " " + path;
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * 下载apk
     */
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	/**
	 * 安装apk
	 */
	private void installApk(){
        if (!ApkFile.exists()) {
            return;
        }    
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(
					mContext
					, "com.jieniuwuliu.jieniu.fileprovider"
					, ApkFile);
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(Uri.fromFile(ApkFile), "application/vnd.android.package-archive");
		}
        mContext.startActivity(intent);
	}
}
