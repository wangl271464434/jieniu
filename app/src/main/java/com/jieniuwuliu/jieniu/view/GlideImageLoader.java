package com.jieniuwuliu.jieniu.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.Util.GlideUtil;
import com.jieniuwuliu.jieniu.bean.ImgBanner;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImgBanner.DataBean data = (ImgBanner.DataBean) path;
        //Glide 加载图片简单用法
//        GlideUtil.setImgUrl(context,path,imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideUtil.setImgUrl(context,data.getUrl(),R.mipmap.loading,imageView);
//        Glide.with(context).load().into(imageView);
    }
}
