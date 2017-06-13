package com.Lyp.bigimageview.bigImageView.view;

import android.Manifest;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.MediaController;

import com.Lyp.bigimageview.R;
import com.Lyp.bigimageview.bigImageView.BigImageViewer;
import com.Lyp.bigimageview.bigImageView.indicator.ProgressIndicator;
import com.Lyp.bigimageview.bigImageView.loader.ImageLoader;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Created by Ruoly on 2017/6/11.
 */

public class BigImageView extends FrameLayout implements ImageLoader.Callback {
    public static final int INIT_SCALE_TYPE_CENTER_INSIDE = 1;
    public static final int INIT_SCALE_TYPE_CENTER_CROP = 2;
    public static final int INIT_SCALE_TYPE_AUTO = 3;

    private final SubsamplingScaleImageView mImageView;

    private final ImageLoader mImageLoader;
    private final List<File> mTempImages;

    private View mProgressIndicatorView;
    private View mThumbnailView;

    private ImageSaveCallback mImageSaveCallback;
    private File mCurrentImageFile;
    private Uri mThumbnail;

    private ProgressIndicator mProgressIndicator;
    private DisplayOptimizelListener mDisplayOptimizeListener;
    private int mInitScaleType;
    private boolean mOptimizeDisplay;

    private final ProgressNotifyRunnable mNotifyRunnable = new ProgressNotifyRunnable() {
        @Override
        public void run() {
            if (mProgressIndicator != null) {
                mProgressIndicator.onProgress(0);
                notified();
            }
        }
    };

    public BigImageView(@NonNull Context context) {
        this(context, null);
    }

    public BigImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BigImageView, defStyleAttr, 0);
        mInitScaleType = array.getInteger(R.styleable.BigImageView_initScaleType, INIT_SCALE_TYPE_CENTER_INSIDE);
        mOptimizeDisplay = array.getBoolean(R.styleable.BigImageView_optimizeDisplay, true);
        array.recycle();

        mImageView = new SubsamplingScaleImageView(context, attrs);
        addView(mImageView);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        mImageView.setMinimumDpi(160);

        setOptimizeDisplay(mOptimizeDisplay);
        setInitScaleType(mInitScaleType);

        mImageLoader = BigImageViewer.imageLoader();

        mTempImages = new ArrayList<>();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mImageView.setOnClickListener(l);
    }

    private void setInitScaleType(int initScaleType) {
        this.mInitScaleType = initScaleType;
        switch (initScaleType) {
            case INIT_SCALE_TYPE_CENTER_CROP:
                mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                break;
            case INIT_SCALE_TYPE_AUTO:
                mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                break;
            case INIT_SCALE_TYPE_CENTER_INSIDE:
            default:
                mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                break;
        }
        if (mDisplayOptimizeListener != null) {
            mDisplayOptimizeListener.setInitScaleType(initScaleType);
        }
    }

    private void setOptimizeDisplay(boolean optimizeDisplay) {
        mOptimizeDisplay = optimizeDisplay;
        if (mOptimizeDisplay) {
            mDisplayOptimizeListener = new DisplayOptimizelListener(mImageView);
            mImageView.setOnImageEventListener(mDisplayOptimizeListener);
        } else {
            mDisplayOptimizeListener = null;
            mImageView.setOnImageEventListener(null);
        }
    }

    public void setImageSaveCallback(ImageSaveCallback imageSaveCallback) {
        mImageSaveCallback = imageSaveCallback;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        mProgressIndicator = progressIndicator;
    }

    public String currentImageFile() {
        return mCurrentImageFile == null ? "" : mCurrentImageFile.getAbsolutePath();
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void saveImageIntoGallery() {
        if (mCurrentImageFile == null) {
            if (mImageSaveCallback != null) {
                mImageSaveCallback.onFail(new IllegalStateException("image not downloaded yet "));
            }

            return;
        }
        try {
            String result = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    mCurrentImageFile.getAbsolutePath(), mCurrentImageFile.getName(), "");
            if (mImageSaveCallback != null) {
                if (!TextUtils.isEmpty(result)) {
                    mImageSaveCallback.onSuccess(result);
                } else {
                    mImageSaveCallback.onFail(new RuntimeException("saveImageIntoGallery fail"));
                }
            }
        } catch (FileNotFoundException e) {
            if (mImageSaveCallback != null) {
                mImageSaveCallback.onFail(e);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0, size = mTempImages.size(); i < size; i++) {
            mTempImages.get(i).delete();
        }
        mTempImages.clear();
    }

    public void showIamge(Uri uri) {
        showImage(Uri.EMPTY, uri);
    }

    private void showImage(Uri empty, Uri uri) {
        Log.d("lbxx", "show Image with thumbnail" + empty + "," + uri);
        mThumbnail = empty;
        mImageLoader.loadImage(uri, this);
    }

    public SubsamplingScaleImageView getSSIV() {
        return mImageView;
    }

    @UiThread
    @Override
    public void onCacheHit(File image) {
        Log.d("BigImageView", "onCacheHit " + image);

        mCurrentImageFile = image;
        doShowImage(image);
    }

    @WorkerThread
    @Override
    public void onCacheMiss(final File image) {
        Log.d("BigImageView", "onCacheMiss " + image);

        mCurrentImageFile = image;
        mTempImages.add(image);
        post(new Runnable() {
            @Override
            public void run() {
                doShowImage(image);
            }
        });

    }

    @WorkerThread
    @Override
    public void onStart() {
        post(new Runnable() {
            @Override
            public void run() {
                // why show thumbnail in onStart? because we may not need download it from internet
                if (mThumbnail != Uri.EMPTY) {
                    mThumbnailView = mImageLoader.showThumbnail(BigImageView.this, mThumbnail,
                            mInitScaleType);
                    addView(mThumbnailView);
                }

                if (mProgressIndicator != null) {
                    mProgressIndicatorView = mProgressIndicator.getView(BigImageView.this);
                    addView(mProgressIndicatorView);
                    mProgressIndicator.onStart();
                }
            }
        });
    }

    @WorkerThread
    @Override
    public void onProgress(int progress) {
        if (mProgressIndicator != null && mNotifyRunnable.update(progress)) {
            post(mNotifyRunnable);
        }
    }

    @WorkerThread
    @Override
    public void onFinish() {
        post(new Runnable() {
            @Override
            public void run() {
                doOnFinish();
            }
        });
    }

    @UiThread
    private void doOnFinish() {
        if (mOptimizeDisplay) {
            AnimationSet set = new AnimationSet(true);
            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(500);
            animation.setFillAfter(true);
            set.addAnimation(animation);
            if (mThumbnailView != null) {
                mThumbnailView.setAnimation(set);
            }

            if (mProgressIndicator != null) {
                mProgressIndicator.onFinish();
            }

            if (mProgressIndicatorView != null) {
                mProgressIndicatorView.setAnimation(set);
            }

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mThumbnailView != null) {
                        mThumbnailView.setVisibility(GONE);
                    }
                    if (mProgressIndicatorView != null) {
                        mProgressIndicatorView.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            if (mProgressIndicator != null) {
                mProgressIndicator.onFinish();
            }
            if (mThumbnailView != null) {
                mThumbnailView.setVisibility(View.GONE);
            }
            if (mProgressIndicatorView != null) {
                mProgressIndicatorView.setVisibility(View.GONE);
            }
        }
    }

    @UiThread
    private void doShowImage(File image) {
        mImageView.setImage(ImageSource.uri(Uri.fromFile(image)));
    }

}
