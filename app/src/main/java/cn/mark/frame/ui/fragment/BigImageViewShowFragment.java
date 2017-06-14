package cn.mark.frame.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;
import cn.mark.frame.databinding.ActivityShowBigImageBinding;
import cn.mark.utils.Constant;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO(用于展示BigImageView 入口Activity)
 * @date:2017-06-14 18:27
 */
public class BigImageViewShowFragment extends BaseFragment {
    private ActivityShowBigImageBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mBinding) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_show_big_image, container, false);
        }
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        RxView.clicks(mBinding.frescoLoader).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.i("lbxx", "fresco加载");
                toIntent();
            }
        });
        RxView.clicks(mBinding.glideLoader).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.i("lbxx", "glide加载图片");
            }
        });
        RxView.clicks(mBinding.longIamge).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.i("lbxx", "long图加载");
            }
        });
        RxView.clicks(mBinding.mScaleType).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.i("lbxx", "mscaleType");
            }
        });
    }

    private void toIntent() {

    }
}
