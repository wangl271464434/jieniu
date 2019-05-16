package com.jieniuwuliu.jieniu.Util;

import android.content.Context;
import android.content.pm.PackageManager;

public class APKVersionCodeUtils {
    /**
     * 获取当前本地apk的版本
     *
     * @param context
     *return
     */
    public static int getVersionCode(Context context){
        int code = 0;
        try {
            code = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
    /**
     * 获取当前本地apk的版本
     *
     * @param context
     *return
     */
    public static String getVersionName(Context context){
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
