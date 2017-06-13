package com.Lyp.bigimageview.bigImageView;

import android.net.Uri;

import com.Lyp.bigimageview.bigImageView.loader.ImageLoader;

/**
 * Created by Ruoly on 2017/6/11.
 */

public class BigImageViewer {
    private static volatile BigImageViewer sInstance;

    private final ImageLoader mImageLoader;

    private BigImageViewer(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public static void initialize(ImageLoader imageLoader) {
        sInstance = new BigImageViewer(imageLoader);
    }

    public static ImageLoader imageLoader() {
        if (sInstance == null) {
            throw new IllegalStateException("You must initialize BigImageViewer before use it!");
        }
        return sInstance.mImageLoader;
    }

    public static void prefetch(Uri... uris) {
        if (uris == null) {
            return;
        }

        ImageLoader imageLoader = imageLoader();
        for (Uri uri : uris) {
            imageLoader.prefetch(uri);
        }
    }
}
