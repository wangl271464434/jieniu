package com.jieniuwuliu.jieniu.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.PSOrderInfo;

import HPRTAndroidSDKA300.HPRTPrinterHelper;

public class PrintUtil {
    public static void setPrint(Context context,PSOrderInfo.DataBean data){
        try {
            Bitmap bitmap = ((BitmapDrawable)context.getResources().getDrawable(R.mipmap.logo)).getBitmap();
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
//            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 将bitmap转成固定大小
     * */
    public static Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / bitmap.getWidth(),
                newHeight / bitmap.getHeight());
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return newBitmap;
    }
}
