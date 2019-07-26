package com.jieniuwuliu.jieniu.Util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneUtil {
    private static String ISDOUBLE;
    private static String SIMCARD;
    private static String SIMCARD_1;
    private static String SIMCARD_2;
    private static boolean isDouble = false;

    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        String phoneNumber1 = tm.getLine1Number();
        initIsDoubleTelephone(context);
        if (isDouble){
            return phoneNumber1;
        }else{
            return phoneNumber1;
        }
    }
    private static void initIsDoubleTelephone(Context context) {
        isDouble = true;
        Method method = null;
        Object result_0 = null;
        Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 只要在反射getSimStateGemini 这个函数时报了错就是单卡手机（这是我自己的经验，不一定全正确）
            method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[]{ int.class });
            // 获取SIM卡1
            result_0 = method.invoke(tm, new Object[]{ new Integer(0) });
            // 获取SIM卡2
            result_1 = method.invoke(tm, new Object[]{ new Integer(1) });
        } catch (SecurityException e) {
            isDouble = false;
            e.printStackTrace();
            // System.out.println("1_ISSINGLETELEPHONE:"+e.toString());
        } catch (NoSuchMethodException e) {
            isDouble = false;
            e.printStackTrace();
            // System.out.println("2_ISSINGLETELEPHONE:"+e.toString());
        } catch (IllegalArgumentException e)
        {
            isDouble = false;
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            isDouble = false;
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            isDouble = false;
            e.printStackTrace();
        } catch (Exception e) {
            isDouble = false;
            e.printStackTrace();
            // System.out.println("3_ISSINGLETELEPHONE:"+e.toString());
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        if (isDouble) {
            // 保存为双卡手机
            editor.putBoolean(ISDOUBLE, true);
            // 保存双卡是否可用
            // 如下判断哪个卡可用.双卡都可以用
            if (result_0.toString().equals("5") && result_1.toString().equals("5")) {
                if (!sp.getString(SIMCARD, "2").equals("0")
                        && !sp.getString(SIMCARD, "2").equals("1")) {
                    editor.putString(SIMCARD, "0");
                }
                editor.putBoolean(SIMCARD_1, true);
                editor.putBoolean(SIMCARD_2, true);
            } else if (!result_0.toString().equals("5")
                    && result_1.toString().equals("5")) {// 卡二可用
                if (!sp.getString(SIMCARD, "2").equals("0")
                        && !sp.getString(SIMCARD, "2").equals("1")) {
                    editor.putString(SIMCARD, "1");
                }
                editor.putBoolean(SIMCARD_1, false);
                editor.putBoolean(SIMCARD_2, true);
            } else if (result_0.toString().equals("5")
                    && !result_1.toString().equals("5")) {// 卡一可用
                if (!sp.getString(SIMCARD, "2").equals("0")
                        && !sp.getString(SIMCARD, "2").equals("1")) {
                    editor.putString(SIMCARD, "0");
                }
                editor.putBoolean(SIMCARD_1, true);
                editor.putBoolean(SIMCARD_2, false);
            } else {// 两个卡都不可用(飞行模式会出现这种种情况)
                editor.putBoolean(SIMCARD_1, false);
                editor.putBoolean(SIMCARD_2, false);
            }
        } else {
            // 保存为单卡手机
            editor.putString(SIMCARD, "0");
            editor.putBoolean(ISDOUBLE, false);
        }
        editor.commit();
    }

}
