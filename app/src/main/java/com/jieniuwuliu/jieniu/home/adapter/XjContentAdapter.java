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
import android.widget.Toast;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XjInfo;
import com.jieniuwuliu.jieniu.home.XJContentActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class XjContentAdapter extends RecyclerView.Adapter<XjContentAdapter.ViewHolder> implements View.OnClickListener {

    private Activity context;
    private OnItemClickListener listener;
    private List<XjInfo.DataBean> list;
    private CallBack callBack;
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
                callBack.sureInfo(item);
            }
        });
    }
    public interface CallBack{
        void sureInfo(XjInfo.DataBean item);
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
