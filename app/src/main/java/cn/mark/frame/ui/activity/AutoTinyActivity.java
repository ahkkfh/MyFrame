package cn.mark.frame.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityAutoTinyBinding;
import cn.mark.utils.Constant;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-10 15:00
 */
public class AutoTinyActivity extends BaseActivity {
    private ActivityAutoTinyBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("AutoTinyWindow");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auto_tiny);
        initView();
    }

    @Override
    protected void initView() {
        RxView.clicks(mBinding.screenList).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(AutoTinyActivity.this, AutoTinyListActivity.class));
            }
        });
        RxView.clicks(mBinding.screenNormal).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(AutoTinyActivity.this, AutoTinyNormalActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
