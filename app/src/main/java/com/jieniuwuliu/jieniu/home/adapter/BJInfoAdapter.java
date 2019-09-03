package com.jieniuwuliu.jieniu.home.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.home.BjInfoActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BJInfoAdapter extends RecyclerView.Adapter<BJInfoAdapter.ViewHolder> implements View.OnClickListener {
    private Activity context;
    private OnItemClickListener listener;
    private List<Machine> list;
    public BJInfoAdapter(Activity context, List<Machine> list) {
        this.list = list;
        this.context = context;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.bj_info_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        Machine item = list.get(i);
        viewHolder.tvName.setText((i+1)+"."+item.getName());
        viewHolder.etExp.setText(item.getExp());
        LinearLayoutManager manager = new LinearLayoutManager(context);
        viewHolder.recyclerView.setLayoutManager(manager);
        BJTypeAdapter adapter = new BJTypeAdapter(context,item.getList());
        viewHolder.recyclerView.setAdapter(adapter);
        if (BjInfoActivity.state == 1){
            viewHolder.etExp.setEnabled(true);
        }else{
            viewHolder.etExp.setEnabled(false);
        }
        viewHolder.etExp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String exp = viewHolder.etExp.getText().toString();
                item.setExp(exp);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.et_exp)
        EditText etExp;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
