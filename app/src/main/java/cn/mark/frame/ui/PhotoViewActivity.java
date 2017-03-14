package cn.mark.frame.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.PhotoViewBinding;
import cn.mark.utils.ToastUtil;
import retrofit2.http.Url;

/***
 * @author marks.luo
 * @Description: TODO(长图加载类)
 * @date:2017-03-08 14:15
 */
public class PhotoViewActivity extends BaseActivity {
    private PhotoViewBinding photoViewBinding;
   private  String url="http://cache.attach.yuanobao.com/image/2016/10/24/332d6f3e63784695a50b782a38234bb7/da0f06f8358a4c95921c00acfd675b60.jpg";//图片地址

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoViewBinding= DataBindingUtil.setContentView(this,R.layout.photo_view);
        initView();
//        initFrescoData();
//        initGlideData();
        getFrescoData();
    }
    private void initView() {
        photoViewBinding.longView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        photoViewBinding.longView.setMinScale(1.0F);
        photoViewBinding.longView.setMaxScale(10.0F);
    }

    private void getFrescoData() {
        
    }

    private void initGlideData() {
        Glide.with(this).load(url).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                photoViewBinding.longView.setImage(ImageSource.uri(Uri.fromFile(resource)),new ImageViewState(1.0F,new PointF(0,0),0));
            }
        });
    }

    private void initFrescoData() {
        Log.i("lbxx","url=="+url);
        if(url==null)return;
        //首先判断是否有缓存
        ImageRequest request= ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        ImagePipeline pipeline= Fresco.getImagePipeline();
        DataSource dataSource=pipeline.fetchDecodedImage(request,null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(@javax.annotation.Nullable Bitmap cacheBitmap) {
                ImageSource  imageSource=ImageSource.cachedBitmap(cacheBitmap);
//                ImageSource  imageSource=ImageSource.bitmap(cacheBitmap);
                Log.i("lbxx","获取imageSource成功");
                ImageViewState state= new ImageViewState(1.0F, new PointF(0, 0), 0);
                Log.i("lbxx","获取state成功");
                photoViewBinding.longView.setImage(imageSource, state);
                Log.i("lbxx","获取bitmap成功");
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                Log.i("lbxx","获取bitmap失败");
            }
        },CallerThreadExecutor.getInstance());
    }
}
