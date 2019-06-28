package com.jieniuwuliu.jieniu.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.JwtUtil;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
import com.jieniuwuliu.jieniu.bean.OrderResult;
import com.jieniuwuliu.jieniu.jijian.PayTypeActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JiJianSelectAdater extends RecyclerView.Adapter<JiJianSelectAdater.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<OrderInfo> list;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public JiJianSelectAdater(Context context, List<OrderInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.jijian_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        String token = (String) SPUtil.get(context, Constant.TOKEN, Constant.TOKEN, "");
        if (list.size()!=0){
            if (list.get(i).getFromUid() == Integer.valueOf(JwtUtil.JWTParse(token))) {
                viewHolder.imgType.setImageResource(R.mipmap.ic_home_jijian);
            } else {
                viewHolder.imgType.setImageResource(R.mipmap.ic_home_shoujian);
            }
            if (list.get(i).getPayType()==0){
                viewHolder.tvPay.setVisibility(View.VISIBLE);
            }else{
                viewHolder.tvPay.setVisibility(View.GONE);
            }
            if (!list.get(i).getInfo().equals("")){
                viewHolder.tvInfo.setText("物品信息："+list.get(i).getInfo());
            }else{
                viewHolder.tvInfo.setText("物品信息：暂无");
            }
            viewHolder.tvNo.setText("运单号："+list.get(i).getOrderNumber());
            viewHolder.tvNum.setText("x"+list.get(i).getNumber()+"件");
            viewHolder.tvTime.setText("下单时间："+list.get(i).getCreatedAt().replace("T"," ").replace("Z",""));
            float price  = list.get(i).getTotalMoney()/100;
            viewHolder.tvPrice.setText("¥ "+String.format("%.2f",price));
            if (list.get(i).getStatus() ==5){
                viewHolder.imgFinish.setVisibility(View.VISIBLE);
                viewHolder.imgFinish.setImageResource(R.mipmap.ic_send_finished);
            }else{
                viewHolder.imgFinish.setVisibility(View.GONE);
            }
            if (list.get(i).isCancel()){//先进性判断能不能被取消
                if (list.get(i).isCancelStatus()){//判断是否取消
                    viewHolder.layoutCancel.setVisibility(View.GONE);
                    viewHolder.imgFinish.setVisibility(View.VISIBLE);
                    viewHolder.imgFinish.setImageResource(R.mipmap.icon_yiquxiao);
                }else{
                    viewHolder.layoutCancel.setVisibility(View.VISIBLE);
                }
            }else{
                viewHolder.layoutCancel.setVisibility(View.GONE);
            }
            viewHolder.tvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context,PayTypeActivity.class);
                    intent.putExtra("orderNo",list.get(i).getOrderNumber());
                    intent.putExtra("price",list.get(i).getTotalMoney());
                    context.startActivity(intent);
                }
            });
            viewHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(i).setCancel(true);
                    list.get(i).setCancelStatus(true);
                    callBack.cancel(i);
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
        @BindView(R.id.img_type)
        ImageView imgType;
        @BindView(R.id.img_finish)
        ImageView imgFinish;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.layout_cancel)
        RelativeLayout layoutCancel;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_pay)
        TextView tvPay;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public interface CallBack{
        void cancel(int position);
    }
}
