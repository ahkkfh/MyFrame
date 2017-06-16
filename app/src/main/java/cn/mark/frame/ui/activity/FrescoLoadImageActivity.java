package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityFrescoLoadBinding;
import longimage.progresspie.ProgressPieIndicator;

/***
 * @author marks.luo
 * @Description: TODO(Fresco 加载长图)
 * @date:2017-06-15 22:01
 */
public class FrescoLoadImageActivity extends BaseActivity {
    private ActivityFrescoLoadBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_fresco_load);

        mBinding.bigImageView.setProgressIndicator(new ProgressPieIndicator());
        mBinding.bigImageView.showImage(
                Uri.parse("http://img3.imgtn.bdimg.com/it/u=3284462501,858280594&fm=21&gp=0.jpg"),
                Uri.parse("http://a.hiphotos.baidu.com/zhidao/pic/item/adaf2edda3cc7cd90df3f2953f01213fb90e91a4.jpg")
        );
        mBinding.bigImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
