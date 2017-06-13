package com.Lyp.bigimageview.bigImageView.indicator;

import android.view.View;
import com.Lyp.bigimageview.bigImageView.BigImageViewer;
import com.Lyp.bigimageview.bigImageView.view.BigImageView;

/**
 * @author mark.ruo
 * @date 2017-06-11 14:45
 * 进度条监听方法
 */
public interface ProgressIndicator {
    /**
     * DO NOT add indicator view into parent! Only called once per load.
     */
    View getView(BigImageView parent) ;

    void onStart();

    /**
     * @param progress in range of {@code [0, 100]}
     */
    void onProgress(int progress);

    void onFinish();
}
