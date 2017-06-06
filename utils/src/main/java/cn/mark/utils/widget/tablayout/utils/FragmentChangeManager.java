package cn.mark.utils.widget.tablayout.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

/***
 * @author marks.luo
 * @Description: TODO(Fragment管理器)
 * @date:2017-04-25 15:06
 */
public class FragmentChangeManager {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;
    /**
     * Fragment切换数组
     */
    private ArrayList<Fragment> mFragments;
    /**
     * 当前选中的Tab
     */
    private int mCurrentTab;

    public FragmentChangeManager(FragmentManager fragmentManager, int containerViewId, ArrayList<Fragment> fragments) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
        this.mFragments = fragments;
        initFragments();
    }

    /***
     * 初始化Fragment
     */
    private void initFragments() {
        for (Fragment fragment : mFragments) {
            mFragmentManager.beginTransaction().add(mContainerViewId, fragment);
        }
        setFragments(0);
    }

    /***
     * 页面切换控制
     * @param index
     */
    public void setFragments(int index) {
        for (int i = 0; i < mFragments.size(); i++) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment fragment = mFragments.get(i);
            if (i == index) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commit();
        }
        mCurrentTab = index;
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(mCurrentTab);
    }
}
