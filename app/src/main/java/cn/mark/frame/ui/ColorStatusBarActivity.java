package cn.mark.frame.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityColorStatusBarBinding;
import cn.mark.utils.Constant;
import cn.mark.utils.StatusBarUtil;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-06 11:51
 */
public class ColorStatusBarActivity extends BaseActivity {
    private ActivityColorStatusBarBinding mBinding;
    private int alpha;
    private int color;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_color_status_bar);

        //设置toolbar
        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RxView.clicks(mBinding.btnChangeColor).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Random random = new Random();
                color = 0xff000000 | random.nextInt(0xffffff);
                mBinding.toolbar.setBackgroundColor(color);
                StatusBarUtil.setColor(ColorStatusBarActivity.this, color, alpha);
            }
        });
        mBinding.sbChangeAlpha.setMax(255);
        mBinding.sbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alpha = progress;
                StatusBarUtil.setColor(ColorStatusBarActivity.this, color, alpha);
                mBinding.tvStatusAlpha.setText(String.valueOf(alpha));
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

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        color = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColor(this, color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
