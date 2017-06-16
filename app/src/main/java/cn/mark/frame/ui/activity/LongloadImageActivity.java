package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityFrescoLoadBinding;
import cn.mark.utils.Constant;
import longimage.progresspie.ProgressPieIndicator;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO(使用BigImageView加载长图)
 * @date:2017-06-16 09:47
 */
public class LongloadImageActivity extends BaseActivity {
    private ActivityFrescoLoadBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_fresco_load);
        mBinding.frescoLoader.setVisibility(View.GONE);
        mBinding.bigImageView.setProgressIndicator(new ProgressPieIndicator());
        mBinding.bigImageView.showImage(Uri.parse("http://ww1.sinaimg.cn/mw690/005Fj2RDgw1f9mvl4pivvj30c82ougw3.jpg"));
        RxView.clicks(mBinding.bigImageView).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {//图片点击事件
                finish();
            }
        });

    }
}
