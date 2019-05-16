package com.jieniuwuliu.jieniu.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStorePicAdapter extends RecyclerView.Adapter<AddStorePicAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<String> list;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public AddStorePicAdapter(Context context, List<String> list) {
        this.context = context;
        setData(list);
    }

    public void setData(List<String> data) {
        list = new ArrayList<>();
        if (data.size()<6){
            list.addAll(data);
            list.add("");
        }else{
            list.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_pic_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        Log.i("imgUrl",list.get(i));
        if (!list.get(i).equals("")){
            viewHolder.imgDelete.setVisibility(View.VISIBLE);
            viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.deletePic(i);
                }
            });
            if (list.get(i).contains("http://")){
                GlideUtil.setImgUrl(context,list.get(i),viewHolder.img);
            }else {
                GlideUtil.setLocalImgUrl(context,list.get(i),viewHolder.img);
            }
        }else{
            viewHolder.imgDelete.setVisibility(View.GONE);
            viewHolder.img.setImageResource(R.mipmap.pic_shop_upload);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.addPic(i);
                }
            });
        }
    }
    public interface CallBack{
        void addPic(int position);
        void deletePic(int position);
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
