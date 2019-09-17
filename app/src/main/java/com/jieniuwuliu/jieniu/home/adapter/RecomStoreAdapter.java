package com.jieniuwuliu.jieniu.home.adapter;

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
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.RecomStore;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.qipeishang.adapter.CartypeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 推荐商家适配器
 * */
public class RecomStoreAdapter extends RecyclerView.Adapter<RecomStoreAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<RecomStore.DataBean> list;
    public RecomStoreAdapter(Context context, List<RecomStore.DataBean> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recom_store_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if (list.size()>0){
            RecomStore.DataBean item = list.get(i);
            switch (item.getPsntype()){
                case 1:
                    if (item.getFuwuCar()!=null){
                        viewHolder.rv.setVisibility(View.VISIBLE);
                        viewHolder.tvInfo.setVisibility(View.GONE);
                        LinearLayoutManager carManager = new LinearLayoutManager(context);
                        carManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        viewHolder.rv.setLayoutManager(carManager);
                        CartypeAdapter cartypeAdapter = new CartypeAdapter(context,item.getFuwuCar());
                        viewHolder.rv.setAdapter(cartypeAdapter);
                    }
                    break;
                    default:
                        viewHolder.rv.setVisibility(View.GONE);
                        if (!"".equals(item.getYewu())){
                            viewHolder.tvInfo.setVisibility(View.VISIBLE);
                            viewHolder.tvInfo.setText("经营范围："+item.getYewu());
                        }
                        break;
            }
            GlideUtil.setRoundImg(context,item.getPhoto(),viewHolder.img);
            viewHolder.name.setText(item.getName());
            viewHolder.address.setText(item.getAddress().replace("陕西省",""));
            if (item.getLabel().equals("")){
               viewHolder.recyclerView.setVisibility(View.GONE);
            }else{
                viewHolder.recyclerView.setVisibility(View.VISIBLE);
                String[] array = item.getLabel().split(",");
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                viewHolder.recyclerView.setLayoutManager(manager);
                StoreServiceAdapter adapter = new StoreServiceAdapter(context,array);
                viewHolder.recyclerView.setAdapter(adapter);
            }

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
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.rv)
        RecyclerView rv;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
