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
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.MenuItem;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineAdater extends RecyclerView.Adapter<MineAdater.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<MenuItem> list;
    public MineAdater(Context context, List<MenuItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.mine_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if (i == 0){
           int isCertify = (int) SPUtil.get(context,Constant.ISCERTIFY,Constant.ISCERTIFY,0);
           if (isCertify == 4){
               viewHolder.tvCertify.setText("未认证");
               viewHolder.tvCertify.setVisibility(View.VISIBLE);
           }else if(isCertify == 3){
               viewHolder.tvCertify.setText("认证中");
               viewHolder.tvCertify.setVisibility(View.VISIBLE);
           }else{
               viewHolder.tvCertify.setVisibility(View.GONE);
           }
        }else if (i==1){
            boolean isBind = (boolean) SPUtil.get(context,Constant.BIND,Constant.BIND,false);
            if (isBind){
                viewHolder.tvCertify.setVisibility(View.GONE);
            }else{
                viewHolder.tvCertify.setVisibility(View.VISIBLE);
                viewHolder.tvCertify.setText("未绑定");
            }
        }
        viewHolder.img.setImageResource(list.get(i).getResId());
        viewHolder.name.setText(list.get(i).getName());
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
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tv_certify)
        TextView tvCertify;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
