package com.jieniuwuliu.jieniu.peisongyuan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Address;
import com.jieniuwuliu.jieniu.bean.PSOrderInfo;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RenwuAdapter extends RecyclerView.Adapter<RenwuAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private OnItemClickListener listener;
    private List<PSOrderInfo.DataBean> list;

    public RenwuAdapter(Context context,List<PSOrderInfo.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.renwu_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        if (list.size()>0){
            int a = i+1;
            viewHolder.tvTab.setText("任务"+a);
            viewHolder.tvNo.setText(list.get(i).getOrderNumber());
            viewHolder.tvAddress.setText(list.get(i).getFromAddress());
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
        @BindView(R.id.tv_tab)
        TextView tvTab;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.tv_address)
        TextView tvAddress;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
