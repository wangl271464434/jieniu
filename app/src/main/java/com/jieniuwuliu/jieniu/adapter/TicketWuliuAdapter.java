package com.jieniuwuliu.jieniu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Coupon;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 按字母排序的适配器
 */
public class TicketWuliuAdapter extends RecyclerView.Adapter<TicketWuliuAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Coupon.DataBean> mData;
    private Context mContext;

    public TicketWuliuAdapter(Context context, List<Coupon.DataBean> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wuliu_ticket_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        Coupon.DataBean item = mData.get(position);
        holder.tvPrice.setText(String.valueOf(item.getMoney()/100));
        holder.tvTime.setText("有效期至："+item.getCouponTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
