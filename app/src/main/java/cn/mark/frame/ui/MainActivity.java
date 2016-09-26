package cn.mark.frame.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityMainBinding;
import cn.mark.frame.helper.LoadingHelper;
import cn.mark.frame.system.FrameApplication;
import cn.mark.network.controller.UserController;
import cn.mark.network.retrofit.bean.userjson.UserBean;
import cn.mark.utils.Constant;
import cn.mark.utils.ToastUtil;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements UserController.LoginUi {
    private ActivityMainBinding binding;
    private UserController userController;
    private UserController.UserLoginCallback userLoginCallback;
    private LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initAtionBar();
        initView();
        initClick();
    }

    private void initAtionBar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.mainDrawer, R.string.bar_open, R.string.bar_close);
        actionBarDrawerToggle.syncState();
        binding.mainDrawer.setDrawerListener(actionBarDrawerToggle);
    }

    private void initView() {
        loadingHelper = new LoadingHelper(this, binding.activityMain);
    }

    private void initClick() {
        userController = FrameApplication.getApplicationHelper().getMainController().getUserController();
        RxView.clicks(binding.userLoginButton).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                userLogin();
            }
        });
        RxView.clicks(binding.lefyLayoutName).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.instance().show("点击name");
            }
        });
        RxView.clicks(binding.lefyLayoutFlag).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.instance().show("点击标签");
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
        loadingHelper.show();
    }

    @Override
    public void userLoginBack(UserBean userBean) {
        loadingHelper.hide();
        if (userBean.error_code == Constant.requestSuccess) {
            Toast.makeText(this, getString(R.string.user_login_request_success_hint), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, userBean.error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (binding.mainDrawer.isDrawerOpen(GravityCompat.START)) {
                binding.mainDrawer.closeDrawers();
            } else {
                binding.mainDrawer.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void setCallbacks(UserController.UserUiCallback callbacks) {
        userLoginCallback = (UserController.UserLoginCallback) callbacks;
    }
}
