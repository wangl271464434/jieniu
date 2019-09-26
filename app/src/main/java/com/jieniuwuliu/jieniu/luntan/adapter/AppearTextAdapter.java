package com.jieniuwuliu.jieniu.luntan.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppearTextAdapter extends RecyclerView.Adapter<AppearTextAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener listener;
    private List<String> list;
    private boolean isFlag = false;//是否弹出点赞和评论
    private String type;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    public AppearTextAdapter(Context context,List<String> list,String type) {
        this.context = context;
        this.type = type;
        setData(list);
    }
    public void setData(List<String> data) {
        list = new ArrayList<>();
        if (type.equals("video")){
            list.addAll(data);
        }else{
            if (data.size()<9){
                list.addAll(data);
                list.add("");
            }else{
                list.addAll(data);
            }
        }
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.appeartext_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setTag(i);
        if (type.equals("video")){
            viewHolder.imgPlay.setVisibility(View.VISIBLE);
        }else {
            viewHolder.imgPlay.setVisibility(View.GONE);
        }
        if (list.get(i).equals("")){
            viewHolder.imgDel.setVisibility(View.GONE);
            viewHolder.img.setImageResource(R.mipmap.pic_shop_upload);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.addPic(i);
                }
            });
        }else{
            viewHolder.imgDel.setVisibility(View.VISIBLE);
            GlideUtil.setLocalImgUrl(context,list.get(i),viewHolder.img);
            viewHolder.imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.deletePic(i);
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
        @BindView(R.id.img_play)
        ImageView imgPlay;
        @BindView(R.id.img_del)
        ImageView imgDel;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}