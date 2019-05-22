package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
   /* private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }*/

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
        LatLonPoint start = new LatLonPoint(QXActivity.currentLat,QXActivity.currentLng);
        LatLonPoint end = new LatLonPoint(item.getAddress().getLat(),item.getAddress().getLng());
//        DistanceUtil.getDistance(context,start,end,DistanceSearch.TYPE_DISTANCE);
        List<LatLonPoint> list = new ArrayList<>();
        list.add(start);
        DistanceSearch search = new DistanceSearch(context);
        DistanceSearch.DistanceQuery query = new DistanceSearch.DistanceQuery();
        query.setOrigins(list);//支持多起点
        query.setDestination(end);
        //设置测量方式，支持直线和驾车
        query.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);//设置为直线
        search.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                Double distance = Double.valueOf(distanceResult.getDistanceResults().get(0).getDistance());
                if (distance>1000){
                    DecimalFormat decimalFomat = new DecimalFormat("#0.00");
                    viewHolder.distance.setText(decimalFomat.format(distance/1000)+"km");
                }else{
                    viewHolder.distance.setText(distance+"m");
                }
            }
        });
        search.calculateRouteDistanceAsyn(query);
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
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
