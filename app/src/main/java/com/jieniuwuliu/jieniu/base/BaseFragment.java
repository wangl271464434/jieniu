package com.jieniuwuliu.jieniu.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    View view;
    protected Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getFragmentLayoutId(),null);
        unbinder = ButterKnife.bind(this,view);
        init();
        return view;
    }
    //布局文件
    protected abstract int getFragmentLayoutId();
    //初始化控件
    protected abstract void init();
}
