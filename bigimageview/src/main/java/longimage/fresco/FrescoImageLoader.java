package longimage.fresco;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.FileCache;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;

import longimage.R;
import longimage.bigImageView.loader.ImageLoader;
import longimage.bigImageView.view.BigImageView;

/**
 * Created by Ruoly on 2017/6/11.
 * 使用Fresco加载图片
 */
public class FrescoImageLoader implements ImageLoader {
    private final Context mAppContext;
    private final DefaultExecutorSupplier mExecutorSupplier;

    private FrescoImageLoader(Context context) {
        mAppContext = context;

        mExecutorSupplier = new DefaultExecutorSupplier(Runtime.getRuntime().availableProcessors());
    }

    public static FrescoImageLoader with(Context context) {
        return with(context, null, null);
    }

    private static FrescoImageLoader with(Context appContext, ImagePipelineConfig imagePipelineConfig) {
        return with(appContext, imagePipelineConfig, null);
    }

    private static FrescoImageLoader with(Context appContext, ImagePipelineConfig imagePipelineConfig, DraweeConfig draweeConfig) {
        Fresco.initialize(appContext, imagePipelineConfig, draweeConfig);
        return new FrescoImageLoader(appContext);
    }

    @Override
    public void loadImage(Uri uri, final Callback callback) {
        ImageRequest request = ImageRequest.fromUri(uri);

        File localCache = getCacheFile(request);
        if (localCache.exists()) {
            callback.onCacheHit(localCache);
        } else {
            callback.onStart(); // ensure `onStart` is called before `onProgress` and `onFinish`
            callback.onProgress(0); // show 0 progress immediately

            ImagePipeline pipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<PooledByteBuffer>> source = pipeline.fetchEncodedImage(request, true);
            source.subscribe(new ImageDownloadSubscriber(mAppContext) {
                @Override
                public void onProgress(int progress) {
                    callback.onProgress(progress);
                }

                @Override
                protected void onSuccess(File image) {
                    callback.onFinish();
                    callback.onCacheMiss(image);
                }

                @Override
                protected void onFail(Throwable t) {
                    t.printStackTrace();
                }
            }, mExecutorSupplier.forBackgroundTasks());
        }
    }

    @Override
    public void loadImage(Uri uri, String filePath, Callback callback) {

    }

    @Override
    public View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType) {
        SimpleDraweeView draweeView = (SimpleDraweeView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_fresco_thumbnail, parent, false);
        DraweeController controller = (DraweeController) Fresco.newDraweeControllerBuilder()
                .setUri(thumbnail)
                .build();
        switch (scaleType) {
            case BigImageView.INIT_SCALE_TYPE_CENTER_CROP:
                draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);

                break;
            case BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE:
                draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
                break;
        }
        draweeView.setController(controller);
        return draweeView;
    }

    @Override
    public void prefetch(Uri uri) {
        ImagePipeline pipeline = Fresco.getImagePipeline();
        pipeline.prefetchToDiskCache(ImageRequest.fromUri(uri), false);// we don't need context, but avoid null
    }

    @Override
    public void prefetch(Uri uri, Callback callback) {

    }

    private File getCacheFile(ImageRequest request) {
        FileCache mainFileCache = ImagePipelineFactory.getInstance().getMainFileCache();
        CacheKey cachekey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request, false); // we don't need context, but avoid null
        File cacheFile = request.getSourceFile();
        if (mainFileCache.hasKey(cachekey) && mainFileCache.getResource(cachekey) != null) {
            cacheFile = ((FileBinaryResource) mainFileCache.getResource(cachekey)).getFile();
        }
        return cacheFile;
    }


}
