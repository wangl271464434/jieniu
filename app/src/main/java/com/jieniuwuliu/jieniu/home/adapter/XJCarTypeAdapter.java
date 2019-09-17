package com.jieniuwuliu.jieniu.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.VinCar;
import com.jieniuwuliu.jieniu.bean.XJCarType;
import com.jieniuwuliu.jieniu.home.AddCarDateActivity;
import com.jieniuwuliu.jieniu.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 按字母排序的适配器
 * */
public class XJCarTypeAdapter extends RecyclerView.Adapter<XJCarTypeAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<XJCarType.Data> mData;
    private Context mContext;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public XJCarTypeAdapter(Context context, List<XJCarType.Data> data) {
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
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        holder.recyclerView.setLayoutManager(manager);
        XJCarItemAdapter adapter = new XJCarItemAdapter(mContext,mData.get(position).getModels());
        holder.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                VinCar.Data data = new VinCar.Data();
                data.setBrand(mData.get(position).getBrand());
                data.setLogos(mData.get(position).getImgurl());
                data.setCartype(mData.get(position).getBrand()+" "+mData.get(position).getModels().get(i));
                Intent intent = new Intent();
                intent.setClass(mContext, AddCarDateActivity.class);
                intent.putExtra("data",data);
                mContext.startActivity(intent);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.show(position);
            }
        });
        if (mData.get(position).isShow()){
            holder.recyclerView.setVisibility(View.VISIBLE);
        }else{
            holder.recyclerView.setVisibility(View.GONE);
        }
    }
    public interface CallBack{
        void show(int position);
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
    public void updateList(List<XJCarType.Data> list){
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
