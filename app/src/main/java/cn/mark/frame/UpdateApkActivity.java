package cn.mark.frame;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.databinding.UpdateApkBinding;
import cn.mark.network.controller.UserController;
import cn.mark.network.retrofit.bean.userjson.DownloadBean;
import cn.mark.network.retrofit.bean.userjson.UpdateApkBean;
import cn.mark.utils.AppSystemUtil;
import cn.mark.utils.Constant;
import cn.mark.utils.DialogUtils;
import cn.mark.utils.InstallUtils;
import cn.mark.utils.ToastUtil;
import rx.functions.Action1;

/**
 * Created by yaoping on 2016/6/7.
 */
public class UpdateApkActivity extends AutoLayoutActivity implements UserController.UpdateApkUi {
    private UpdateApkBinding binding;
    private UserController userController;
    private UserController.UpdateApkCallback updateApkCallback;
    private static final String verison_code = "3.3.3";
    //    private static final  String verison_code="2.2.2";
//    private static final  String verison_code="1.1.1";
    private DialogUtils dialogUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.update_apk);
        initClick();
    }

    private void initClick() {
        userController = FrameApplication.getApplicationHelper().getMainController().getUserController();
        RxView.clicks(binding.button1).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(UpdateApkActivity.this, "button1", Toast.LENGTH_SHORT).show();
            }
        });
        RxView.clicks(binding.button2).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                updateApkCallback.feachUpdateInfo(verison_code);
                updateApkCallback.feachUpdateInfo(AppSystemUtil.getVersionName());
            }
        });
    }

    @Override
    public void FeachUpdateInfo(final UpdateApkBean bean) {
        Log.i("lbxx", "更新信息" + bean.error_code + ",type==" + bean.getUpgrade_type());
        if (Constant.requestSuccess != bean.error_code) {
            ToastUtil.instance().show(bean.error_msg);
            return;
        }
        if (Constant.numTen.equals(bean.getUpgrade_type())) {
            ToastUtil.instance().show(getString(R.string.update_apk_newest_version));
            return;
        }
        if (Constant.numTwo.equals(bean.getUpgrade_type())) {
            dialogUtils.showUpdateApkDialog(getString(R.string.update_apk_content_hint), "", getString(R.string.update_apk_button_hint), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.instance().show(getString(R.string.update_apk_toasts, "1.0.1"));
                    updateApkCallback.downloadApk(UpdateApkActivity.this, bean.getUrl());
                }
            });
        }
    }

    @Override
    public void downloadApk(DownloadBean bean) {
        Log.i("lbxx", "code==" + bean.error_code + "==msg=" + bean.error_msg);
        if (bean.error_code != 0) {
            ToastUtil.instance().show(bean.error_msg);
            return;
        }
        InstallUtils.installApk(this, bean.app_url);
    }

    @Override
    public void setCallbacks(UserController.UserUiCallback callbacks) {
        updateApkCallback = (UserController.UpdateApkCallback) callbacks;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userController.attachUi(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userController.detachUi(this);
    }
}
