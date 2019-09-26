package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 推荐商家适配器
 * */
public class StoreServiceAdapter extends RecyclerView.Adapter<StoreServiceAdapter.ViewHolder>{
    private Context context;
    private String[] array;
    public StoreServiceAdapter(Context context, String[] array) {
        this.array = array;
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
        if ("包邮".equals(array[i])){
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.tv.setBackgroundResource(R.drawable.bg_red_shape);
        }else{
            viewHolder.tv.setTextColor(context.getResources().getColor(R.color.orange));
            viewHolder.tv.setBackgroundResource(R.drawable.orange_shape);
        }
        viewHolder.tv.setText(array[i]);
    }

    @Override
    public int getItemCount() {
        return array.length;
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
