package cn.mark.frame.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityMaterialSearchViewBinding;
import cn.mark.utils.Constant;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-08-16 14:30
 *
 */
public class MaterialSearchViewActivity extends BaseActivity {
    private ActivityMaterialSearchViewBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_material_search_view);
        initView();
    }

    @Override
    protected void initView() {
        RxView.clicks(mBinding.buttonDefault).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MaterialSearchViewActivity.this, MaterialSearchViewDefaultActivity.class));
            }
        });
        RxView.clicks(mBinding.buttonInputType).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MaterialSearchViewActivity.this, MaterialSearchViewInputTypeActivity.class));
            }
        });
        RxView.clicks(mBinding.buttonSticky).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MaterialSearchViewActivity.this, MaterialSearchViewStickyActivity.class));
            }
        });
        RxView.clicks(mBinding.buttonTab).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MaterialSearchViewActivity.this, MaterialSearchViewTabActivity.class));
            }
        });
        RxView.clicks(mBinding.buttonThemed).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MaterialSearchViewActivity.this, MaterialSearchColoredActivity.class));
            }
        });
        RxView.clicks(mBinding.buttonVoice).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MaterialSearchViewActivity.this, MaterialSearchViewVoiceActivity.class));
            }
        });
    }
}
