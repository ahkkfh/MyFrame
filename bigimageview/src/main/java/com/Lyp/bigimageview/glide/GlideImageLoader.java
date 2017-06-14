package com.Lyp.bigimageview.glide;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.Lyp.bigimageview.R;
import com.Lyp.bigimageview.bigImageView.loader.ImageLoader;
import com.Lyp.bigimageview.bigImageView.view.BigImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

import okhttp3.OkHttpClient;


/**
 * Created by Ruoly on 2017/6/11.
 * 使用Gidle加载图片
 */
public class GlideImageLoader implements ImageLoader {
    private RequestManager requestManager;

    private GlideImageLoader(Context context, OkHttpClient okHttpClient) {
        GlideProgressSupport.init(Glide.get(context), okHttpClient);
        requestManager = Glide.with(context);
    }

    public static GlideImageLoader with(Context context) {
        return with(context, null);
    }

    public static GlideImageLoader with(Context context, OkHttpClient client) {
        return new GlideImageLoader(context, client);
    }

    @Override
    public void loadImage(Uri uri, final Callback callback) {
        requestManager.load(uri)
                .downloadOnly(new ImageDownloadTarget(uri.toString()) {
                    @Override
                    public void onDownloadStart() {
                        callback.onStart();
                    }

                    @Override
                    public void onProgress(int progress) {
                        callback.onProgress(progress);
                    }

                    @Override
                    public void onDownloadFinish() {
                        callback.onFinish();
                    }

                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        callback.onCacheHit(resource);
                    }
                });
    }

    @Override
    public View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType) {
        ImageView thumbnailView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_glide_thumbnail, parent, Boolean.FALSE);
        switch (scaleType) {
            case BigImageView.INIT_SCALE_TYPE_CENTER_CROP:
                thumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE:
                thumbnailView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            default:
                break;
        }
        requestManager.load(thumbnail)
                .into(thumbnailView);
        return thumbnailView;
    }

    @Override
    public void prefetch(Uri uri) {
        requestManager.load(uri)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        // not interested in result
                    }
                });
    }
}
