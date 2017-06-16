package cn.mark.frame.ui;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityHomeBinding;
import cn.mark.frame.ui.fragment.BigImageViewShowFragment;
import cn.mark.frame.ui.fragment.CenterFragment;
import cn.mark.frame.ui.fragment.HomeFragment;
import cn.mark.frame.ui.fragment.LogingRegisFragment;
import cn.mark.frame.ui.fragment.TwoFragment;
import cn.mark.utils.StatusBarUtil;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-07 17:09
 */
public class MainActivity extends BaseActivity {
    private ActivityHomeBinding mBinding;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] arrays = new String[]{"Home", "Two", "Login&Regiest", "Center", "Big"};
    private Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.activity_home);
        initView();
    }

    protected void initView() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle("Home");
        mBinding.homeBottomBar
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite, arrays[0]))
                .addItem(new BottomNavigationItem(R.drawable.ic_gavel, arrays[1]))
                .addItem(new BottomNavigationItem(R.drawable.ic_group_work, arrays[2]))
                .addItem(new BottomNavigationItem(R.drawable.ic_avatar, arrays[3]))
                .addItem(new BottomNavigationItem(R.drawable.ic_avatar, arrays[4]))
                .initialise();
        mBinding.homeBottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                getSupportActionBar().setTitle(arrays[position]);
                mBinding.homeViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        mFragments.add(new HomeFragment());
        mFragments.add(new TwoFragment());
        mFragments.add(new LogingRegisFragment());
        mFragments.add(new CenterFragment());
        mFragments.add(new BigImageViewShowFragment());
        mBinding.homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(arrays[position]);
                mBinding.homeBottomBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.homeViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_02D1B1), 120);
    }
}

