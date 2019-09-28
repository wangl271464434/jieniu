package com.jieniuwuliu.jieniu.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    Intent intent;
    protected Unbinder unbinder;
    private List<Activity> activityList;
    public static final String LOGIN = "receiver_action_finish_login";
    public static FinishActivityRecevier mRecevier;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        init();
        mRecevier = new FinishActivityRecevier();
    }
    private class FinishActivityRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //根据需求添加自己需要关闭页面的action
            if (LOGIN.equals(intent.getAction())) {
                BaseActivity.this.finish();
            }
        }
    }
    /**
     * 添加Activity到集合中
     */
    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new LinkedList<>();
        }
        activityList.add(activity);
    }
    /**
     * 关闭指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityList != null && activity != null && activityList.contains(activity)) {
            //使用迭代器安全删除
            for (Iterator<Activity> it = activityList.iterator(); it.hasNext(); ) {
                Activity temp = it.next();
                // 清理掉已经释放的activity
                if (temp == null) {
                    it.remove();
                    continue;
                }
                if (temp == activity) {
                    it.remove();
                }
            }
            activity.finish();
        }
    }
    /**
     * 关闭指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityList != null) {
            // 使用迭代器安全删除
            for (Iterator<Activity> it = activityList.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }
    }
    /**
     * 关闭所有的Activity
     */
    public void finishAllActivity() {
        if (activityList != null) {
            for (Iterator<Activity> it = activityList.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                if (activity != null) {
                    activity.finish();
                }
            }
            activityList.clear();
        }
    }
    //布局文件
    protected abstract int getLayoutId();
    //初始化数据
    protected abstract void init();
    //界面跳转
    protected void startAcy(Class<?> clazz){
        intent = new Intent();
        intent.setClass(this,clazz);
        startActivity(intent);
    }
}
