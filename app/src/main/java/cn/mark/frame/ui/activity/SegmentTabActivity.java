package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivitySegmentTabBinding;
import cn.mark.frame.ui.fragment.SimpleCardFragment;
import cn.mark.utils.widget.tablayout.listener.OnTabSelectListener;
import cn.mark.utils.widget.tablayout.view.MsgView;

/***
 * @author marks.luo
 * @Description: TODO(细分Tab)
 * @date:2017-04-25 16:02
 */
public class SegmentTabActivity extends BaseActivity {
    private ActivitySegmentTabBinding mBinding;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();

    private String[] mTitles = {"首页", "消息"};
    private String[] mTitles_2 = {"首页", "消息", "联系人"};
    private String[] mTitles_3 = {"首页", "消息", "联系人", "更多"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_segment_tab);
        initView();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mBinding.segmentTabBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("segmentTab");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        for (String title : mTitles_3) {
            mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + title));
        }

        for (String title : mTitles_2) {
            mFragments2.add(SimpleCardFragment.getInstance("Switch Fragment " + title));
        }

        mBinding.tl1.setTabData(mTitles);
        mBinding.tl2.setTabData(mTitles);

        setTl3Data();
        mBinding.tl4.setTabData(mTitles_2, this, R.id.fl_change, mFragments2);
        mBinding.tl5.setTabData(mTitles_3);

        //显示未读红点
        mBinding.tl1.showDot(2);
        mBinding.tl2.showDot(2);
        mBinding.tl3.showDot(1);
        mBinding.tl4.showDot(1);

        //设置未读消息红点
        mBinding.tl3.showDot(2);
        MsgView rtv_3_2 = mBinding.tl3.getMsgView(2);
        if (rtv_3_2 != null) {
            rtv_3_2.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }

    private void setTl3Data() {
        mBinding.vp2.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mBinding.tl3.setTabData(mTitles_3);
        mBinding.tl3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mBinding.vp2.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mBinding.vp2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.tl3.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.vp2.setCurrentItem(1);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles_3[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
