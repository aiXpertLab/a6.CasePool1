package com.reapex.sv;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.reapex.sv.service.LocationService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.edu.heuet.littlecurl.ninegridview.preview.NineGridViewGroup;

public class L0_Application extends Application {
    public static LocationService locationService;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        // 上拉下拉控件
//        ClassicsHeader.REFRESH_HEADER_PULLING = ""; //" 下拉可以刷新";
//        ClassicsHeader.REFRESH_HEADER_RELEASE = ""; // "释放立即刷新";
//        ClassicsHeader.REFRESH_HEADER_REFRESHING = ""; // "正在刷新...";
//        ClassicsHeader.REFRESH_HEADER_RELEASE = ""; // "释放立即刷新";
//        ClassicsHeader.REFRESH_HEADER_FINISH = ""; // "刷新完成";
//        ClassicsHeader.REFRESH_HEADER_FAILED = ""; // "刷新失败";
//        ClassicsHeader.REFRESH_HEADER_UPDATE = ""; // "上次更新 M-d HH:mm";
//
//        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在加载"; // "正在加载..."
//        ClassicsFooter.REFRESH_FOOTER_FINISH = ""; // "加载完成"
//        ClassicsFooter.REFRESH_FOOTER_NOTHING = ""; // "全部加载完成"

        NineGridViewGroup.setImageLoader(new GlideImageLoader());
    }

    /**
     * Glide 加载图片
     */
    private class GlideImageLoader implements NineGridViewGroup.ImageLoader {
        GlideImageLoader() {
        }

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_default_color)   // 图片未加载时的占位图或背景色
                    .error(R.drawable.ic_default_color)         // 图片加载失败时显示的图或背景色
                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // 开启本地缓存
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
