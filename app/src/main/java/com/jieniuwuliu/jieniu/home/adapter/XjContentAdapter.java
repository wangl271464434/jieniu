package com.jieniuwuliu.jieniu.home.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XjInfo;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XjContentAdapter extends RecyclerView.Adapter<XjContentAdapter.ViewHolder> implements View.OnClickListener {

    private Activity context;
    private OnItemClickListener listener;
    private List<XjInfo.DataBean> list;

    public XjContentAdapter(Activity context, List<XjInfo.DataBean> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xj_content_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        XjInfo.DataBean item = list.get(i);
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvTime.setText(item.getUpdatedAt());
        String info = "";
        List<Object> objects = GsonUtil.praseJsonToList(item.getPartslist(),Machine.class);
        for (int j = 0;j<objects.size();j++) {
            Machine machine = (Machine) objects.get(j);
            if (j==0){
                info += machine.getName()+"   "+machine.getMoney();
            }else{
                info += "\n"+ machine.getName()+"   "+machine.getMoney();
            }
        }
        viewHolder.tvInfo.setText(info);
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_call)
        TextView tvCall;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
