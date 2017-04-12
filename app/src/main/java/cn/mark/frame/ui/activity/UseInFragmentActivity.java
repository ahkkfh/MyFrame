package cn.mark.frame.ui.activity;

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
import java.util.Random;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityUseInFragmentBinding;
import cn.mark.frame.ui.fragment.ImageFragment;
import cn.mark.frame.ui.fragment.SimpleFragment;
import cn.mark.utils.StatusBarUtil;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-06 11:53
 */
public class UseInFragmentActivity extends BaseActivity {
    private ActivityUseInFragmentBinding mBinding;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_use_in_fragment);
        initView();
    }

    protected void initView() {
        mBinding.bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_favorite, "one"))
                .addItem(new BottomNavigationItem(R.drawable.ic_gavel, "two"))
                .addItem(new BottomNavigationItem(R.drawable.ic_grade, "three"))
                .addItem(new BottomNavigationItem(R.drawable.ic_group_work, "four"))
                .initialise();
        mBinding.bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {//设置item选择
            @Override
            public void onTabSelected(int position) {
                mBinding.vpHome.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        mFragmentList.add(new ImageFragment());
        mFragmentList.add(new SimpleFragment());
        mFragmentList.add(new SimpleFragment());
        mFragmentList.add(new SimpleFragment());
        mBinding.vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.bottomNavigationBar.selectTab(position);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        Random random = new Random();
                        int color = 0xff000000 | random.nextInt(0xffffff);
                        if (mFragmentList.get(position) instanceof SimpleFragment) {
                            ((SimpleFragment) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.vpHome.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(UseInFragmentActivity.this, null);
    }
}
