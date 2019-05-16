package com.jieniuwuliu.jieniu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.LocalFileUtil;
import com.jieniuwuliu.jieniu.Util.PinyinComparator;
import com.jieniuwuliu.jieniu.adapter.MoreCarSortAdapter;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.SortModel;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.messageEvent.CarTypeEvent;
import com.jieniuwuliu.jieniu.view.SideBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreCarActivity extends BaseActivity implements OnItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    LinearLayoutManager manager;
    private MoreCarSortAdapter adapter;
    private List<Object> objects;
    private List<SortModel> SourceDateList;
    private List<SortModel> list;//要传的数组
    private List<SortModel> chooseList;//要传的数组
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_car;
    }

    @Override
    protected void init() {
        pinyinComparator = new PinyinComparator();
        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        String json = LocalFileUtil.readTextFromSDcard(this);
        json = json.replace(" ", "");
        objects = GsonUtil.praseJsonToList(json, SortModel.class);
        SourceDateList = new ArrayList<>();
        for (Object object : objects) {
            SourceDateList.add((SortModel) object);
        }
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new MoreCarSortAdapter(this, SourceDateList);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        CarTypeEvent event = new CarTypeEvent();
        event.setName(SourceDateList.get(position).getName());
        EventBus.getDefault().post(event);
        finish();
    }


    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }
}
