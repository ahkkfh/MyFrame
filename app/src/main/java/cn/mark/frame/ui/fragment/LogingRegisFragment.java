package cn.mark.frame.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;
import cn.mark.frame.databinding.FragmentLoginBinding;
import cn.mark.frame.system.FrameApplication;
import cn.mark.frame.ui.activity.FingerprintActivity;
import cn.mark.frame.ui.activity.UpdateApkActivity;
import cn.mark.network.controller.UserController;
import cn.mark.network.retrofit.bean.userjson.UserBean;
import cn.mark.utils.CircularAnimUtil;
import cn.mark.utils.Constant;
import rx.functions.Action1;


/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-07 17:15
 */
public class LogingRegisFragment extends BaseFragment implements UserController.LoginUi {
    private FragmentLoginBinding mBinding;
    private UserController userController;
    private UserController.UserLoginCallback userLoginCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mBinding) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        }
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        userController = FrameApplication.getApplicationHelper().getMainController().getUserController();
        initClick();
    }


    private void initClick() {
        RxView.clicks(mBinding.userLoginButton).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                userLogin();
                CircularAnimUtil.hide(mBinding.userLoginButton);//动画效果隐藏按钮
            }
        });
        RxView.clicks(mBinding.userRegiestButton).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                startActivity(new Intent(MainActivity.this, RegiestActivity.class));
            }
        });
        RxView.clicks(mBinding.updateApk).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), UpdateApkActivity.class));
            }
        });
        RxView.clicks(mBinding.fingerprintLogin).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
            startActivity(new Intent(getActivity(),FingerprintActivity.class));
            }
        });
    }
    private void userLogin() {
        String account = mBinding.userLoginInputAccount.getText().toString().trim();
        String password = mBinding.userLoginInputPassword.getText().toString().trim();
        if (Constant.stringIsAir.equals(account)) {
            Toast.makeText(getActivity(), getString(R.string.user_login_account_input_hint), Toast.LENGTH_LONG).show();
            return;
        }
        if (Constant.stringIsAir.equals(password)) {
            Toast.makeText(getActivity(), getString(R.string.user_login_password_input_hint), Toast.LENGTH_LONG).show();
            return;
        }
        userLoginCallback.userLogin(account, password);
    }

    @Override
    public void onResume() {
        super.onResume();
        userController.attachUi(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        userController.detachUi(this);
    }

    @Override
    public void userLoginBack(UserBean userBean) {
        if (userBean.error_code == Constant.requestSuccess) {
            Toast.makeText(getActivity(), getString(R.string.user_login_request_success_hint), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), UpdateApkActivity.class));
        } else {
            Toast.makeText(getActivity(), userBean.error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setCallbacks(UserController.UserUiCallback callbacks) {
        userLoginCallback = (UserController.UserLoginCallback) callbacks;
    }
}
