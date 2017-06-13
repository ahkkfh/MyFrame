package com.Lyp.bigimageview.bigImageView.loader;

import android.net.Uri;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.view.View;

import com.Lyp.bigimageview.bigImageView.view.BigImageView;

import java.io.File;

/**
 * Created by Ruoly on 2017/6/11.
 * 图片加载
 */
public interface ImageLoader {
    void loadImage(Uri uri, Callback callback);

    View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType);

    void prefetch(Uri uri);

    interface Callback {
        @UiThread
        void onCacheHit(File image);

        @WorkerThread
        void onCacheMiss(File image);

        @WorkerThread
        void onStart();

        @WorkerThread
        void onProgress(int progress);

        @WorkerThread
        void onFinish();
    }
}
