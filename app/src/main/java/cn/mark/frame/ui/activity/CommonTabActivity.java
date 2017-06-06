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
import java.util.Random;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityCommonTabBinding;
import cn.mark.frame.entity.TabEntity;
import cn.mark.frame.ui.fragment.SimpleCardFragment;
import cn.mark.utils.DPToPx;
import cn.mark.utils.widget.tablayout.listener.CustomTabEntity;
import cn.mark.utils.widget.tablayout.listener.OnTabSelectListener;
import cn.mark.utils.widget.tablayout.utils.UnreadMsgUtils;
import cn.mark.utils.widget.tablayout.view.MsgView;

/***
 * @author marks.luo
 * @Description: TODO(公共tab的Activity)
 * @date:2017-04-25 16:01
 */
public class CommonTabActivity extends BaseActivity {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();

    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ActivityCommonTabBinding mBinding;
    private Random mRandom = new Random();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_common_tab);
        initView();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mBinding.commonTabBar);
        getSupportActionBar().setTitle("CommonTab");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + title));
            mFragments2.add(SimpleCardFragment.getInstance("Switch Fragment " + title));
        }

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mBinding.vp2.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mBinding.tl1.setTabData(mTabEntities);
        setTL2Data();

        mBinding.tl3.setTabData(mTabEntities, this, R.id.fl_change, mFragments2);
        mBinding.tl4.setTabData(mTabEntities);
        mBinding.tl5.setTabData(mTabEntities);
        mBinding.tl6.setTabData(mTabEntities);
        mBinding.tl7.setTabData(mTabEntities);
        mBinding.tl8.setTabData(mTabEntities);

        mBinding.tl3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mBinding.tl1.setCurrentTab(position);
                mBinding.tl2.setCurrentTab(position);
                mBinding.tl4.setCurrentTab(position);
                mBinding.tl5.setCurrentTab(position);
                mBinding.tl6.setCurrentTab(position);
                mBinding.tl7.setCurrentTab(position);
                mBinding.tl8.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });


        mBinding.tl8.setCurrentTab(2);
        mBinding.tl3.setCurrentTab(2);


        //显示未读小红点
        mBinding.tl1.showDot(2);
        mBinding.tl3.showDot(1);
        mBinding.tl4.showDot(1);

        //显示两位数
        mBinding.tl2.showMsg(0, 55);
        mBinding.tl2.setMsgMargin(0, -5, 5);

        //显示三位数
        mBinding.tl2.showMsg(0, 55);
        mBinding.tl2.setMsgMargin(0, -5, 5);

        //设置未读小红点
        mBinding.tl2.showDot(2);
        MsgView rtv_2_2 = mBinding.tl2.getMsgView(2);
        if (rtv_2_2 != null) {
            UnreadMsgUtils.setSize(rtv_2_2, DPToPx.dp2px(this, 7.5f));
        }

        //设置未读消息背景
        mBinding.tl2.showMsg(3, 5);
        mBinding.tl2.setMsgMargin(3, 0, 5);
        MsgView rtv_2_3 = mBinding.tl2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }

    private void setTL2Data() {
        mBinding.tl2.setTabData(mTabEntities);
        mBinding.tl2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mBinding.vp2.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mBinding.tl2.showMsg(0, mRandom.nextInt(100) + 1);
                }
            }
        });
        mBinding.vp2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.tl2.setCurrentTab(position);
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
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
