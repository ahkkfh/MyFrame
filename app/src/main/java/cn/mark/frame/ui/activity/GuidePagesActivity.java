package cn.mark.frame.ui.activity;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.databinding.ActivityGuidePagesBinding;
import cn.mark.frame.ui.MainActivity;
import cn.mark.frame.ui.adapter.SectionsPagerAdapter;
import cn.mark.utils.Constant;
import rx.functions.Action1;

public class GuidePagesActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ActivityGuidePagesBinding mBinding;
    private int bgColors[];
    private int currentPosition;
    private ImageView[] indicators;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences =  getSharedPreferences("Guide_page", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("is_loading", true)) {
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_guide_pages);
            initView();
        } else {
            navigateToMainActivity();
            finish();
        }
    }

    private void initView() {
        bgColors = new int[]{ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.color_02D1B1),
                ContextCompat.getColor(this, R.color.color_ed6d70)};
        indicators = new ImageView[]{mBinding.imageViewIndicator0, mBinding.imageViewIndicator1, mBinding.imageViewIndicator2};
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mBinding.container.setAdapter(mSectionsPagerAdapter);
        mBinding.container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (int) new ArgbEvaluator().evaluate(positionOffset, bgColors[position], bgColors[position == 2 ? position : position + 1]);
                mBinding.container.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updateIndicators(position);
                mBinding.container.setBackgroundColor(bgColors[position]);
                mBinding.imageButtonPre.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mBinding.imageButtonNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mBinding.buttonFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        RxView.clicks(mBinding.buttonFinish).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("is_loading", false);
                editor.apply();
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(GuidePagesActivity.this, MainActivity.class));
        finish();
    }


    /***
     * 更改点的状态和颜色
     * @param position 下标
     */
    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(i == position ? R.drawable.onboarding_indicator_selected : R.drawable.onboarding_indicator_unselected);
        }
    }

}
