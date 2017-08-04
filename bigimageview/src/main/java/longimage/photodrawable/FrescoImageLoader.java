package longimage.photodrawable;

import android.net.Uri;
import android.view.View;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.disk.FileCache;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;

import longimage.bigImageView.loader.ImageLoader;
import longimage.bigImageView.view.BigImageView;
import longimage.fresco.ImageDownloadSubscriber;

/***
 * @author marks.luo
 * @Description: ()
 * @date:2017-08-04 16:09
 *
 */
public class FrescoImageLoader implements ImageLoader {
    private final DefaultExecutorSupplier mExecutorSupplier;
    private DataSource dataSource;

    public FrescoImageLoader() {
        mExecutorSupplier = new DefaultExecutorSupplier(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void loadImage(Uri uri, Callback callback) {

    }

    @Override
    public void loadImage(Uri uri, String filePath, final Callback callback) {
        ImageRequest request = ImageRequest.fromUri(uri);
        File localCache = getCacheFile(uri.toString());
        if (localCache != null) {
            callback.onSuccess(localCache);
        } else {
            callback.onProgress(0); // show 0 progress immediately

            ImagePipeline pipeline = Fresco.getImagePipeline();
            dataSource = pipeline.fetchEncodedImage(request, true);
            dataSource.subscribe(new ImageDownloadSubscriber(filePath) {
                @Override
                public void onProgress(int progress) {
                    callback.onProgress(progress);
                }

                @Override
                protected void onSuccess(File image) {
                    callback.onSuccess(image);
                }

                @Override
                protected void onFail(Throwable t) {
                    callback.onFail(t);
                    t.printStackTrace();
                }
            }, mExecutorSupplier.forBackgroundTasks());
        }
    }

    @Override
    public View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType) {
        return null;
    }

    @Override
    public void prefetch(Uri uri) {
        
    }


    //预加载网络图片到本地,图片并已缓存到内存
    @Override
    public void prefetch(final Uri uri, final Callback callback) {
        final ImagePipeline pipeline = Fresco.getImagePipeline();
        dataSource = pipeline.fetchDecodedImage(ImageRequest.fromUri(uri), true); //其他加载方法获取的都是未解码的，也就是还未保存文件到本地
        dataSource.subscribe(new BaseDataSubscriber() {
            @Override
            protected void onNewResultImpl(DataSource dataSource) {
                File file = getCacheFile(uri.toString());
                if (file != null) {
                    callback.onSuccess(file);
                } else {
                    callback.onFail(null);
                }
                dataSource.close();
            }

            @Override
            protected void onFailureImpl(DataSource dataSource) {
                callback.onFail(dataSource.getFailureCause());
                dataSource.close();
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }

    /**
     * 获取本地缓存文件，如果没有则返回空
     *
     * @param path
     * @return
     */
    public File getCacheFile(String path) {
        File cacheFile = null;
        FileCache mainFileCache = ImagePipelineFactory
                .getInstance()
                .getMainFileCache();
        SimpleCacheKey simpleCacheKey = new SimpleCacheKey(path);
        if (mainFileCache.hasKey(simpleCacheKey)) {
            FileBinaryResource resource = (FileBinaryResource) mainFileCache.getResource(simpleCacheKey);
            if (resource != null) {
                cacheFile = resource.getFile();
            }
        }
        return cacheFile;
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
