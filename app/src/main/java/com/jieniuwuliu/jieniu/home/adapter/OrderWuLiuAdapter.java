package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.OrderInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderWuLiuAdapter extends RecyclerView.Adapter<OrderWuLiuAdapter.ViewHolder> {
    private Context context;
    private List<OrderInfo.OrderListBean> list;

    public OrderWuLiuAdapter(Context context, List<OrderInfo.OrderListBean> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wuliu_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.itemView.setTag(i);
        if (list.size() != 0) {
            OrderInfo.OrderListBean item = list.get(i);
            if (i == 0) {
                if (list.size() > 1) {
                    holder.tvUpLine.setVisibility(View.INVISIBLE);
                    holder.tvDownLine.setVisibility(View.VISIBLE);
                } else {
                    holder.tvUpLine.setVisibility(View.INVISIBLE);
                    holder.tvDownLine.setVisibility(View.INVISIBLE);
                }

            } else {
                if (i == list.size() - 1) {
                    holder.tvUpLine.setVisibility(View.VISIBLE);
                    holder.tvDownLine.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvUpLine.setVisibility(View.VISIBLE);
                    holder.tvDownLine.setVisibility(View.VISIBLE);
                }

            }
            holder.tvState.setText(list.get(i).getMsg());
            holder.tvInfo.setText(list.get(i).getName() + list.get(i).getMsg());
            switch (list.get(i).getMsg()) {
                case "已发货":
                    holder.imgIcon.setImageResource(R.mipmap.ic_order_fahuo);
                    break;
                case "分拣中":
                    holder.imgIcon.setImageResource(R.mipmap.ic_order_zhongzhuan);
                    break;
                case "配送中":
                    holder.imgIcon.setImageResource(R.mipmap.ic_order_yunshu);
                    break;
                case "已签收":
                    holder.imgIcon.setImageResource(R.mipmap.ic_order_wancheng);
                    break;
            }
            holder.tvTime.setText(item.getCreatedAt());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_up_line)
        TextView tvUpLine;
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_down_line)
        TextView tvDownLine;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_info)
        TextView tvInfo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
