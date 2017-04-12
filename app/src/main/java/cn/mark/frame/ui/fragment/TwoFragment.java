package cn.mark.frame.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;
import cn.mark.frame.databinding.FragmentTwoBinding;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-12 15:36
 */
public class TwoFragment extends BaseFragment {
    private FragmentTwoBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mBinding) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_two, container, false);
        }
        return mBinding.getRoot();
    }
}
