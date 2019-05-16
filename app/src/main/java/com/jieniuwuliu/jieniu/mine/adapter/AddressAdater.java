package com.jieniuwuliu.jieniu.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Address;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressAdater extends RecyclerView.Adapter<AddressAdater.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<Address> list;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public AddressAdater(Context context, List<Address> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        Address item = list.get(i);
        if (item.isDefaultX()){
            viewHolder.checkbox.setChecked(true);
        }else {
            viewHolder.checkbox.setChecked(false);
        }
        viewHolder.tvAddress.setText(item.getAddress());
        viewHolder.tvInfo.setText(item.getName()+"  "+item.getPhone());
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.delete(i);
            }
        });
        if (item.isDefaultX()){
            viewHolder.tvDefault.setChecked(true);
            viewHolder.tvDefault.setText("取消默认地址");
        }else{
            viewHolder.tvDefault.setChecked(false);
            viewHolder.tvDefault.setText("设为默认地址");
        }
        viewHolder.tvDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    callBack.setDefault(i,isChecked);
                }
            }
        });

    }
    public interface CallBack{
        void delete(int position);
        void setDefault(int position,boolean isChecked);
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
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.delete)
        TextView delete;
        @BindView(R.id.tv_default)
        CheckBox tvDefault;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
