package cn.mark.frame.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.FragmentStatusBarBinding;
import cn.mark.utils.Constant;
import cn.mark.utils.StatusBarUtil;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-07 17:16
 */
public class StatusBarActivty extends BaseActivity {
    private FragmentStatusBarBinding mBinding;
    private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_status_bar);
        initView();
    }

    protected void initView() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle("Statusbar");
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initClick();
        initAlpha();
    }

    private void initAlpha() {
        mBinding.sbChangeAlpha.setMax(255);//设置透明度最大值
        mBinding.sbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                if (mBinding.chbTranslucent.isChecked()) {
                    StatusBarUtil.setTranslucentForDrawerLayout(StatusBarActivty.this, mBinding.drawerLayout, mAlpha);
                } else {
                    StatusBarUtil.setColorForDrawerLayout(StatusBarActivty.this, mBinding.drawerLayout, getResources().getColor(R.color.color_02D1B1), mAlpha);
                }
                mBinding.tvStatusAlpha.setText(String.valueOf(mAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.sbChangeAlpha.setProgress(StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    private void initClick() {
        RxView.clicks(mBinding.btnSetColor).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivty.this, ColorStatusBarActivity.class));
            }
        });

        RxView.clicks(mBinding.btnSetTransparent).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(StatusBarActivty.this, ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, true);
                startActivity(intent);
            }
        });

        RxView.clicks(mBinding.btnSetTranslucent).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(StatusBarActivty.this, ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, false);
                startActivity(intent);
            }
        });
        RxView.clicks(mBinding.btnSetForImageView).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivty.this, ImageViewActivity.class));
            }
        });
        RxView.clicks(mBinding.btnUseInFragment).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivty.this, UseInFragmentActivity.class));
            }
        });
        RxView.clicks(mBinding.btnSetColorForSwipeBack).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivty.this, SwipeBackActivity.class));
            }
        });
    }

    @Override
    protected void setStatusBar() {
//        StatusBarUtil.setTranslucentForImageView(this, findViewById(R.id.view_need_offset));//设置半透明
        StatusBarUtil.setTransparentForImageView(this, findViewById(R.id.view_need_offset));//设置全透明
    }
}
