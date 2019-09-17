package com.jieniuwuliu.jieniu.luntan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.PingLun;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.listener.OnItemLongClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LuntanInfoAdater extends RecyclerView.Adapter<LuntanInfoAdater.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private List<PingLun> list;
    public LuntanInfoAdater(Context context,List<PingLun> list) {
        this.context = context;
        this.list = list;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.luntan_info_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if (list.size()!=0){
            if (list.get(i)!=null){
                String s = "";
                if (!list.get(i).getRname().equals("")){
                    s = "<font color='#316487'>"+list.get(i).getName()+"@"+list.get(i).getRname()+":"+"</font>"+"<font color='#484848'>"+list.get(i).getInfo()+"</font>";

                }else {
                    s = "<font color='#316487'>"+list.get(i).getName()+":"+"</font>"+"<font color='#484848'>"+list.get(i).getInfo()+"</font>";
                }
                viewHolder.tvName.setText(Html.fromHtml(s));
            }
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

    @Override
    public boolean onLongClick(View view) {
        if (longClickListener!=null){
            longClickListener.onItemLongClick(view, (Integer) view.getTag());
        }
        return false;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_name)
        TextView tvName;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
