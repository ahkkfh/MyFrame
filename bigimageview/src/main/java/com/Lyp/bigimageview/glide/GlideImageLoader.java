package com.Lyp.bigimageview.glide;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.Lyp.bigimageview.bigImageView.loader.ImageLoader;
import com.Lyp.bigimageview.bigImageView.view.BigImageView;
import com.bumptech.glide.RequestManager;


/**
 * Created by Ruoly on 2017/6/11.
 * 使用Gidle加载图片
 */
public class GlideImageLoader implements ImageLoader {
    private RequestManager requestManager;

    private GlideImageLoader(Context context) {

    }

    @Override
    public void loadImage(Uri uri, Callback callback) {

    }

    @Override
    public View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType) {
        return null;
    }

    @Override
    public void prefetch(Uri uri) {

    }
}
