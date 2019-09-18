package com.jieniuwuliu.jieniu;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jieniuwuliu.jieniu.qipeishang.QPSListActivity;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.LocalFileUtil;
import com.jieniuwuliu.jieniu.util.PinyinComparator;
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
    private int type;
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private String json;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_car;
    }

    @Override
    protected void init() {
        type = getIntent().getIntExtra("type",0);
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
        SourceDateList = new ArrayList<>();
        if (type == 1){
            json = LocalFileUtil.readTextFromSDcard(this);
        }else{
            json = LocalFileUtil.readKaCar(this);
        }
//        json = LocalFileUtil.readTextFromSDcard(this);
        json = json.replace(" ", "");
        setData(json);
    }

    private void setData(String json) {
        SourceDateList.clear();
        objects = GsonUtil.praseJsonToList(json, SortModel.class);
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
       /* CarTypeEvent event = new CarTypeEvent();
        event.setType(type);
        event.setName(SourceDateList.get(position).getName());
        EventBus.getDefault().post(event);*/
        Intent intent = new Intent();
        intent.setClass(this, QPSListActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("car",SourceDateList.get(position).getName());
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.close)
    public void onViewClicked(View view) {
        finish();
    }
}
