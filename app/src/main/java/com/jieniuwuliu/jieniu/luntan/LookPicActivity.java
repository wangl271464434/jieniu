package com.jieniuwuliu.jieniu.luntan;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.adapter.GuidePageAdapter;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.luntan.adapter.PicAdapter;
import com.jieniuwuliu.jieniu.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看图片
 */
public class LookPicActivity extends BaseActivity {

    @BindView(R.id.banner)
    ViewPager banner;
    private ArrayList<String> list;
    private int index = 0;
    private List<ImageView> views = new ArrayList<>();
    private PicAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_look_pic;
    }

    @Override
    protected void init() {
        list = (ArrayList<String>) getIntent().getStringArrayListExtra("list");
        index = getIntent().getIntExtra("index",0);
        adapter = new PicAdapter(this,list);
        banner.setAdapter(adapter);
        banner.setCurrentItem(index);
    }
}
