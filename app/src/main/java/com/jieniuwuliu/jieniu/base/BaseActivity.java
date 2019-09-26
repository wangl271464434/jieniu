package com.jieniuwuliu.jieniu.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    Intent intent;
    protected Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        init();
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
