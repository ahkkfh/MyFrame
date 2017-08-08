package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;

/***
 * @author marks.luo
 * @Description: ()
 * @date:2017-08-08 14:36
 *
 */
public class ImageSelectActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_image_select);
    }
}
