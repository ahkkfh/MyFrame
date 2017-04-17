package cn.mark.frame.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fingerprints.service.FingerprintManager;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;
import cn.mark.frame.databinding.FragmentLoginBinding;
import cn.mark.frame.system.FrameApplication;
import cn.mark.frame.ui.activity.UpdateApkActivity;
import cn.mark.network.controller.UserController;
import cn.mark.network.retrofit.bean.userjson.UserBean;
import cn.mark.utils.CircularAnimUtil;
import cn.mark.utils.Constant;
import cn.mark.utils.ToastUtil;
import rx.functions.Action1;

import static android.R.attr.id;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-07 17:15
 */
public class LogingRegisFragment extends BaseFragment implements UserController.LoginUi {
    private FragmentLoginBinding mBinding;
    private UserController userController;
    private UserController.UserLoginCallback userLoginCallback;
    private FingerprintManager mFm;//指纹管理器
    private FingerprintManager.IdentifyCallback mIdentifyCallback;//指纹识别的回调
    private android.hardware.fingerprint.FingerprintManager mFingerprintManager_M;


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
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    fingerprintLogin();
                } else {
                    fingerprintLoginForAndroid_M();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fingerprintLoginForAndroid_M() {
        initAndroidMFingerprint();
        checkSelfPermission();//检测运行时权限
        Log.i("lbxx", "android M 指纹识别:" + (mFingerprintManager_M == null));
    }

    public static final int PERISSION_CODE = 1;

    /***
     * 检查android M 的运行时权限
     */
    private void checkSelfPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.USE_FINGERPRINT);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {//判断是否有该运行权限
            //发生获取权限的方法
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.USE_FINGERPRINT}, PERISSION_CODE);
        } else {
            androidMFingerprintLogin();
        }
    }

    /***
     * android M 指纹识别认证
     */
    private void androidMFingerprintLogin() {

    }

    /***
     * 权限请求回调方法
     * @param requestCode 请求code
     * @param permissions 需要获取的运行时权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERISSION_CODE://指纹识别权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    androidMFingerprintLogin();
                }
                break;
        }
    }

    /***
     * android M 指纹识别初始化
     */
    private void initAndroidMFingerprint() {
        if (mFingerprintManager_M == null) {
            mFingerprintManager_M = (android.hardware.fingerprint.FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        }
    }

    /***
     * 指纹登录
     */
    private void fingerprintLogin() {
        initFigerprint();//得到FingerprintManager的实例
        Log.i("lbxx", "mFm==null:" + (mFm == null));
        int[] ids = mFm.getIds();
        if (ids == null) {//得到系统中已经录入的指纹个数
            Log.i("lbxx", "no fingerprint enrolled");
        } else {
            if (null == mIdentifyCallback) {
                mIdentifyCallback = createIdentifyCallback();//创建指纹识别的回调方法
            }
            mFm.startIdentify(mIdentifyCallback, mFm.getIds());//调用指纹识别认证接口
        }
    }

    /**
     * 初始化指纹管理器
     */
    private void initFigerprint() {
        if (null == mFm) {
            mFm = FingerprintManager.open();
        }
    }

    /***
     * 创建指纹识别认证的回调方法
     * @return
     */
    private FingerprintManager.IdentifyCallback createIdentifyCallback() {
        return new FingerprintManager.IdentifyCallback() {
            @Override
            public void onIdentified(int i, boolean b) {//认证成功
                Log.d("lbxx", "onIdentified!, fingerId:" + id);
                ToastUtil.instance().show("指纹登录成功");
                mFm.release();
                //认证成功后release, 需要注意的是在不使用指纹功能后必须要调用release, 也就是说open和release严格配对
                //否则会造成mBack不能使用, 因为只有调用release之后才能从指纹模式切换到back模式
            }

            @Override
            public void onNoMatch() {//认证失败
                Log.i("lbxx", "onNoMatch");
                fingerprintLogin();//一次认证失败后重新再次发起认证
            }
        };
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
