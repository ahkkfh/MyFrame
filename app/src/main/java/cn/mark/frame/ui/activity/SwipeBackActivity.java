package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Random;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivitySwipeBackBinding;
import cn.mark.utils.StatusBarUtil;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-06 11:51
 */
public class SwipeBackActivity extends BaseActivity {
    private ActivitySwipeBackBinding mBinding;
    private int mColor = Color.GRAY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_swipe_back);
        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mBinding.toolbar.setBackgroundColor(mColor);
        mBinding.btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                mColor = 0xff000000 | random.nextInt(0xffffff);
                mBinding.toolbar.setBackgroundColor(mColor);
                StatusBarUtil.setColorForSwipeBack(SwipeBackActivity.this, mColor, 38);
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorForSwipeBack(this, mColor, 38);
    }
}
