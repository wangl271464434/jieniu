package com.jieniuwuliu.jieniu.luntan;

import androidx.viewpager.widget.ViewPager;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.luntan.adapter.PicAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
        index = getIntent().getIntExtra("index", 0);
        adapter = new PicAdapter(this, list);
        banner.setAdapter(adapter);
        banner.setCurrentItem(index);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
