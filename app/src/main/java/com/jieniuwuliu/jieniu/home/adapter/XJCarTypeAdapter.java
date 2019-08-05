package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.SortModel;
import com.jieniuwuliu.jieniu.bean.XJCarType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 按字母排序的适配器
 * */
public class XJCarTypeAdapter extends RecyclerView.Adapter<XJCarTypeAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<XJCarType> mData;
    private Context mContext;
    public XJCarTypeAdapter(Context context, List<XJCarType> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
    }
    @Override
    public XJCarTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sort_xj_car_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final XJCarTypeAdapter.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getBrands());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }
        GlideUtil.setImgUrl(mContext,mData.get(position).getImgurl(),holder.img);
        holder.tvName.setText(this.mData.get(position).getBrand());
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
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(List<XJCarType> list){
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
        return mData.get(position).getBrands().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getBrands();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
