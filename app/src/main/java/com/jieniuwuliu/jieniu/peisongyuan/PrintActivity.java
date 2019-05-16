package com.jieniuwuliu.jieniu.peisongyuan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.FastClickUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.PSOrderInfo;
import com.jieniuwuliu.jieniu.view.MyLoading;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import HPRTAndroidSDKA300.HPRTPrinterHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 打印界面
 */
public class PrintActivity extends BaseActivity {

    @BindView(R.id.btn_print)
    Button btnPrint;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    private HPRTPrinterHelper printer;
    private PSOrderInfo.DataBean data;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isConnect = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_print;
    }

    @Override
    protected void init() {
        title.setText("打印");
        data = (PSOrderInfo.DataBean) getIntent().getSerializableExtra("data");
    }
    /**
     * 链接蓝牙
     * */
    private void connectBlue() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                return;
            }
        }
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices){
                Log.i("blue","蓝牙名称："+device.getName()+",mac:"+device.getAddress());
                if (device.getName().contains("HM-A300")){
                    printer = new HPRTPrinterHelper(this, HPRTPrinterHelper.PRINT_NAME_A300);
                    HPRTPrinterHelper.PortOpen("Bluetooth,"+device.getAddress());
                    isConnect = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvInfo.setText("蓝牙连接成功，可以进行打印");
                        }
                    });
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back,R.id.btn_connect, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_connect:
                if (FastClickUtil.isFastClick()){
                    MyToast.show(getApplicationContext(),"请不要重复点击");
                    return;
                }
                tvInfo.setText("正在连接蓝牙……");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        connectBlue();
                    }
                }.start();

                break;
            case R.id.btn_print://打印
                if (isConnect){
                    setPrint();
                }else{
                    MyToast.show(getApplicationContext(),"请先连接打印机");
                }
                break;
        }
    }

    /**
     * 设置打印内容
     **/
    private void setPrint() {
        try {
            Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.logo)).getBitmap();
            Bitmap newBitmap = resizeBitmap(60,60,bitmap);
            HPRTPrinterHelper.printAreaSize("0","200","200","1400","1");
            HPRTPrinterHelper.Encoding("UTF-8");
            HPRTPrinterHelper.InverseLine("0","0","550","0","1");
            HPRTPrinterHelper.InverseLine("0","0","0","1300","1");
            HPRTPrinterHelper.InverseLine("550","0","550","1300","1");
            HPRTPrinterHelper.Expanded("5","5",newBitmap,0);
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","70","10","捷牛速运");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","400","10","标准件");
            HPRTPrinterHelper.InverseLine("0","70","550","70","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","20","90",data.getOrderNumber());
            HPRTPrinterHelper.InverseLine("0","140","550","140","1");
//            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","20","230","收");
            HPRTPrinterHelper.AutLine("10","230",100,4,true,false,"收");
            HPRTPrinterHelper.InverseLine("80","140","80","350","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","90","150",data.getToName());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","90","200",data.getToPhone());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","90","250",data.getToAddress());
            HPRTPrinterHelper.InverseLine("0","350","550","350","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","40","380","寄");
            HPRTPrinterHelper.InverseLine("80","350","80","450","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","90","360",data.getFromName());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","90","390",data.getFromPhone());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","90","420",data.getFromAddress());
            HPRTPrinterHelper.InverseLine("0","450","550","450","1");
            HPRTPrinterHelper.PrintQR(HPRTPrinterHelper.BARCODE,"20","460","2","6",data.getOrderNumber());//订单二维码
            HPRTPrinterHelper.InverseLine("160","450","160","600","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","170","460","物品信息："+data.getInfo());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","170","500","代收货款："+data.getDaishouMoney());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","170","540","保价费："+data.getBaojiaMoney());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","170","570","运费："+data.getYunfeiMoney());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","300","570","合计："+data.getTotalMloney());
            HPRTPrinterHelper.InverseLine("0","600","550","600","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","10","610","签收：");
            HPRTPrinterHelper.InverseLine("0","660","550","660","1");
            HPRTPrinterHelper.InverseLine("190","600","190","660","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","200","610","客服电话：400-029-2282");
            //附联
            HPRTPrinterHelper.Expanded("5","705",newBitmap,0);
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","70","740","捷牛速运");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","400","740","标准件");
            HPRTPrinterHelper.InverseLine("0","780","550","780","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","20","800",data.getOrderNumber());
            HPRTPrinterHelper.InverseLine("0","830","550","830","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","20","850","收");
            HPRTPrinterHelper.InverseLine("80","830","80","900","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","90","840",data.getToPhone());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","90","870",data.getToAddress());
            HPRTPrinterHelper.InverseLine("0","900","550","900","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"4","0","20","920","寄");
            HPRTPrinterHelper.InverseLine("80","900","80","980","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","90","910",data.getFromPhone());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","90","940",data.getFromAddress());
            HPRTPrinterHelper.InverseLine("0","980","550","980","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","10","1040","代收货款："+data.getDaishouMoney());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","10","1070","保价费："+data.getBaojiaMoney());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","10","1100","运费："+data.getYunfeiMoney());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","10","1130","合计："+data.getTotalMloney());
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","320","1010","扫码关注公众号");
            HPRTPrinterHelper.PrintQR(HPRTPrinterHelper.BARCODE,"320","1040","2","6","http://weixin.qq.com/r/5i-Tyz-EcGIPrXuo93r0");//公众号二维码
            HPRTPrinterHelper.InverseLine("0","1260","550","1260","1");
            HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT,"7","0","10","1270","客服电话：400-029-2282");
            HPRTPrinterHelper.InverseLine("0","1300","550","1300","1");
            HPRTPrinterHelper.Form();
            HPRTPrinterHelper.Print();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // 如果请求被取消，则结果数组为空。
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connectBlue();
                } else {
                    Log.i("tag", "拒绝申请");
                }
                return;
            }
        }
    }
    /**
     * 将bitmap转成固定大小
     * */
    public Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / bitmap.getWidth(),
                newHeight / bitmap.getHeight());
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return newBitmap;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (printer != null) {
                HPRTPrinterHelper.PortClose();
            }
        } catch (Exception e) {
            Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> onClickClose ")).append(e.getMessage()).toString());
        }
    }

}
