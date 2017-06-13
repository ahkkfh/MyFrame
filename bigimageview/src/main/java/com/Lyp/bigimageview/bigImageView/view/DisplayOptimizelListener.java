package com.Lyp.bigimageview.bigImageView.view;

import android.graphics.PointF;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by Ruoly on 2017/6/11.
 */

public class DisplayOptimizelListener implements SubsamplingScaleImageView.OnImageEventListener {
    private static final int LONG_IMAGE_SIZE_RATIO = 2;

    private final SubsamplingScaleImageView mImageView;

    private int mInitScaleType;

    public DisplayOptimizelListener(SubsamplingScaleImageView mImageView) {
        this.mImageView = mImageView;
    }

    @Override
    public void onReady() {
        float result = 0.5F;
        int imageWidth = mImageView.getSWidth();
        int imageHeight = mImageView.getSHeight();
        int viewWidth = mImageView.getWidth();
        int viewHeight = mImageView.getHeight();

        boolean hasZeroValue = false;
        if (imageWidth == 0 || imageHeight == 0 || viewHeight == 0 || viewWidth == 0) {
            result = 0.5F;
            hasZeroValue = true;
        }

        if (!hasZeroValue) {
            result = imageWidth <= imageHeight ? (float) viewWidth / imageWidth : (float) viewHeight / imageHeight;
        }

        if (!hasZeroValue && (float) imageHeight / imageWidth > LONG_IMAGE_SIZE_RATIO) {
            mImageView.animateScaleAndCenter(result, new PointF(imageWidth / 2, 0))
                    .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                    .start();
        }

        // 对结果进行放大，防止计算结果跟双击的放大的结果过于相近
        if (Math.abs(result - 0.1) < 0.2F) {
            result += 0.2f;
        }

        if (mInitScaleType == BigImageView.INIT_SCALE_TYPE_AUTO) {
            float maxScale = Math.max((float) viewWidth / imageWidth, (float) viewHeight / imageHeight);
            if (maxScale > 1) {
                // image is smaller than screen, it should be zoomed out to its origin size
                mImageView.setMinScale(1);
                // and it should be zoomed in to fill the screen
                float defaultMaxScale = mImageView.getMaxScale();
                mImageView.setMaxScale(Math.max(defaultMaxScale, maxScale * 1.2F));
            } else {
                // image is bigger than screen, it should be zoomed out to fit the screen
                float minScale = Math.min((float) viewWidth / imageWidth, (float) viewHeight / imageHeight);
                mImageView.setMinScale(minScale);
                // but no need to set max scale
            }
            mImageView.setScaleAndCenter(maxScale, new PointF(imageWidth / 2, imageHeight / 2));
        }
        mImageView.setDoubleTapZoomScale(result);
    }

    @Override
    public void onImageLoaded() {

    }

    @Override
    public void onPreviewLoadError(Exception e) {

    }

    @Override
    public void onImageLoadError(Exception e) {

    }

    @Override
    public void onTileLoadError(Exception e) {

    }

    @Override
    public void onPreviewReleased() {

    }

    public void setInitScaleType(int initScaleType) {
        this.mInitScaleType = initScaleType;
    }
}
