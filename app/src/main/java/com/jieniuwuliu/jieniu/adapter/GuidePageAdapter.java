package com.jieniuwuliu.jieniu.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Desc:引导页适配器
 */
public class GuidePageAdapter extends PagerAdapter {
    //界面列表
    private List<ImageView> views;

    public GuidePageAdapter (List<ImageView> views){
        this.views = views;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    //获得当前界面数
    @Override
    public int getCount() {
        if (views != null)
        {
            return views.size();
        }

        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    //判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }
}
