/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package longimage.bigImageView.loader;

import android.net.Uri;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.view.View;

import java.io.File;

import longimage.bigImageView.view.BigImageView;

/**
 * @author mark.luo
 * @Date 2017-06-06 14:20
 * 图片加载监听
 */
public interface ImageLoader {

    void loadImage(Uri uri, Callback callback);

    //设置缩略图
    View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType);

    //预览
    void prefetch(Uri uri);

    interface Callback {
        @UiThread
        void onCacheHit(File image);

        @WorkerThread
        void onCacheMiss(File image);

        @WorkerThread
        void onStart();

        @WorkerThread
        void onProgress(int progress);

        @WorkerThread
        void onFinish();
    }
}
