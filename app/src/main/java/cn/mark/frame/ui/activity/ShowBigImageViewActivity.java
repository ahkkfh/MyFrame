package cn.mark.frame.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityShowBigImageBinding;
import cn.mark.utils.Constant;
import rx.functions.Action1;

/**
 * Created by Ruoly on 2017/6/11.
 * 大图展示页
 */
public class ShowBigImageViewActivity extends BaseActivity {
    private ActivityShowBigImageBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_big_image);
        initView();
    }

    @Override
    protected void initView() {
        RxView.clicks(mBinding.frescoLoader).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        RxView.clicks(mBinding.longIamge).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(ShowBigImageViewActivity.this, LongImageActivity.class));
            }
        });
        RxView.clicks(mBinding.mScaleType).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
    }
}
