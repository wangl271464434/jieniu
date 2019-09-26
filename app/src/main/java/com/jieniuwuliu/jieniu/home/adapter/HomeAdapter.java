package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.JwtUtil;
import com.jieniuwuliu.jieniu.util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.jijian.JiJianSelectActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<OrderInfo> list;
    private  View view;
    private ViewHolder holder;
    private FootView footView;
    public HomeAdapter(Context context,List<OrderInfo> list) {
        this.context = context;
        this.list = list;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()-1){
            return 1;
        }else{
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (i){
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.home_item,viewGroup,false);
                viewHolder = new ViewHolder(view);
                view.setOnClickListener(this);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.home_footer,viewGroup,false);
                viewHolder = new FootView(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        switch (getItemViewType(i)){
            case 0:
                holder = (ViewHolder) viewHolder;
                OrderInfo item = list.get(i);
                holder.tvNo.setText(item.getOrderNumber());
                String token = (String) SPUtil.get(context,Constant.TOKEN,Constant.TOKEN,"");
                holder.tvStartAdr.setText(item.getFromName());
                holder.tvEndAdr.setText(item.getToName());
                holder.info.setText(item.getInfo());
                holder.tvNum.setText(item.getNumber()+"件");
                if (item.getFromUid() == Integer.valueOf(JwtUtil.JWTParse(token))){
                    holder.img.setImageResource(R.mipmap.ic_home_jijian);
                }else{
                    holder.img.setImageResource(R.mipmap.ic_home_shoujian);
                }
                if (item.getOrderList()!=null){
                    if (item.getOrderList().size()>0){
                        holder.imgStart.setVisibility(View.INVISIBLE);
                        holder.imgMiddle.setVisibility(View.VISIBLE);
                        holder.imgEnd.setVisibility(View.INVISIBLE);
                        holder.bar.setSecondaryProgress(50);
                        holder.tvMiddle.setText("配送中");
                    }else{
                        holder.imgStart.setVisibility(View.VISIBLE);
                        holder.imgMiddle.setVisibility(View.INVISIBLE);
                        holder.imgEnd.setVisibility(View.INVISIBLE);
                        holder.bar.setSecondaryProgress(0);
                    }
                }else {
                    holder.imgStart.setVisibility(View.VISIBLE);
                    holder.imgMiddle.setVisibility(View.INVISIBLE);
                    holder.imgEnd.setVisibility(View.INVISIBLE);
                    holder.bar.setSecondaryProgress(0);
                }
                break;
            case 1:
                footView = (FootView) viewHolder;
                footView.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(context, JiJianSelectActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.info)
        TextView info;
        @BindView(R.id.tv_middle)
        TextView tvMiddle;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.bar)
        ProgressBar bar;
        @BindView(R.id.img_start)
        ImageView imgStart;
        @BindView(R.id.tv_start_adr)
        TextView tvStartAdr;
        @BindView(R.id.tv_end_adr)
        TextView tvEndAdr;
        @BindView(R.id.img_middle)
        ImageView imgMiddle;
        @BindView(R.id.img_end)
        ImageView imgEnd;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    static class FootView extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_more)
        TextView tvMore;
        FootView(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
