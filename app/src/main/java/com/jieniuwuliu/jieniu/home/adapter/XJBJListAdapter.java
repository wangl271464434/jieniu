package com.jieniuwuliu.jieniu.home.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XJBJListAdapter extends RecyclerView.Adapter<XJBJListAdapter.ViewHolder>{
    private Activity context;
    private  List<Machine> list;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public XJBJListAdapter(Activity context, List<Machine> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xj_bj_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        Machine item = list.get(i);
        viewHolder.tvName.setText((i+1)+"."+item.getName());
        if(item.getList()!=null){
            LinearLayoutManager manager = new LinearLayoutManager(context);
            viewHolder.recyclerView.setLayoutManager(manager);
            XJTypeAdapter adapter = new XJTypeAdapter(context,item.getList());
            viewHolder.recyclerView.setAdapter(adapter);
            adapter.setCallBack(new XJTypeAdapter.CallBack() {
                @Override
                public void notifyData(boolean isChecked) {
                    if (isChecked){
                        Constant.LIST.add(list.get(i));
                    }else{
                        Constant.LIST.remove(list.get(i));
                    }
                    callBack.current(i);
                }
            });
        }
        if (!item.getExp().equals("")){
            viewHolder.tvExp.setVisibility(View.VISIBLE);
            viewHolder.tvExp.setText("报价备注："+item.getExp());
        }else{
            viewHolder.tvExp.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface CallBack{
        void current(int currentPosition);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.tv_exp)
        TextView tvExp;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
