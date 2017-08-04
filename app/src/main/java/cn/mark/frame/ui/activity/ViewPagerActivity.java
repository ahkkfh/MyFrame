package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityViewpagerBinding;

/***
 * @author marks.luo
 * @Description: (ViewPagerActivity 显示长图)
 * @date:2017-08-04 17:48
 *
 */
public class ViewPagerActivity extends BaseActivity {
    private ActivityViewpagerBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_viewpager);
        initView();
    }

    @Override
    protected void initView() {

    }
}
