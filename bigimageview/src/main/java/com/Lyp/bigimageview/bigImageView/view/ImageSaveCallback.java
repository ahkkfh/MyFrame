package com.Lyp.bigimageview.bigImageView.view;

/**
 * Created by Ruoly on 2017/6/11.
 */

public interface ImageSaveCallback {
    void onSuccess(String uri);

    void onFail(Throwable t);
}
