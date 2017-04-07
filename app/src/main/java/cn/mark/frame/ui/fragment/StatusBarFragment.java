package cn.mark.frame.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;
import cn.mark.frame.databinding.FragmentStatusBarBinding;
import cn.mark.frame.ui.ColorStatusBarActivity;
import cn.mark.frame.ui.MainActivity;
import cn.mark.frame.ui.ImageStatusBarActivity;
import cn.mark.frame.ui.ImageViewActivity;
import cn.mark.frame.ui.SwipeBackActivity;
import cn.mark.frame.ui.UseInFragmentActivity;
import cn.mark.utils.Constant;
import cn.mark.utils.StatusBarUtil;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-07 17:16
 */
public class StatusBarFragment extends BaseFragment {
    private FragmentStatusBarBinding mBinding;
    private int mStatusBarColor;
    private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mBinding) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_status_bar, container, false);
        }
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        ((MainActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), mBinding.drawerLayout, mBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
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
                    StatusBarUtil.setTranslucentForDrawerLayout(getActivity(), mBinding.drawerLayout, mAlpha);
                } else {
                    StatusBarUtil.setColorForDrawerLayout(getActivity(), mBinding.drawerLayout, mStatusBarColor, mAlpha);
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
                startActivity(new Intent(getActivity(), ColorStatusBarActivity.class));
            }
        });

        RxView.clicks(mBinding.btnSetTransparent).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, true);
                startActivity(intent);
            }
        });

        RxView.clicks(mBinding.btnSetTranslucent).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, false);
                startActivity(intent);
            }
        });
        RxView.clicks(mBinding.btnSetForImageView).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), ImageViewActivity.class));
            }
        });
        RxView.clicks(mBinding.btnUseInFragment).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), UseInFragmentActivity.class));
            }
        });
        RxView.clicks(mBinding.btnSetColorForSwipeBack).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), SwipeBackActivity.class));
            }
        });
    }
}
