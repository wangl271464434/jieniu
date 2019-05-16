package com.jieniuwuliu.jieniu.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/15 0015.
 * 自己写的Toast
 */

public class MyToast {
    /**
     *
     * @date: 2018/6/25
     * @author:wanglei
     * @params: context string
     *
     * */
    public static void show(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
    /**
     *
     * @date: 2018/6/25
     * @author:wanglei
     * @params: context,resId
     *
     * */
    public static void show(Context context,int resId){
        Toast.makeText(context, resId,Toast.LENGTH_SHORT).show();
    }
}
