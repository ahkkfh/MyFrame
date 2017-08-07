package longimage.photodrawable;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;

import longimage.R;
import longimage.bigImageView.loader.ImageLoader;
import longimage.subsampling.ImageSource;
import longimage.subsampling.ImageViewState;
import longimage.subsampling.SubsamplingScaleImageView;

/***
 * @author marks.luo
 * @Description: (带有loading层的simpledraweee组合控件)
 * @date:2017-08-04 15:47
 *
 */
public class LoadingDraweeview extends FrameLayout {
    private PhotoDraweeView mDraweeView;
    private SubsamplingScaleImageView mScaleImageView;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private Context mContext;
    private OnClickListener mOnClickListener;
    private FrescoImageLoader mImageLoader = new FrescoImageLoader();
    private int screenWidth;
    private int screenheight;

    public LoadingDraweeview(@NonNull Context context, String url) {
        super(context);
        mContext = context;
        getDisplayMetrics();
        if (url.startsWith("http")) {
            downloadImage(Uri.parse(url), true);
        } else {//显示本地图片
            File file = new File(url);
            checkImageFile(file, Uri.fromFile(file));
        }
    }

    private void downloadImage(final Uri uri, boolean needShowLoadingView) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        if (mScaleImageView == null && mDraweeView == null) {
            mImageView = new ImageView(mContext);
            mImageView.setImageResource(R.drawable.feed_item_picture_bg);
            addView(mImageView, layoutParams);
        }
        if (needShowLoadingView) {
            mProgressBar = new ProgressBar(mContext);
            addView(mProgressBar, layoutParams);
        }
        mImageLoader.prefetch(uri, new ImageLoader.Callback() {
            @Override
            public void onCacheHit(File image) {

            }

            @Override
            public void onCacheMiss(File image) {

            }

            @Override
            public void onSuccess(final File image) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        removeView(mImageView);
                        removeView(mProgressBar);
                        checkImageFile(image, uri);
                    }
                });
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onFail(Throwable e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        removeView(mProgressBar);
                    }
                });
            }

            @Override
            public void onFinish() {

            }
        });
    }

    Handler handler = new Handler(Looper.getMainLooper());

    //根据图片信息决定使用哪个控件显示图片
    private void checkImageFile(File file, Uri uri) {
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//确保图片不加载到内存
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            String type = options.outMimeType;
            Log.i("lbxx", "是否为长图===" + isLongImage(options.outWidth, options.outHeight));
            if (isLongImage(options.outWidth, options.outHeight) && !"image/gif".equals(type)) {
                showLongImage(file.getAbsolutePath());
            } else {
                showImage(uri, false);
            }
        }
    }

    private void showImage(Uri uri, final boolean forceFillWidth) {
        if (mDraweeView == null) {
            mDraweeView = new PhotoDraweeView(mContext);
            addView(mDraweeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mDraweeView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(view);
                    }
                }
            });
        }
        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setUri(uri)
                .setAutoPlayAnimations(true)//自动播放gif
                .setOldController(mDraweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null) {
                            return;
                        }
                        if (forceFillWidth && imageInfo.getWidth() < imageInfo.getHeight()) {
                            mDraweeView.fillWidth(imageInfo.getWidth(), imageInfo.getHeight());
                        } else {
                            mDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                            mDraweeView.setGestureEnable(true);
                        }
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                    }
                });
        mDraweeView.setController(controllerBuilder.build());
    }

    private void showLongImage(String absolutePath) {
        if (mScaleImageView == null) {
            mScaleImageView = new SubsamplingScaleImageView(mContext);
            addView(mScaleImageView, new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mScaleImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(v);
                    }
                }
            });
        }
        mScaleImageView.setImage(ImageSource.uri(absolutePath), new ImageViewState(0.0F, new PointF(0, 0), 0));
        mScaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
    }

    private boolean isLongImage(int width, int height) {
        Log.i("lbxx", "b=" + width + "==" + height + "===" + screenWidth + "====" + screenheight);
        if ((width > screenWidth || height > screenheight)) {
            if ((width / (float) height) > 2 || (height / (float) width) > 2) {
                return true;
            }
        }
        return false;
    }

    private void getDisplayMetrics() {
        DisplayMetrics metric = mContext.getResources().getDisplayMetrics();
        screenWidth = metric.widthPixels < metric.heightPixels ? metric.widthPixels : metric.heightPixels;  // 屏幕宽度（像素）
        screenheight = metric.heightPixels > metric.widthPixels ? metric.heightPixels : metric.widthPixels;  // 屏幕高度（像素）
    }

}
