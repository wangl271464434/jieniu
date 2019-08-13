package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.JwtUtil;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.jijian.JiJianSelectActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<OrderInfo> list;
    private  View view;
//    private ViewHolder holder;
//    private FootView footView;
    public HomeAdapter(Context context,List<OrderInfo> list) {
        this.context = context;
        this.list = list;
//        setData(list);
    }

/*   public void setData(List<OrderInfo> data) {
        list = new ArrayList<>();
        list.addAll(data);
        if (list.size()>0){
            list.add(new OrderInfo());
        }
        notifyDataSetChanged();
    }*/

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

  /*  @Override
    public int getItemViewType(int position) {
        if (position == list.size()-1){
            return 1;
        }else{
            return 0;
        }
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.home_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        OrderInfo item = list.get(i);
        viewHolder.tvNo.setText(item.getOrderNumber());
        String token = (String) SPUtil.get(context,Constant.TOKEN,Constant.TOKEN,"");
        viewHolder.tvStartAdr.setText(item.getFromName());
        viewHolder.tvEndAdr.setText(item.getToName());
        viewHolder.info.setText(item.getInfo());
        viewHolder.tvNum.setText(item.getNumber()+"件");
        if (item.getFromUid() == Integer.valueOf(JwtUtil.JWTParse(token))){
            viewHolder.img.setImageResource(R.mipmap.ic_home_jijian);
        }else{
            viewHolder.img.setImageResource(R.mipmap.ic_home_shoujian);
        }
        if (item.getOrderList()!=null){
            if (item.getOrderList().size()>0){
                viewHolder.imgStart.setVisibility(View.INVISIBLE);
                viewHolder.imgMiddle.setVisibility(View.VISIBLE);
                viewHolder.imgEnd.setVisibility(View.INVISIBLE);
                viewHolder.bar.setSecondaryProgress(50);
                viewHolder.tvMiddle.setText("配送中");
            }else{
                viewHolder.imgStart.setVisibility(View.VISIBLE);
                viewHolder.imgMiddle.setVisibility(View.INVISIBLE);
                viewHolder.imgEnd.setVisibility(View.INVISIBLE);
                viewHolder.bar.setSecondaryProgress(0);
            }
        }else {
            viewHolder.imgStart.setVisibility(View.VISIBLE);
            viewHolder.imgMiddle.setVisibility(View.INVISIBLE);
            viewHolder.imgEnd.setVisibility(View.INVISIBLE);
            viewHolder.bar.setSecondaryProgress(0);
        }
     /*   switch (getItemViewType(i)){
            case 0:

                break;
            case 1:
                footView = (FootView) viewHolder;

                break;
        }*/

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
  /*  static class FootView extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_more)
        TextView tvMore;
        FootView(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }*/
}
