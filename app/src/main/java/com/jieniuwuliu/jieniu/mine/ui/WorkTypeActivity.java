package com.jieniuwuliu.jieniu.mine.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.messageEvent.CarEvent;
import com.jieniuwuliu.jieniu.bean.WorkType;
import com.jieniuwuliu.jieniu.mine.adapter.WorkTypeAdater;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主营业务
 */
public class WorkTypeActivity extends BaseActivity implements WorkTypeAdater.CallBack {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.right)
    TextView right;
    private List<WorkType> list;
    private List<WorkType> checkList;
    private List<WorkType> recevieList;
    private WorkTypeAdater adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_type;
    }

    @Override
    protected void init() {
        title.setText("主营业务");
        right.setText("确定");
        checkList = new ArrayList<>();
        recevieList = new ArrayList<>();
        list = new ArrayList<>();
        list.add(new WorkType("钣金烤漆"));
        list.add(new WorkType("机修电路"));
        list.add(new WorkType("保险理赔"));
        list.add(new WorkType("救援服务"));
        list.add(new WorkType("装潢美容"));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new WorkTypeAdater(this,list);
        rv.setAdapter(adapter);
        adapter.setCallBack(this);
    }

    @OnClick({R.id.back, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                CarEvent carEvent = new CarEvent();
                carEvent.setList(checkList);
                carEvent.setType("work");
                EventBus.getDefault().post(carEvent);
                finish();

                break;
        }
    }

    @Override
    public void check(int position, boolean b) {
        if (b){
            checkList.add(list.get(position));
        }else {
            checkList.remove(list.get(position));
        }
    }
}
