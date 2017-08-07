package longimage.bigImageView.view;

import android.graphics.PointF;

import longimage.subsampling.SubsamplingScaleImageView;

/***
 * @author marks.luo
 * @Description: TODO(显示优化监听)
 * @date:2017-06-14 11:55
 */
public class DisplayOptimizeListener implements SubsamplingScaleImageView.OnImageEventListener {
    private static final int LONG_IMAGE_SIZE_RATIO = 2;//宽高比

    private final SubsamplingScaleImageView mImageView;//长图加载view

    private int mInitScaleType;//加载状态

    public DisplayOptimizeListener(SubsamplingScaleImageView imageView) {
        mImageView = imageView;
    }

    @Override
    public void onReady() {
        float result = 0.5f;
        int imageWidth = mImageView.getSWidth();
        int imageHeight = mImageView.getSHeight();
        int viewWidth = mImageView.getWidth();
        int viewHeight = mImageView.getHeight();

        boolean hasZeroValue = false;
        if (imageWidth == 0 || imageHeight == 0 || viewWidth == 0 || viewHeight == 0) {
            result = 0.5f;
            hasZeroValue = true;
        }

        if (!hasZeroValue) {
            if (imageWidth <= imageHeight) {
                result = (float) viewWidth / imageWidth;
            } else {
                result = (float) viewHeight / imageHeight;
            }
        }

        if (!hasZeroValue && (float) imageHeight / imageWidth > LONG_IMAGE_SIZE_RATIO) {
            // scale at top
            mImageView.animateScaleAndCenter(result, new PointF(imageWidth / 2, 0))
                    .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                    .start();
        }

        // `对结果进行放大裁定，防止计算结果跟双击放大结果过于相近`
        if (Math.abs(result - 0.1) < 0.2f) {
            result += 0.2f;
        }

        if (mInitScaleType == BigImageView.INIT_SCALE_TYPE_AUTO) {
            float maxScale = Math.max((float) viewWidth / imageWidth, (float) viewHeight / imageHeight);
            if (maxScale > 1) {
                // 图像小于屏幕，应缩小到原尺寸
                mImageView.setMinScale(1);
                // 且应放大到以填充屏幕
                float defaultMaxScale = mImageView.getMaxScale();
                mImageView.setMaxScale(Math.max(defaultMaxScale, maxScale * 1.2F));
            } else {
                //图像大于屏幕应该缩小到以适应屏幕
                float minScale = Math.min((float) viewWidth / imageWidth, (float) viewHeight / imageHeight);
                mImageView.setMinScale(minScale);
                //不需要设置最大刻度
            }
            // 尺寸适合屏幕和中心
            mImageView.setScaleAndCenter(maxScale, new PointF(imageWidth / 2, imageHeight / 2));
        }
        mImageView.setDoubleTapZoomScale(result);//设置双击设置
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
        mInitScaleType = initScaleType;
    }
}
