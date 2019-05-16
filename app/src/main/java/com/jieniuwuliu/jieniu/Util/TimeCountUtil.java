package com.jieniuwuliu.jieniu.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
/**
 * Created by wanglei on 2016/7/14.
 * 倒计时
 */
public class TimeCountUtil{

    /**
     * 倒计时
     *
     * @param button 控件
     * @param waitTime 倒计时总时长
     * @param interval 倒计时的间隔时间
     * @param hint     倒计时完毕时显示的文字
     */
    public static void countDown(final Context context, final TextView button, long waitTime, long interval, final String hint) {
        button.setEnabled(false);
        android.os.CountDownTimer timer = new android.os.CountDownTimer(waitTime, interval) {

            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText(String.format(" %ds后重新获取", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                button.setEnabled(true);
                button.setText(hint);
            }
        };
        timer.start();
    }
}
