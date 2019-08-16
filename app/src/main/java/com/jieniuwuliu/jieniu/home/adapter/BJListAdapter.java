package com.jieniuwuliu.jieniu.home.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.bean.BJOrder;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.XJOrder;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;
import com.jieniuwuliu.jieniu.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class BJListAdapter extends RecyclerView.Adapter<BJListAdapter.ViewHolder> implements View.OnClickListener {

    private Activity context;
    private OnItemClickListener listener;
    private List<BJOrder.DataBean> list;
    private MyLoading loading;
    public BJListAdapter(Activity context, List<BJOrder.DataBean> list) {
        this.list = list;
        this.context = context;
        loading = new MyLoading(context,R.style.CustomDialog);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.bj_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        BJOrder.DataBean item = list.get(i);
        GlideUtil.setImgUrl(context,item.getLogos(),viewHolder.img);
        viewHolder.tvTime.setText(item.getCreatedAt());
        viewHolder.tvNum.setText(item.getPcount()+"人已报价");
        viewHolder.tvName.setText(item.getCarbrand());
        List<Object> list = GsonUtil.praseJsonToList(item.getPartslist(),Machine.class);
        String info = "";
        for (int j = 0;j<list.size();j++){
            Machine machine = (Machine) list.get(j);
            if (j==0){
                info += machine.getName();
            }else{
                info += ","+machine.getName();
            }
        }
        viewHolder.tvInfo.setText(info);
        switch (item.getStype()){
            case 1:
                viewHolder.tvState.setText("询价中");
                break;
            case 2:
                viewHolder.tvState.setText("已取消");
                break;
            case 3:
                viewHolder.tvState.setText("已报价");
                break;
            case 4:
                viewHolder.tvState.setText("交易完成");
                break;
        }
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_state)
        TextView tvState;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
