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
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.Follow;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<Follow.DataBean> list;

    public FollowAdapter(Context context, List<Follow.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.follow_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        Follow.DataBean item = list.get(i);
        GlideUtil.setImgUrl(context,item.getStore().getShopPhoto(),viewHolder.img);
        viewHolder.tvName.setText(item.getStore().getNickname());
        viewHolder.tvAddress.setText(item.getStore().getAddress().getAddress());
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
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_wechat)
        TextView tvWechat;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
