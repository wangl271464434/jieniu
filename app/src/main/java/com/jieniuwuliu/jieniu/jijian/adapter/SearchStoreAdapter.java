package com.jieniuwuliu.jieniu.jijian.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.SearchStore;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchStoreAdapter extends RecyclerView.Adapter<SearchStoreAdapter.ViewHolder> implements View.OnClickListener {
    private Activity context;
    private OnItemClickListener listener;
    private List<SearchStore.DataBean> list;
    private String[] permissions = new String[]{Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS};
    public SearchStoreAdapter(Activity context, List<SearchStore.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_store_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        viewHolder.name.setText(list.get(i).getNickname());
        String phone = list.get(i).getAddress().getPhone();
        String str = "";
        if (phone.length()>8){
            str = phone.substring(0,3)+"*****"+phone.substring(8,11);
        }else{
            str = phone;
        }
        viewHolder.phone.setText("联系电话："+str);
        GlideUtil.setImgUrl(context,list.get(i).getShopPhoto(),viewHolder.img);
        viewHolder.address.setText("地址："+list.get(i).getAddress().getAddress().replace("陕西省",""));
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
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.address)
        TextView address;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
