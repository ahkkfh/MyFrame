package cn.mark.frame.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;
import cn.mark.frame.databinding.FragmentTwoBinding;
import cn.mark.utils.Constant;
import cn.mark.utils.ToastUtil;
import cn.mark.utils.zxing.CaptureActivity;
import cn.mark.utils.zxing.ScanTools;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-12 15:36
 */
public class TwoFragment extends BaseFragment {
    private FragmentTwoBinding mBinding;
    public static final int REQUEST_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mBinding) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_two, container, false);
        }
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.qrCodeImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //todo:调用扫描工具
                ScanTools.scanCode(mBinding.qrCodeImg, new ScanTools.ScanCall() {
                    @Override
                    public void getCode(String code) {
                        ToastUtil.instance().show("二维码获取的信息===" + code);
                        mBinding.qrCodeShow.setText(code);
                    }
                });
                return true;
            }
        });
        RxView.clicks(mBinding.scanQrBtn).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivityForResult(intent, REQUEST_CODE);
            }
        });
        RxView.clicks(mBinding.qrcodeEncode).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                try {
                    //生成二维码
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    Bitmap www = ScanTools.createQRCode("mark.ruo", 600, 600, bitmap);
                    mBinding.qrCodeImg.setImageBitmap(www);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            ToastUtil.instance().show("getResult===" + scanResult);
        }
    }
}
