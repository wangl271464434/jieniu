package com.jieniuwuliu.jieniu.qipeishang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.Car;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QiPeiShangListAdapter extends RecyclerView.Adapter<QiPeiShangListAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<StoreBean.DataBean> list;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public QiPeiShangListAdapter(Context context, List<StoreBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.qipeishang_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        StoreBean.DataBean item = list.get(i);
        viewHolder.tvName.setText(item.getNickname());
        viewHolder.tvAddress.setText("地址："+item.getAddress().getAddress());
        GlideUtil.setImgUrl(context,item.getShopPhoto(),viewHolder.img);
        String s = "";
        if (item.getFuwuCar().size()>0){
            viewHolder.rv.setVisibility(View.VISIBLE);
            viewHolder.tvCars.setVisibility(View.GONE);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.rv.setLayoutManager(manager);
            CartypeAdapter adapter = new CartypeAdapter(context,item.getFuwuCar());
            viewHolder.rv.setAdapter(adapter);
        }else{
            viewHolder.rv.setVisibility(View.GONE);
            viewHolder.tvCars.setVisibility(View.VISIBLE);
            viewHolder.tvCars.setText("经营范围："+item.getYewu());
        }

        viewHolder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.callPhone(i);
            }
        });
        viewHolder.tvWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.callWeChat(i);
            }
        });
    }
    public interface CallBack{
        void callPhone(int positon);
        void callWeChat(int positon);
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_cars)
        TextView tvCars;
        @BindView(R.id.rv)
        RecyclerView rv;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_wechat)
        TextView tvWechat;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
