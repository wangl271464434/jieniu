package com.jieniuwuliu.jieniu;

import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.adapter.GuidePageAdapter;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity {
    private int[] pics = {R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3};
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<ImageView> views = new ArrayList<>();
    private String token;
    private int userType;
    private boolean isGuide;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init() {
        token = (String) SPUtil.get(this,Constant.TOKEN,Constant.TOKEN,"");
        userType = (int) SPUtil.get(this,Constant.USERTYPE,Constant.USERTYPE,0);
        isGuide = (boolean) SPUtil.get(this,Constant.GUIDE,Constant.GUIDE,false);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int pic : pics) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pic);
            views.add(iv);
        }
        GuidePageAdapter mAdapter = new GuidePageAdapter(views);
        viewPager.setAdapter(mAdapter);
        views.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtil.put(GuideActivity.this,Constant.GUIDE,Constant.GUIDE,true);
                startAcy(LoginActivity.class);
                finish();
            }
        });
    }
}
