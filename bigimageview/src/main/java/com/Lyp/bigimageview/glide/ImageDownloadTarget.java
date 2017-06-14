package com.Lyp.bigimageview.glide;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-06-14 11:55
 */
public abstract class ImageDownloadTarget extends SimpleTarget<File> implements GlideProgressSupport.ProgressListener {
    private String mUrl;

    protected ImageDownloadTarget(String url) {
        mUrl = url;
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        super.onLoadCleared(placeholder);
        GlideProgressSupport.forget(mUrl);
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        super.onLoadStarted(placeholder);
        GlideProgressSupport.expect(mUrl, this);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        GlideProgressSupport.forget(mUrl);
    }
}
