package cn.mark.frame.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;
import cn.mark.frame.databinding.FragmentCenterBinding;
import cn.mark.frame.ui.activity.ChartActivity;
import cn.mark.frame.ui.activity.ImageSelectActivity;
import cn.mark.frame.ui.activity.StatusBarActivty;
import cn.mark.frame.ui.activity.TopTabActivity;
import cn.mark.frame.ui.activity.ViewPagerActivity;
import cn.mark.utils.Constant;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-12 15:15
 */
public class CenterFragment extends BaseFragment {
    private FragmentCenterBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mBinding) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_center, container, false);
        }
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        RxView.clicks(mBinding.statusBarLayout).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), StatusBarActivty.class));
            }
        });
        RxView.clicks(mBinding.chartButton).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), ChartActivity.class));
            }
        });
        RxView.clicks(mBinding.topTab).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), TopTabActivity.class));
            }
        });
        RxView.clicks(mBinding.bigImageView).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), ViewPagerActivity.class));
            }
        });
        RxView.clicks(mBinding.imageSelect).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), ImageSelectActivity.class));
            }
        });
    }
}
