package com.jieniuwuliu.jieniu.qipeishang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.home.adapter.StoreServiceAdapter;
import com.jieniuwuliu.jieniu.util.GlideUtil;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        StoreBean.DataBean item = list.get(i);
        viewHolder.tvName.setText(item.getNickname());
        viewHolder.tvAddress.setText("地址："+item.getAddress().getAddress().replace("陕西省",""));
        GlideUtil.setImgUrl(context,item.getShopPhoto(),viewHolder.img);
        String s = "";
        if (item.getPersonType()==1||item.getPersonType()==8){
            if (item.getFuwuCar().size()>0){
                viewHolder.tvCars.setText("经营车型：");
                viewHolder.rv.setVisibility(View.VISIBLE);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                viewHolder.rv.setLayoutManager(manager);
                CartypeAdapter adapter = new CartypeAdapter(context,item.getFuwuCar());
                viewHolder.rv.setAdapter(adapter);
            }else{
                viewHolder.tvCars.setText("经营车型：暂无车型");
                viewHolder.rv.setVisibility(View.GONE);
            }
        }else{
            viewHolder.rv.setVisibility(View.GONE);
            viewHolder.tvCars.setText("主营业务："+item.getYewu());
        }
       /* if (item.getFuwuCar().size()>0){

        }else{
            if (item.getPersonType() == 1){

            }else{

            }
        }*/
        if ("".equals(item.getLabel())){
            viewHolder.rvType.setVisibility(View.GONE);
        }else{
            viewHolder.rvType.setVisibility(View.VISIBLE);
            String[] array = item.getLabel().split(",");
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.rvType.setLayoutManager(manager);
            StoreServiceAdapter adapter = new StoreServiceAdapter(context,array);
            viewHolder.rvType.setAdapter(adapter);
        }
        viewHolder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.callPhone(i);
            }
        });
    }
    public interface CallBack{
        void callPhone(int positon);
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
        @BindView(R.id.rv_type)
        RecyclerView rvType;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
