package com.jieniuwuliu.jieniu.luntan.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicAdapter extends PagerAdapter {
    private Activity context;
    private List<String> list;

    public PicAdapter(Activity context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.img_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        GlideUtil.setImgUrl(context,list.get(position),viewHolder.img);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        container.addView(view);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.img)
        ImageView img;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
