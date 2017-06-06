package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivitySlidingTabBinding;
import cn.mark.frame.ui.fragment.SimpleCardFragment;
import cn.mark.utils.widget.tablayout.listener.OnTabSelectListener;
import cn.mark.utils.widget.tablayout.view.MsgView;

/***
 * @author marks.luo
 * @Description: TODO(滑动Tab的Activity)
 * @date:2017-04-25 16:00
 */
public class SlidingTabActivity extends BaseActivity implements OnTabSelectListener {
    private ActivitySlidingTabBinding mBinding;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    };
    private MyPagerAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sliding_tab);
        initView();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mBinding.slidingTabBar);
        getSupportActionBar().setTitle("SlidingTab");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mBinding.vp.setAdapter(mAdapter);

        mBinding.tl1.setViewPager(mBinding.vp);
        mBinding.tl2.setViewPager(mBinding.vp);
        mBinding.tl2.setOnTabSelectListener(this);
        mBinding.tl3.setViewPager(mBinding.vp);
        mBinding.tl4.setViewPager(mBinding.vp);
        mBinding.tl5.setViewPager(mBinding.vp);
        mBinding.tl6.setViewPager(mBinding.vp);
        mBinding.tl7.setViewPager(mBinding.vp, mTitles);
        mBinding.tl8.setViewPager(mBinding.vp, mTitles, this, mFragments);
        mBinding.tl9.setViewPager(mBinding.vp);
        mBinding.tl10.setViewPager(mBinding.vp);


        mBinding.vp.setCurrentItem(4);
        //设置显示点
        mBinding.tl1.showDot(4);
        mBinding.tl2.showDot(4);
        mBinding.tl3.showDot(4);


        mBinding.tl2.showMsg(3, 5);
        mBinding.tl2.setMsgMargin(3, 0, 10);

        MsgView msgView = mBinding.tl2.getMsgView(3);
        if (msgView != null) {
            msgView.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        mBinding.tl2.showMsg(5, 5);
        mBinding.tl2.setMsgMargin(5, 0, 10);
    }

    @Override
    public void onTabSelect(int position) {
        Toast.makeText(this, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(this, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager mFragmentManager;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
