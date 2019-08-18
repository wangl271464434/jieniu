package com.jieniuwuliu.jieniu.home.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XjInfo;
import com.jieniuwuliu.jieniu.home.XJContentActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XJTabAdapter extends RecyclerView.Adapter<XJTabAdapter.ViewHolder> {

    private Activity context;
    private String[] array;
    public XJTabAdapter(Activity context,String[] array) {
        this.array = array;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xj_tab_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        viewHolder.tvTab.setText(array[i]);
    }

    @Override
    public int getItemCount() {
        return array.length;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tab)
        TextView tvTab;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
