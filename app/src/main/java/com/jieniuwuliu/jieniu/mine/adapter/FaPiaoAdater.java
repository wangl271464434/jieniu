package com.jieniuwuliu.jieniu.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.JwtUtil;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.TimeUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaPiaoAdater extends RecyclerView.Adapter<FaPiaoAdater.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<OrderInfo> list;

    public FaPiaoAdater(Context context, List<OrderInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fapiao_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        String token = (String) SPUtil.get(context, Constant.TOKEN, Constant.TOKEN, "");
        if (list.size()!=0){
            if (list.get(i).getFromUid() == Integer.valueOf(JwtUtil.JWTParse(token))) {
                viewHolder.imgType.setImageResource(R.mipmap.ic_home_jijian);
            } else {
                viewHolder.imgType.setImageResource(R.mipmap.ic_home_shoujian);
            }
            viewHolder.tvNo.setText("运单号："+list.get(i).getOrderNumber());
            viewHolder.tvNum.setText("x"+list.get(i).getNumber());
            viewHolder.tvTime.setText("下单时间："+list.get(i).getCreatedAt());
            float price  = list.get(i).getTotalMoney()/100;
            viewHolder.tvPrice.setText("¥ "+String.format("%.2f",price));
            if (list.get(i).isInvoice()){
                viewHolder.checkBox.setChecked(true);
            }else{
                viewHolder.checkBox.setChecked(false);
            }
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()){
                        if (isChecked){
                            list.get(i).setInvoice(true);
                        }else{
                            list.get(i).setInvoice(false);
                        }
                    }
                }
            });
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
        @BindView(R.id.checkbox)
        CheckBox checkBox;
        @BindView(R.id.img_type)
        ImageView imgType;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
