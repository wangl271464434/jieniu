package com.jieniuwuliu.jieniu.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);
    }
}
