package com.jieniuwuliu.jieniu.home.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GsonUtil;
import com.jieniuwuliu.jieniu.util.MyToast;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XjInfo;
import com.jieniuwuliu.jieniu.home.XJContentActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XjContentAdapter extends RecyclerView.Adapter<XjContentAdapter.ViewHolder> implements View.OnClickListener{

    private Activity context;
    private OnItemClickListener listener;
    private List<XjInfo.DataBean> list;
    private CallBack callBack;
    private int current = -1;
    private String[] permissions = new String[]{Manifest.permission.CALL_PHONE,
            Manifest.permission.PROCESS_OUTGOING_CALLS};
    public XjContentAdapter(Activity context, List<XjInfo.DataBean> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xj_content_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        XjInfo.DataBean item = list.get(i);
        switch (item.getState()){
            case 0:
                viewHolder.tvState.setText("已报价");
                break;
            case 1:
                viewHolder.tvState.setText("交易完成");
                break;
            case 2:
                viewHolder.tvState.setText("已报价");
                break;
        }
        if (!item.getLabel().equals("")){
            viewHolder.tabRv.setVisibility(View.VISIBLE);
            String[] array = item.getLabel().split(",");
            LinearLayoutManager tabManager = new LinearLayoutManager(context);
            tabManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.tabRv.setLayoutManager(tabManager);
            XJTabAdapter xjTabAdapter = new XJTabAdapter(context,array);
            viewHolder.tabRv.setAdapter(xjTabAdapter);
        }else{
            viewHolder.tabRv.setVisibility(View.GONE);
        }
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvAddress.setText(item.getAddress());
        viewHolder.tvTime.setText(item.getUpdatedAt());
        List<Object> objects = GsonUtil.praseJsonToList(item.getPartslist(),Machine.class);
        List<Machine> machines = new ArrayList<>();
        for (Object object:objects) {
            Machine machine = (Machine) object;
            machines.add(machine);
        }
        LinearLayoutManager manager = new LinearLayoutManager(context);
        viewHolder.recyclerView.setLayoutManager(manager);
        XJBJListAdapter adapter = new XJBJListAdapter(context,machines);
        viewHolder.recyclerView.setAdapter(adapter);
        adapter.setCallBack(new XJBJListAdapter.CallBack() {
            @Override
            public void notifyList(List<Machine> machines) {
                current = i;
                item.setPartslist(GsonUtil.listToJson(machines));
            }
        });
        viewHolder.tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23){
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(context,permissions,100);
                        return;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + item.getPhone());
                intent.setData(data);
                context.startActivity(intent);
            }
        });
        if (XJContentActivity.state == 3){
            viewHolder.tvBuy.setVisibility(View.GONE);
        }else{
            viewHolder.tvBuy.setVisibility(View.VISIBLE);
        }
        viewHolder.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current != -1){
                    if (current == i){
                        callBack.sureInfo(i);
                        current = -1;
                    }else{
                        MyToast.show(context,"请您选择想要购买的配件");
                    }
                }else{
                    MyToast.show(context,"请您选择想要购买的配件");
                }
            }
        });
    }
    public interface CallBack{
        void sureInfo(int position);
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
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tab_rv)
        RecyclerView tabRv;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.tv_call)
        TextView tvCall;
        @BindView(R.id.tv_buy)
        TextView tvBuy;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
