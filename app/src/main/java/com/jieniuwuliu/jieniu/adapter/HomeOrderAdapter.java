package com.jieniuwuliu.jieniu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.fragment.PagerFragment;
import com.jieniuwuliu.jieniu.util.JwtUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class HomeOrderAdapter extends FragmentPagerAdapter {
    private List<OrderInfo> list;
    public HomeOrderAdapter(FragmentManager fm,List<OrderInfo> list) {
        super(fm);
        this.list = list;
    }
    @Override
    public Fragment getItem(int position) {
        PagerFragment pagerFragment = new PagerFragment();
        pagerFragment.setItem(list.get(position));
        return pagerFragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public int getItemPosition(Object object) {
        //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
        // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
        return PagerAdapter.POSITION_NONE;
    }
}
