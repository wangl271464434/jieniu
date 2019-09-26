package com.jieniuwuliu.jieniu.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.SortModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 按字母排序的适配器
 * */
public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<SortModel> mData;
    private Context mContext;
    private CallBack callBack;
    public SortAdapter(Context context, List<SortModel> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public SortAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sort_car_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SortAdapter.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getZimu());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }
        if (mData.get(position).isIscheck()){
            holder.checkBox.setImageResource(R.mipmap.ic_fapiao_checked);
        }else{
            holder.checkBox.setImageResource(R.mipmap.ic_fapiao_unchecked);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).isIscheck()){
                    mData.get(position).setIscheck(false);
                    callBack.isChecked(position,false);
                }else{
                    mData.get(position).setIscheck(true);
                    callBack.isChecked(position,true);
                }
                notifyItemChanged(position);
            }
        });
        GlideUtil.setImgUrl(mContext,mData.get(position).getTupian(),holder.img);
        holder.tvName.setText(this.mData.get(position).getName());
    }
    public interface CallBack{
        void isChecked(int position,boolean isChecked);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //**************************************************************

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView tvName;
        @BindView(R.id.tag)
        TextView tvTag;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.checkbox)
        ImageView checkBox;
        @BindView(R.id.layout)
        LinearLayout layout;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(List<SortModel> list){
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getZimu().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getZimu();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
