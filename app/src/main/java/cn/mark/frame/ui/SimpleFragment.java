package cn.mark.frame.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.mark.frame.R;
import cn.mark.frame.databinding.FragmentSimpleBinding;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-07 14:09
 */
public class SimpleFragment extends Fragment {
    private FragmentSimpleBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mBinding) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple, container, false);
        }
        return mBinding.getRoot();
    }

    public void setTvTitleBackgroundColor(@ColorInt int color) {
        mBinding.tvTitle.setBackgroundColor(color);
        mBinding.fakeStatusBar.setBackgroundColor(color);
    }
}
