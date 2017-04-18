package cn.mark.frame.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityFingerprintBinding;
import cn.mark.frame.system.CryptoObjectHelper;


/***
 * @author marks.luo
 * @Description: TODO(指纹识别的Activity)
 * @date:2017-04-18 14:35
 */
public class FingerprintActivity extends BaseActivity {
    private ActivityFingerprintBinding mBinding;
    private FingerprintManagerCompat mManagerCompat;
    private CancellationSignal mCancellationSignal;//取消信息号
    public static final int PERISSION_CODE = 1;
    private FingerprintCallback mFingerprintCallback;
    private Handler mHandler;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;
    private AlertDialog.Builder builder = null;
    private Dialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_fingerprint);
        initView();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mBinding.fingerprintToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("指纹识别");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initFingerprint();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initFingerprintDialog();
        fingerprintLogin();
    }

    private void initFingerprintDialog() {
        if (null == builder) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("指纹识别");
            builder.setMessage("指纹登录，请输入指纹");
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            mDialog = builder.create();
            mDialog.show();
        } else {
            builder.show();
        }
    }

    /***
     * 指纹识别
     */
    private void fingerprintLogin() {
        boolean permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED;
        if (permissionCheck) {
            //发生获取权限的方法
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, PERISSION_CODE);
        } else {
            login();
        }
    }

    private void login() {
        try {
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            if (mCancellationSignal == null) {
                mCancellationSignal = new CancellationSignal();
            }
            mManagerCompat.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                    mCancellationSignal, mFingerprintCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    login();
                }
                break;
        }
    }

    private void initFingerprint() {
        mManagerCompat = FingerprintManagerCompat.from(this);
        if (!mManagerCompat.isHardwareDetected()) {//不存在传感器
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_sensor_dialog_title);
            builder.setMessage(R.string.no_sensor_dialog_message);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else if (!mManagerCompat.hasEnrolledFingerprints()) {//判断是否有录入指纹
            // no fingerprint image has been enrolled.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_fingerprint_enrolled_dialog_title);
            builder.setMessage(R.string.no_fingerprint_enrolled_dialog_message);
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            // show this dialog
            builder.create().show();
        } else {
            try {
                if (null == mFingerprintCallback) {
                    mFingerprintCallback = new FingerprintCallback(mHandler);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i("lbxx", "msg==" + msg.what + "===" + msg.toString());
                switch (msg.what) {
                    case MSG_AUTH_SUCCESS://成功
                        setResultInfo("指纹识别成功");
                        mCancellationSignal = null;
                        break;
                    case MSG_AUTH_FAILED://身份验证失败
                        setResultInfo("指纹识别不了，请重试");
                        mCancellationSignal = null;
                        break;
                    case MSG_AUTH_ERROR:
                        handleErrorCode(msg.arg1);
                        break;
                    case MSG_AUTH_HELP:
                        handleHelpCode(msg.arg1);
                }
            }
        };
    }

    private void handleHelpCode(int code) {
        String temp = null;
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD://指纹获取良好
                temp = "the image acquired was good";
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                temp = getString(R.string.AcquiredImageDirty_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                temp = getString(R.string.AcquiredInsufficient_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                temp = getString(R.string.AcquiredImageDirty_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                temp = getString(R.string.AcquiredTooFast_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                temp = getString(R.string.AcquiredToSlow_warning);
                break;
        }
        setResultInfo(temp == null ? "" : temp);
    }

    private void handleErrorCode(int code) {
        String temp = null;
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                temp = getString(R.string.ErrorCanceled_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                temp = getString(R.string.ErrorHwUnavailable_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                temp = getString(R.string.ErrorLockout_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                temp = getString(R.string.ErrorNoSpace_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                temp = getString(R.string.ErrorTimeout_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                temp = getString(R.string.ErrorUnableToProcess_warning);
                break;
        }
        setResultInfo(temp == null ? "" : temp);
    }

    /***
     * 设置指纹识别信息
     * @param msg 提示信息
     */
    private void setResultInfo(String msg) {
        if (mBinding.showFingerprintInfo != null) {
            mBinding.showFingerprintInfo.setTextColor(getResources().getColor(msg.equals(R.string.fingerprint_success) ? R.color.success_color : R.color.warning_color));
            mBinding.showFingerprintInfo.setText(msg);
        }
    }

    /**
     * 指纹识别的回调监听
     */
    private class FingerprintCallback extends FingerprintManagerCompat.AuthenticationCallback {
        private Handler handler = null;

        public FingerprintCallback(Handler handler) {
            super();
            this.handler = handler;
        }

        /***
         * 认证错误
         * @param errMsgId
         * @param errString 错误信息
         */
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
//            Log.i("lbxx", "onAuthenticationError===" + errMsgId + "==" + errString);
            if (handler != null) {
                handler.obtainMessage(MSG_AUTH_ERROR, errMsgId, 0).sendToTarget();
            }
//            if (dialog != null) {
//                dialog.dismiss();
//            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            if (handler != null) {
                handler.obtainMessage(MSG_AUTH_FAILED).sendToTarget();
            }
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
//            Log.i("lbxx", "onAuthenticationHelp===" + helpMsgId + "==" + helpString);
            if (handler != null) {
                handler.obtainMessage(MSG_AUTH_HELP, helpMsgId, 0).sendToTarget();
            }
//            if (dialog != null) {
//                dialog.dismiss();
//            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
//            Log.i("lbxx", "onAuthenticationSucceeded===" + result.getCryptoObject());
            if (handler != null) {
                handler.obtainMessage(MSG_AUTH_SUCCESS).sendToTarget();
            }
//            Log.i("lbxx", "dialog==null:" + (mDialog == null));
            if (mDialog != null) {
//                Log.i("lbxx", "隐藏dialog start");
                mDialog.dismiss();
//                Log.i("lbxx", "隐藏dialog  end");
            }
        }
    }
}
