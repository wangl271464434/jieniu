package com.jieniuwuliu.jieniu;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.LocalFileUtil;
import com.jieniuwuliu.jieniu.Util.PinyinComparator;
import com.jieniuwuliu.jieniu.adapter.SortAdapter;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.bean.SortModel;
import com.jieniuwuliu.jieniu.view.SideBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 车型
 */
public class CarTypeActivity extends BaseActivity implements SortAdapter.CallBack {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    private LinearLayoutManager manager;
    private SortAdapter adapter;
    private List<Object> objects;
    private List<SortModel> SourceDateList;
    private List<SortModel> list;//要传的数组
    private List<SortModel> chooseList;//要传的数组
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private String json,type;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_car_type;
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        chooseList = (List<SortModel>) getIntent().getSerializableExtra("list");
        type = getIntent().getStringExtra("type");
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
        if (type.equals("小型汽车")){
            json = LocalFileUtil.readTextFromSDcard(this);
        }else{
            json = LocalFileUtil.readKaCar(this);
        }
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
        if (chooseList.size()!=0){
            for (int i =0;i<SourceDateList.size();i++){
                for (int j = 0;j<chooseList.size();j++){
                    if (SourceDateList.get(i).getName().equals(chooseList.get(j).getName())){
                        SourceDateList.set(i,chooseList.get(j));
                    }
                }
            }
        }
        adapter = new SortAdapter(this, SourceDateList);
        recyclerView.setAdapter(adapter);
        adapter.setCallBack(this);
    }
    @OnClick(R.id.tv_sure)
    void onClick(){
        if (chooseList.size()>0){
            list.addAll(chooseList);
        }
        CarEvent carEvent = new CarEvent();
        carEvent.setSortModelList(list);
        carEvent.setType("car");
        EventBus.getDefault().post(carEvent);
        finish();
    }
    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void isChecked(int position, boolean isChecked) {
        if (isChecked){
            list.add(SourceDateList.get(position));
        }else{
            if (chooseList.size()>0){
                chooseList.remove(SourceDateList.get(position));
            }
            list.remove(SourceDateList.get(position));
        }

    }
}
