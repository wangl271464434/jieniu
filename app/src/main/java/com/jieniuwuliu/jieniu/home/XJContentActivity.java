package com.jieniuwuliu.jieniu.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.base.BaseActivity;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XJOrder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XJContentActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private XJOrder.DataBean data;
    private String token;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_xjcontent;
    }

    @Override
    protected void init() {
        data = (XJOrder.DataBean) getIntent().getSerializableExtra("data");
        token = (String) SPUtil.get(this, Constant.TOKEN,Constant.TOKEN,"");
        GlideUtil.setImgUrl(this,data.getLogos(),img);
        tvTime.setText(data.getCreatedat());
        tvName.setText(data.getCarbrand());
        List<Object> list = GsonUtil.praseJsonToList(data.getPartslist(), Machine.class);
        String info = "";
        for (int j = 0;j<list.size();j++){
            Machine machine = (Machine) list.get(j);
            if (j==0){
                info += machine.getName();
            }else{
                info += ","+machine.getName();
            }
        }
        tvInfo.setText("配件："+info);
        getData();
    }
    /**
     * 获取数据
     * */
    private void getData() {
    }

    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }

}
