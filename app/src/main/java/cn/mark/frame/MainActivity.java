package cn.mark.frame;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.databinding.ActivityMainBinding;
import cn.mark.network.controller.UserController;
import cn.mark.network.retrofit.bean.userjson.UserBean;
import cn.mark.utils.Constant;
import rx.functions.Action1;

public class MainActivity extends AutoLayoutActivity implements UserController.LoginUi {
    private ActivityMainBinding binding;
    private UserController userController;
    private UserController.UserLoginCallback userLoginCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initClick();
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
        } else {
            Toast.makeText(this, userBean.error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setCallbacks(UserController.UserUiCallback callbacks) {
        userLoginCallback = (UserController.UserLoginCallback) callbacks;
    }
}
