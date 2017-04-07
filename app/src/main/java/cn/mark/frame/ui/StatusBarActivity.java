package cn.mark.frame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.utils.Constant;
import cn.mark.utils.StatusBarUtil;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-06 11:42
 */
public class StatusBarActivity extends BaseActivity {
    private int mStatusBarColor;
    private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;
    private DrawerLayout drawerLayout;
    private LinearLayout contentLayout;
    private Toolbar toolbar;
    private Checkable mCheckable;
    private TextView mTvStatusAlpha;
    private SeekBar mSbChangeAlpha;
    private Button btnSetColor;
    private Button btnSetTransparent;
    private Button btnSetTranslucent;
    private Button btnSetforImageView;
    private Button btnUseInFragment;
    private Button btnSetColorForSwipeBack;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar);
        init();
    }

    private void init() {
        initView();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        initClick();
        initAlpha();
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        contentLayout = (LinearLayout) findViewById(R.id.main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCheckable = (Checkable) findViewById(R.id.chb_translucent);
        mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
        mSbChangeAlpha = (SeekBar) findViewById(R.id.sb_change_alpha);
        btnSetColor = (Button) findViewById(R.id.btn_set_color);
        btnSetTransparent = (Button) findViewById(R.id.btn_set_transparent);
        btnSetTranslucent = (Button) findViewById(R.id.btn_set_translucent);
        btnSetforImageView = (Button) findViewById(R.id.btn_set_for_image_view);
        btnUseInFragment = (Button) findViewById(R.id.btn_use_in_fragment);
        btnSetColorForSwipeBack = (Button) findViewById(R.id.btn_set_color_for_swipe_back);
    }

    private void initAlpha() {
        mSbChangeAlpha.setMax(255);//设置透明度最大值
        mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                if (mCheckable.isChecked()) {
                    StatusBarUtil.setTranslucentForDrawerLayout(StatusBarActivity.this, drawerLayout, mAlpha);
                } else {
                    StatusBarUtil.setColorForDrawerLayout(StatusBarActivity.this, drawerLayout, mStatusBarColor, mAlpha);
                }
                mTvStatusAlpha.setText(String.valueOf(mAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbChangeAlpha.setProgress(StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    private void initClick() {
        RxView.clicks(btnSetColor).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivity.this, ColorStatusBarActivity.class));
            }
        });

        RxView.clicks(btnSetTransparent).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(StatusBarActivity.this, ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, true);
                startActivity(intent);
            }
        });

        RxView.clicks(btnSetTranslucent).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(StatusBarActivity.this, ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, false);
                startActivity(intent);
            }
        });
        RxView.clicks(btnSetforImageView).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivity.this, ImageViewActivity.class));
            }
        });
        RxView.clicks(btnUseInFragment).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivity.this, UseInFragmentActivity.class));
            }
        });
        RxView.clicks(btnSetColorForSwipeBack).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(StatusBarActivity.this, SwipeBackActivity.class));
            }
        });
//        mCheckable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCheckable.isChecked()) {
//                    contentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_monkey));
//                    StatusBarUtil.setTranslucentForDrawerLayout(StatusBarActivity.this, drawerLayout, mAlpha);
//                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                } else {
//                    contentLayout.setBackgroundDrawable(null);
//                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    StatusBarUtil.setColorForDrawerLayout(StatusBarActivity.this, drawerLayout,
//                            getResources().getColor(R.color.colorPrimary), mAlpha);
//                }
//            }
//        });
    }

//    @Override
//    protected void setStatusBar() {
//        mStatusBarColor = getResources().getColor(R.color.colorPrimary);
//        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout, mStatusBarColor, mAlpha);
//    }
}
