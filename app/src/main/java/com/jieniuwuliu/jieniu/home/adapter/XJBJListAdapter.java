package com.jieniuwuliu.jieniu.home.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XjInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XJBJListAdapter extends RecyclerView.Adapter<XJBJListAdapter.ViewHolder> {
    private Activity context;
    private List<Machine> list;

    public XJBJListAdapter(Activity context, List<Machine> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xj_bj_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        Machine item = list.get(i);
        viewHolder.tvName.setText((i+1)+"."+item.getName());
        viewHolder.tvType.setText(item.getType());
        if ("".equals(item.getMoney())){
            viewHolder.tvPrice.setText("未报价");
        }else{
            viewHolder.tvPrice.setText("¥  "+item.getMoney());
        }
    }

    public interface CallBack {
        void sureInfo(XjInfo.DataBean item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}