package com.jieniuwuliu.jieniu.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.TimeUtil;
import com.jieniuwuliu.jieniu.bean.Coupon;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketAdater extends RecyclerView.Adapter<TicketAdater.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<Coupon.DataBean> list;

    public TicketAdater(Context context, List<Coupon.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.ticket_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        Coupon.DataBean item = list.get(i);
        if (item.isUse()){//判断优惠券是否使用
           viewHolder.imgBg.setImageResource(R.mipmap.icon_used);
        }else {
            if (TimeUtil.isExprie(item.getCouponTime())){//判断优惠券是否过期
                viewHolder.imgBg.setImageResource(R.mipmap.icon_exprise);
            }else{
                viewHolder.imgBg.setImageResource(R.mipmap.icon_un_used);
            }
        }
        if (item.getUseMoney()==0){
            viewHolder.tvMoney.setText("免运费");
        }else{
            viewHolder.tvMoney.setText("¥  "+item.getMoney()/100);
        }
        viewHolder.tvTime.setText("有效期至"+item.getCouponTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, (Integer) view.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.img_bg)
        ImageView imgBg;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
