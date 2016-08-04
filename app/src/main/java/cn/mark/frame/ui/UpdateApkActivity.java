package cn.mark.frame.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.UpdateApkBinding;
import cn.mark.frame.system.FrameApplication;
import cn.mark.network.controller.UserController;
import cn.mark.network.retrofit.bean.userjson.DownloadBean;
import cn.mark.network.retrofit.bean.userjson.UpdateApkBean;
import cn.mark.utils.Constant;
import cn.mark.utils.DialogUtils;
import cn.mark.utils.InstallUtils;
import cn.mark.utils.LogUtils;
import cn.mark.utils.ToastUtil;
import rx.functions.Action1;

/**
 * Created by yaoping on 2016/6/7.
 */
public class UpdateApkActivity extends BaseActivity implements UserController.UpdateApkUi {
    private UpdateApkBinding binding;
    private UserController userController;
    private UserController.UpdateApkCallback updateApkCallback;
    private static final String verison_code = "3.3.3";
    private DialogUtils dialogUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.update_apk);
        initClick();
    }

    private void initClick() {
        back();
        setHeadTitle(getString(R.string.update_apk_title));
        dialogUtils = new DialogUtils(this);
        userController = FrameApplication.getApplicationHelper().getMainController().getUserController();
        RxView.clicks(binding.button2).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                updateApkCallback.feachUpdateInfo(verison_code);
            }
        });
    }

    private static final String url = "http://pkg3.fir.im/709099240dae681c09ee5f83f3160a234f53f1d7.apk?filename=app-dev-release.apk_1.1.0.6.apk";

    //查询更新信息
    @Override
    public void FeachUpdateInfo(UpdateApkBean bean) {
        if (Constant.requestSuccess != bean.error_code) {
            ToastUtil.instance().show(bean.error_msg);
            return;
        }
      /*  dialogUtils.showUpdateApkDialog(getString(R.string.update_apk_content_hint), "", getString(R.string.update_apk_button_hint), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.instance().show("正在更新...");
                dialogUtils.shutdownDialog();*/
        updateApkCallback.downloadApk(UpdateApkActivity.this, "/709099240dae681c09ee5f83f3160a234f53f1d7.apk?filename=app-dev-release.apk_1.1.0.6.apk");
      /*      }
        });*/
    }

    @Override
    public void downloadApk(DownloadBean bean) {
        dialogUtils.shutdownDialog();//关闭dialog
        if (bean.error_code != Constant.requestSuccess) {
            ToastUtil.instance().show(bean.error_msg);
            return;
        }
        if (Constant.stringIsAir.equals(bean.app_url)) {
            ToastUtil.instance().show("app地址为空");
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
