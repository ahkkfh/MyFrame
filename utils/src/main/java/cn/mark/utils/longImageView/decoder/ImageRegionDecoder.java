package cn.mark.utils.longImageView.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;

/***
 * @author marks.luo
 * @Description: TODO(用于图像解码类的接口)
 * @date:2017-03-07 17:25
 */
public interface ImageRegionDecoder {
    /**
     * Initialise the decoder. When possible, initial setup work once in this method. This method
     * must return the dimensions of the image. The URI can be in one of the following formats:
     * (初始化解码器。 如果可能，初始设置在此方法中工作一次。 此方法必须返回图像的尺寸。 URI可以采用以下格式之一：)
     * File: file:///scard/picture.jpg
     * Asset: file:///android_asset/picture.png
     * Resource: android.resource://com.example.app/drawable/picture
     * @param context Application context. A reference may be held, but must be cleared on recycle.
     * @param uri URI of the image.
     * @return Dimensions of the image.
     * @throws Exception if initialisation fails.
     */
    Point init(Context context, Uri uri) throws Exception;

    /**
     * Decode a region of the image with the given sample size. This method is called off the UI thread so it can safely
     * load the image on the current thread. It is called from an {@link android.os.AsyncTask} running in a single
     * threaded executor, and while a synchronization lock is held on this object, so will never be called concurrently
     * even if the decoder implementation supports it.
     * (使用给定的样本大小解码图像的区域。 这个方法被称为UI线程，所以它可以安全地加载当前线程上的图像。
     * 它从在一个单线程执行器中运行的{@link android.os.AsyncTask}中调用，
     * 并且同步锁定保持在该对象上，因此即使解码器实现支持它也不会被并发调用。)
     * @param sRect Source image rectangle to decode.
     * @param sampleSize Sample size.
     * @return The decoded region. It is safe to return null if decoding fails.
     */
    Bitmap decodeRegion(Rect sRect, int sampleSize);

    /**
     * Status check. Should return false before initialisation and after recycle.
     * 状态检查。 应在初始化之前和循环后返回false。
     * @return true if the decoder is ready to be used.
     */
    boolean isReady();

    /**
     * This method will be called when the decoder is no longer required. It should clean up any resources still in use.
     * 当不再需要解码器时，将调用此方法。 它应该清理仍在使用的任何资源。
     */
    void recycle();
}