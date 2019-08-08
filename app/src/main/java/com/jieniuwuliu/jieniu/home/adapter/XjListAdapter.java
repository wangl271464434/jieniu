package com.jieniuwuliu.jieniu.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.Util.GsonUtil;
import com.jieniuwuliu.jieniu.Util.HttpUtil;
import com.jieniuwuliu.jieniu.Util.MyToast;
import com.jieniuwuliu.jieniu.Util.SPUtil;
import com.jieniuwuliu.jieniu.Util.SimpleCallBack;
import com.jieniuwuliu.jieniu.bean.Constant;
import com.jieniuwuliu.jieniu.bean.Machine;
import com.jieniuwuliu.jieniu.bean.OrderInfo;
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

public class XjListAdapter extends RecyclerView.Adapter<XjListAdapter.ViewHolder> implements View.OnClickListener {

    private Activity context;
    private OnItemClickListener listener;
    private List<XJOrder.DataBean> list;
    private MyLoading loading;
    public XjListAdapter(Activity context, List<XJOrder.DataBean> list) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.xj_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        XJOrder.DataBean item = list.get(i);
        GlideUtil.setImgUrl(context,item.getLogos(),viewHolder.img);
        viewHolder.tvTime.setText(item.getCreatedat());
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
                viewHolder.tvCancel.setVisibility(View.GONE);
                break;
        }
        viewHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(item,i);
            }
        });
    }

    private void cancel(XJOrder.DataBean item, int i) {
        loading.show();
        String token = (String) SPUtil.get(context, Constant.TOKEN,Constant.TOKEN,"");
        Call<ResponseBody> call = HttpUtil.getInstance().getApi(token).cancelBJOrder(item.getId());
        call.enqueue(new SimpleCallBack<ResponseBody>(context) {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                loading.dismiss();
                item.setStype(2);
                notifyItemChanged(i);
            }

            @Override
            public void onFail(int errorCode, Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    String s = response.errorBody().string();
                    Log.w("result",s);
                    JSONObject object = new JSONObject(s);
                    MyToast.show(context, object.getString("msg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetError(String s) {
                loading.dismiss();
                MyToast.show(context,s);
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
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
