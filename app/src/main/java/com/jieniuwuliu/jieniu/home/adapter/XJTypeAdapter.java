package com.jieniuwuliu.jieniu.home.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.home.XJContentActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XJTypeAdapter extends RecyclerView.Adapter<XJTypeAdapter.ViewHolder> implements View.OnClickListener {

    private Activity context;
    private OnItemClickListener listener;
    private List<Machine.Type> list;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public XJTypeAdapter(Activity context, List<Machine.Type> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xj_type_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        Machine.Type item = list.get(i);
        viewHolder.tvType.setText(item.getType());
        if (item.isChecked()){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }
        if (XJContentActivity.state == 3){
            viewHolder.checkBox.setEnabled(false);
        }else{
            viewHolder.checkBox.setEnabled(true);
        }
        if ("".equals(item.getMoney()) && "0".equals(item.getMoney())
                &&"0.0".equals(item.getMoney()) && "0.00".equals(item.getMoney())){
            viewHolder.tvMoney.setText("未报价");
        }else{
            viewHolder.tvMoney.setText("¥  "+item.getMoney());
        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    item.setChecked(isChecked);
                    notifyItemChanged(i);
                    callBack.notifyData();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface CallBack{
        void notifyData();
    }
    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, (Integer) view.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.checkbox)
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
