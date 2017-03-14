package cn.mark.frame.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityMainBinding;
import cn.mark.frame.system.FrameApplication;
import cn.mark.network.controller.UserController;
import cn.mark.network.retrofit.bean.userjson.UserBean;
import cn.mark.utils.CircularAnimUtil;
import cn.mark.utils.Constant;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements UserController.LoginUi {
    private ActivityMainBinding binding;
    private UserController userController;
    private UserController.UserLoginCallback userLoginCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        initClick();
    }

    private void initView() {
        back();
        setHeadTitle(R.string.user_login_title);
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

    private void initClick() {
        userController = FrameApplication.getApplicationHelper().getMainController().getUserController();
        RxView.clicks(binding.userLoginButton).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                userLogin();
                CircularAnimUtil.hide(binding.userLoginButton);//动画效果隐藏按钮
            }
        });
        RxView.clicks(binding.updateApk).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MainActivity.this, UpdateApkActivity.class));
            }
        });
        RxView.clicks(binding.userRegiestButton).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                startActivity(new Intent(MainActivity.this, RegiestActivity.class));
            }
        });
        RxView.clicks(binding.loadingLongView).throttleFirst(Constant.defaultClickTime,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(MainActivity.this,PhotoViewActivity.class));
            }
        });
    }

    private void userLogin() {
        String account = binding.userLoginInputAccount.getText().toString().trim();
        String password = binding.userLoginInputPassword.getText().toString().trim();
        if (Constant.stringIsAir.equals(account)) {
            Toast.makeText(this, getString(R.string.user_login_account_input_hint), Toast.LENGTH_LONG).show();
            return;
        }
        if (Constant.stringIsAir.equals(password)) {
            Toast.makeText(this, getString(R.string.user_login_password_input_hint), Toast.LENGTH_LONG).show();
            return;
        }
        userLoginCallback.userLogin(account, password);
    }

    @Override
    public void userLoginBack(UserBean userBean) {
        if (userBean.error_code == Constant.requestSuccess) {
            Toast.makeText(this, getString(R.string.user_login_request_success_hint), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, UpdateApkActivity.class));
        } else {
            Toast.makeText(this, userBean.error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setCallbacks(UserController.UserUiCallback callbacks) {
        userLoginCallback = (UserController.UserLoginCallback) callbacks;
    }
}
