package com.jieniuwuliu.jieniu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
   /* public static String getDateStr(String string){
        string = string.replace("Z", " UTC");//注意是空格+UTC
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
            Date date = formatter.parse(string);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = sdf.format(date);
            return s;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return "";
    }*/
    //获取当前毫秒值
    public static long getMillisecond(){
        long milisecond = System.currentTimeMillis();
        return milisecond;
    }
    //获取当前毫秒值
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  sdf.format(new Date());
    }
    //将时间字符串转换成毫秒值
    public static long getMiliSecond(String string){
        long second = 0;
/*        String tzId = TimeZone.getDefault().getID();
        TimeZone tz = TimeZone.getTimeZone(tzId);*/
//        string = string.replace("Z", " UTC");//注意是空格+UTC
        try {
   /*         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
            Date date = formatter.parse(string);
            Log.i("date",date.toString());*/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(string);
            second = date.getTime();
            return second;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return second;
    }
    //根据发布的时间不同，显示不同的标签
    public static String getShowString(long a){
        String s = "";
        long current = getMillisecond();
        long b = current - a;
        int m = (int) (b/(60*1000));
        int h = m/60;
        int d = h/24;
        if (m>0){
            if (h>0){
                if (d>0){
                    if (d<7){
                        s = d + "天前";
                    }else{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        s = sdf.format(new Date(a));
                    }
                }else{
                    s = h + "小时前";
                }
            }else{
                s = m+"分钟前";
            }
        }else {
            s = "刚刚";
        }
        return s;
    }
    /**
     * 将秒数转换成 小时  和  分钟
     * @return
     */
    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            DateTimes= days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
        }else if(hours>0){
            DateTimes=hours + "小时" + minutes + "分钟" + seconds + "秒";
        }else if(minutes>0){
            DateTimes=minutes + "分钟" + seconds + "秒";
        }else{
            DateTimes=seconds + "秒";
        }
        return DateTimes;
    }
    /**
     * 判断优惠券是否过期
     * */
    public static boolean isExprie(String time){
        boolean b = false;
        long currentTime = getMillisecond();
        if (currentTime-getMiliSecond(time)>0){
            b = true;
        }else{
            b = false;
        }
        return b;
    }
}
