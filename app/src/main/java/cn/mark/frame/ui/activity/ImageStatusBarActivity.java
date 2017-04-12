package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityImageStatusBarBinding;
import cn.mark.utils.Constant;
import cn.mark.utils.StatusBarUtil;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-06 11:51
 */
public class ImageStatusBarActivity extends BaseActivity {
    public static final String EXTRA_IS_TRANSPARENT = "is_transparent";
    private ActivityImageStatusBarBinding mBinding;
    private boolean isBgChanged;
    private boolean isTransparent;
    private int mAlpha;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_status_bar);

        RxView.clicks(mBinding.btnChangeBackground).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                isBgChanged = !isBgChanged;
                if (isBgChanged) {
                    mBinding.rootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_girl));
                } else {
                    mBinding.rootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_monkey));
                }
            }
        });

        if (!isTransparent) {
            mBinding.sbChangeAlpha.setVisibility(View.VISIBLE);
            setSeekBar();
        } else {
            mBinding.sbChangeAlpha.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setStatusBar() {
        if (isTransparent) {
            StatusBarUtil.setTransparent(this);
        } else {
            StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        }
    }

    private void setSeekBar() {
        mBinding.sbChangeAlpha.setMax(255);
        mBinding.sbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                StatusBarUtil.setTranslucent(ImageStatusBarActivity.this, mAlpha);
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
}
