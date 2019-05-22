package com.jieniuwuliu.jieniu.Util;

import android.content.Context;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jieniuwuliu.jieniu.R;
import com.jieniuwuliu.jieniu.view.GlideCircleTransform;
import com.jieniuwuliu.jieniu.view.GlideRoundTransform;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Createtime: 2018/6/25
 * Author: wanglei
 * Description: 图片加载工具
 */
public class GlideUtil {
    /**
     * @date: 2018/6/25
     * @author: wangleiA
     * @params: context, path, imageView
     * 不会缓存
     */
    public static void setImgUrl(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(imageView);
    }

    /**
     * @author YangJing
     * @date 2018/7/11
     * @des:: 加载图片，带有加载错误的参数
     */
    public static void setImgUrl(Context context, String path, int resId, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(resId)
                .dontAnimate()
                .centerCrop()
                .error(resId).into(imageView);
    }

    /**
     * @date: 2018/7/10
     * @author: wanglei
     * @params: 加载本地图片
     */
    public static void setLocalImgUrl(Context context, String path, ImageView imageView) {
        Glide.with(context).load(new File(path))
                .centerCrop()
                .into(imageView);
    }
    public static void setLocal0UserImgUrl(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(new File(path))
                .transform(new GlideCircleTransform(context, 2, context.getResources().getColor(R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
    /**
     * 加载用户头像
     */
    public static void setUserImgUrl(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder( R.mipmap.user) //加载成功前显示的图片
                .transform(new GlideCircleTransform(context, 2, context.getResources().getColor(R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
    /**
     * 圆角图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void setRoundImg(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.mipmap.logo) //占位符 也就是加载中的图片，可放个gif
                .error(R.mipmap.logo) //失败图片
                .skipMemoryCache(true)//跳过缓存
                .transform(new CenterCrop(context),new GlideRoundTransform(context,6))//圆角图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }
    /**
     * 圆形加载
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadCircleImage(Context mContext, int path,
                                       ImageView imageview) {
        Glide.with(mContext).load(path).centerCrop().placeholder(R.mipmap.user)
                .transform(new GlideCircleTransform(mContext, 2, mContext.getResources().getColor(R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
    }
    public static void setVideoImg(Context context,String path,ImageView imageView){
        Glide.with(context).load(path)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(imageView));
    }

    public static void setVideoImgUrl(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(imageView);
    }


    /*    *//**
     * @date: 2018/7/10
     * @author: wanglei
     * @params: 加载用户
     *//*
    public static void setUserImgUrl(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .dontAnimate()
                .placeholder( R.mipmap.user) //加载成功前显示的图片
                .transform(new GlideCircleTransform(context, 2, context.getResources().getColor(R.color.white)))
                .fallback( R.mipmap.user) //url为空的时候,显示的图片
                .error(R.mipmap.user)//错误图片
                .centerCrop()
                .into(imageView);
    }
    *//**
     * @date: 2018/7/10
     * @author: wanglei
     * @params: 加载本地资源图片
     *//*
    public static void setLocalImgUrl(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId).asBitmap().into(imageView);
    }

    *//**
     * @param context
     * @param url
     * @param resId
     * @param imageView
     *//*
    public static void setRoundImg(Context context, String url, int resId, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
//                .placeholder(resId) //占位符 也就是加载中的图片，可放个gif
                .error(resId) //失败图片
                .skipMemoryCache(true)//跳过缓存
                .transform(new CenterCrop(context),new GlideRoundTransform(context,6))//圆角图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    *//**
     * 圆形加载
     *
     * @param mContext
     * @param path
     * @param imageview
     *//*
    public static void LoadCircleImage(Context mContext, int path,
                                       ImageView imageview) {
        Glide.with(mContext).load(path).centerCrop().placeholder(R.mipmap.beauty)
                .transform(new GlideCircleTransform(mContext, 2, mContext.getResources().getColor(R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
    }*/
}
