package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.TimeUtil;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderWuLiuAdapter extends RecyclerView.Adapter<OrderWuLiuAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<OrderInfo.OrderListBean> list;
    public OrderWuLiuAdapter(Context context, List<OrderInfo.OrderListBean> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.wuliu_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if (list.size()!=0){
            OrderInfo.OrderListBean item = list.get(i);
            if (i==0){
                if (list.size()>1){
                    viewHolder.tvUpLine.setVisibility(View.INVISIBLE);
                    viewHolder.tvDownLine.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.tvUpLine.setVisibility(View.INVISIBLE);
                    viewHolder.tvDownLine.setVisibility(View.INVISIBLE);
                }

            }else {
                if (i == list.size()-1){
                    viewHolder.tvUpLine.setVisibility(View.VISIBLE);
                    viewHolder.tvDownLine.setVisibility(View.INVISIBLE);
                }else{
                    viewHolder.tvUpLine.setVisibility(View.VISIBLE);
                    viewHolder.tvDownLine.setVisibility(View.VISIBLE);
                }

            }
            viewHolder.tvState.setText(list.get(i).getMsg());
            viewHolder.tvInfo.setText(list.get(i).getName()+list.get(i).getMsg());
            switch (list.get(i).getMsg()){
                case "已发货":
                    viewHolder.imgIcon.setImageResource(R.mipmap.ic_order_fahuo);
                    break;
                case "分拣中":
                    viewHolder.imgIcon.setImageResource(R.mipmap.ic_order_zhongzhuan);
                    break;
                case "配送中":
                    viewHolder.imgIcon.setImageResource(R.mipmap.ic_order_yunshu);
                    break;
                case "已签收":
                    viewHolder.imgIcon.setImageResource(R.mipmap.ic_order_wancheng);
                    break;
            }
            viewHolder.tvTime.setText(item.getCreatedAt());

        }

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


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_up_line)
        TextView tvUpLine;
        @BindView(R.id.tv_down_line)
        TextView tvDownLine;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
