package com.jieniuwuliu.jieniu.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.base.BaseFragment;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.home.OrderInfoActivity;
import com.jieniuwuliu.jieniu.util.JwtUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PagerFragment extends BaseFragment {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.img_start)
    ImageView imgStart;
    @BindView(R.id.img_middle)
    ImageView imgMiddle;
    @BindView(R.id.img_end)
    ImageView imgEnd;
    @BindView(R.id.start)
    ImageView start;
    @BindView(R.id.bar)
    ProgressBar bar;
    @BindView(R.id.end)
    ImageView end;
    @BindView(R.id.tv_start_adr)
    TextView tvStartAdr;
    @BindView(R.id.tv_middle)
    TextView tvMiddle;
    @BindView(R.id.tv_end_adr)
    TextView tvEndAdr;
    private OrderInfo item;

    public void setItem(OrderInfo item) {
        this.item = item;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.home_item;
    }

    @Override
    protected void init() {
        tvNo.setText(item.getOrderNumber());
        String token = (String) SPUtil.get(getActivity(), Constant.TOKEN, Constant.TOKEN, "");
        tvStartAdr.setText(item.getFromName());
        tvEndAdr.setText(item.getToName());
        info.setText(item.getInfo());
        tvNum.setText(item.getNumber() + "件");
        if (item.getFromUid() == Integer.valueOf(JwtUtil.JWTParse(token))) {
            img.setImageResource(R.mipmap.ic_home_jijian);
        } else {
            img.setImageResource(R.mipmap.ic_home_shoujian);
        }
        if (item.getOrderList() != null) {
            if (item.getOrderList().size() > 0) {
                imgStart.setVisibility(View.INVISIBLE);
                imgMiddle.setVisibility(View.VISIBLE);
                imgEnd.setVisibility(View.INVISIBLE);
                bar.setSecondaryProgress(50);
                tvMiddle.setText("配送中");
            } else {
                imgStart.setVisibility(View.VISIBLE);
                imgMiddle.setVisibility(View.INVISIBLE);
                imgEnd.setVisibility(View.INVISIBLE);
                bar.setSecondaryProgress(0);
            }
        } else {
            imgStart.setVisibility(View.VISIBLE);
            imgMiddle.setVisibility(View.INVISIBLE);
            imgEnd.setVisibility(View.INVISIBLE);
            bar.setSecondaryProgress(0);
        }
    }

    @OnClick(R.id.container)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(getActivity(),OrderInfoActivity.class);
        intent.putExtra("orderNo",item.getOrderNumber());
        getActivity().startActivity(intent);
    }
}
