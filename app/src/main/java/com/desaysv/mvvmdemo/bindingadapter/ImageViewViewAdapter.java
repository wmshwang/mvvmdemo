package com.desaysv.mvvmdemo.bindingadapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.desaysv.mvvmdemo.BaseApplication;
import com.desaysv.mvvmdemo.R;

/**
 * 自定义View
 *
 * @author llw
 */
public class ImageViewViewAdapter {

    private ImageViewViewAdapter() {
        throw new IllegalStateException("Utility view class");
    }

    private static final RequestOptions OPTIONS = new RequestOptions()
            .placeholder(R.drawable.wallpaper_bg)//图片加载出来前，显示的图片
            .fallback(R.drawable.wallpaper_bg) //url为空的时候,显示的图片
            .error(R.mipmap.ic_loading_failed);//图片加载失败后，显示的图片

    private static final RequestOptions OPTIONS_LOCAL = new RequestOptions()
            .placeholder(R.drawable.logo)//图片加载出来前，显示的图片
            .fallback(R.drawable.logo) //url为空的时候,显示的图片
            .error(R.mipmap.ic_loading_failed)//图片加载失败后，显示的图片
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);

    /**
     * 必应壁纸  因为拿到的url不完整，因此需要做一次地址拼接
     *
     * @param imageView 图片视图
     * @param url       网络url
     */
    @BindingAdapter(value = {"android:biyingUrl"}, requireAll = false)
    public static void setBiyingUrl(ImageView imageView, String url) {
        String assembleUrl = "http://cn.bing.com" + url;
        LogUtils.d(assembleUrl);
        Glide.with(BaseApplication.getInstance()).load(assembleUrl).into(imageView);
    }

    /**
     * 普通网络地址图片
     *
     * @param imageView 图片视图
     * @param url       网络url
     */
    @BindingAdapter(value = {"android:networkUrl"}, requireAll = false)
    public static void setNetworkUrl(ImageView imageView, String url) {
        Glide.with(BaseApplication.getInstance()).load(url).apply(OPTIONS).into(imageView);
    }

    /**
     * 本地地址图片
     *
     * @param imageView 图片视图
     * @param url       本地url
     */
    @BindingAdapter(value = {"android:localUrl"}, requireAll = false)
    public static void setLocalUrl(ImageView imageView, String url) {
        Glide.with(BaseApplication.getInstance()).load(url).apply(OPTIONS_LOCAL).into(imageView);
    }
}
