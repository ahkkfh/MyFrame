package com.Lyp.bigimageview.progresspie;


import android.view.LayoutInflater;
import android.view.View;

import com.Lyp.bigimageview.R;
import com.Lyp.bigimageview.bigImageView.BigImageViewer;
import com.Lyp.bigimageview.bigImageView.indicator.ProgressIndicator;
import com.Lyp.bigimageview.bigImageView.view.BigImageView;
import com.filippudak.ProgressPieView.ProgressPieView;
import java.util.Locale;

/**
 * Created by Ruoly on 2017/6/11.
 */

public class ProgressPieIndicator implements ProgressIndicator {
    private ProgressPieView mProgressPieView;

    @Override
    public View getView(BigImageView parent) {
        mProgressPieView = (ProgressPieView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_progress_pie_indicator, parent, false);
        return mProgressPieView;
    }

    @Override
    public void onStart() {
        // not interested
    }

    @Override
    public void onProgress(int progress) {
        if (progress < 0 || progress > 100 || mProgressPieView == null) {
            return;
        }
        mProgressPieView.setProgress(progress);
        mProgressPieView.setText(String.format(Locale.getDefault(), "%d%%", progress));
    }

    @Override
    public void onFinish() {
        // not interested
    }
}