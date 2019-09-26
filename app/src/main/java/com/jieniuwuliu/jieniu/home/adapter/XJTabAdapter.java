package com.jieniuwuliu.jieniu.home.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;

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
