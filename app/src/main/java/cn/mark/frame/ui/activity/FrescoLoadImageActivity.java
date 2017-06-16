package cn.mark.frame.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import longimage.bigImageView.view.BigImageView;
import longimage.progresspie.ProgressPieIndicator;

/***
 * @author marks.luo
 * @Description: TODO(Fresco 加载长图)
 * @date:2017-06-15 22:01
 */
public class FrescoLoadImageActivity extends BaseActivity {
    private Button mButton;
    private BigImageView mBigImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_load);
        mButton = (Button) findViewById(R.id.fresco_loader);
        mBigImageView = (BigImageView) findViewById(R.id.big_image_view);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBigImageView.setProgressIndicator(new ProgressPieIndicator());
                mBigImageView.showImage(
                        Uri.parse("http://img3.imgtn.bdimg.com/it/u=3284462501,858280594&fm=21&gp=0.jpg"),
                        Uri.parse("http://a.hiphotos.baidu.com/zhidao/pic/item/adaf2edda3cc7cd90df3f2953f01213fb90e91a4.jpg")
                );
            }
        });

    }
}
