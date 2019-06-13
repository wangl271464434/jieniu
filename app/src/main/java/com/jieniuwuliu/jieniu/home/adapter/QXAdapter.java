package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.DistanceUtil;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.StoreBean;
import com.jieniuwuliu.jieniu.home.QXActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lljjcoder.citypickerview.utils.JLogUtils.D;

public class QXAdapter extends RecyclerView.Adapter<QXAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<StoreBean.DataBean> list;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public QXAdapter(Context context, List<StoreBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.qx_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        StoreBean.DataBean item = list.get(i);
        GlideUtil.setImgUrl(context,item.getShopPhoto(),viewHolder.img);
        viewHolder.name.setText(item.getNickname());
        viewHolder.address.setText("地址："+item.getAddress().getAddress());
        viewHolder.yewu.setText("主营业务："+item.getYewu());
        if (item.getDistance()>1000){
            DecimalFormat decimalFomat = new DecimalFormat("#0.00");
            viewHolder.distance.setText(decimalFomat.format(item.getDistance()/1000)+"km");
        }else{
            if (item.getDistance()<100){
                viewHolder.distance.setText("<100m");
            }else{
                viewHolder.distance.setText(item.getDistance()+"m");
            }
        }
        viewHolder.tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.chooseMap(i);
            }
        });
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
    public interface CallBack{
        void chooseMap(int position);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.yewu)
        TextView yewu;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.tv_go)
        LinearLayout tvGo;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
