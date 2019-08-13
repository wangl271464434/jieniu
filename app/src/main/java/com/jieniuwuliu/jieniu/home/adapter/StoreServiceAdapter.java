package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.RecomStore;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 推荐商家适配器
 * */
public class StoreServiceAdapter extends RecyclerView.Adapter<StoreServiceAdapter.ViewHolder>{
    private Context context;
    private List<String> list;
    public StoreServiceAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_service_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if ("包邮".equals(list.get(i))){
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.tv.setBackgroundResource(R.drawable.bg_red_shape);
        }else{
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.orange));
            viewHolder.tv.setBackgroundResource(R.drawable.orange_shape);
        }
        viewHolder.tv.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv)
        TextView tv;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
