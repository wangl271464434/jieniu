package com.jieniuwuliu.jieniu.mine.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.AddressItem;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapAddressAdapter extends RecyclerView.Adapter<MapAddressAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<AddressItem> list;
    public MapAddressAdapter(Context context, List<AddressItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.map_address_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        PoiItem poiItem = list.get(i).getPoiItem();
        if (list.get(i).isChecked()){
            viewHolder.img.setImageResource(R.mipmap.ic_fapiao_checked);
        }else{
            viewHolder.img.setImageResource(R.mipmap.ic_fapiao_unchecked);

        }
        viewHolder.tvAddress.setText(poiItem.getAdName()+poiItem.toString());
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
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.img)
        ImageView img;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
