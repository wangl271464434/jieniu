package com.jieniuwuliu.jieniu.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.jieniuwuliu.jieniu.home.OrderInfoActivity;
import com.jieniuwuliu.jieniu.util.JwtUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeOrderAdapter extends PagerAdapter {
    private List<OrderInfo> list;
    private Context context;

    public HomeOrderAdapter(Context context, List<OrderInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        OrderInfo item = list.get(position);
        viewHolder.tvNo.setText(item.getOrderNumber());
        String token = (String) SPUtil.get(context, Constant.TOKEN, Constant.TOKEN, "");
        viewHolder.tvStartAdr.setText(item.getFromName());
        viewHolder.tvEndAdr.setText(item.getToName());
        viewHolder.info.setText(item.getInfo());
        viewHolder.tvNum.setText(item.getNumber() + "件");
        if (item.getFromUid() == Integer.valueOf(JwtUtil.JWTParse(context,token))) {
            viewHolder.img.setImageResource(R.mipmap.ic_home_jijian);
        } else {
            viewHolder.img.setImageResource(R.mipmap.ic_home_shoujian);
        }
        if (item.getOrderList() != null) {
            if (item.getOrderList().size() > 0) {
                viewHolder.imgStart.setVisibility(View.INVISIBLE);
                viewHolder.imgMiddle.setVisibility(View.VISIBLE);
                viewHolder.imgEnd.setVisibility(View.INVISIBLE);
                viewHolder.bar.setSecondaryProgress(50);
                viewHolder.tvMiddle.setText("配送中");
            } else {
                viewHolder.imgStart.setVisibility(View.VISIBLE);
                viewHolder.imgMiddle.setVisibility(View.INVISIBLE);
                viewHolder.imgEnd.setVisibility(View.INVISIBLE);
                viewHolder.bar.setSecondaryProgress(0);
            }
        } else {
            viewHolder.imgStart.setVisibility(View.VISIBLE);
            viewHolder.imgMiddle.setVisibility(View.INVISIBLE);
            viewHolder.imgEnd.setVisibility(View.INVISIBLE);
            viewHolder.bar.setSecondaryProgress(0);
        }
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,OrderInfoActivity.class);
                intent.putExtra("orderNo",item.getOrderNumber());
                context.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    static class ViewHolder {
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
        @BindView(R.id.container)
        RelativeLayout container;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
