package cn.mark.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yaoping on 2016/6/8.
 */
public class DialogUtils {
    private Activity mContext;
    private Dialog dialog;
    private static final String defaultLeftButtonText = "取消";
    private static final String defaultRightButtonText = "确定";

    public DialogUtils(Activity mContext) {
        this.mContext = mContext;
    }

    /***
     * 显示更新apkdialog
     *
     * @param content       提示文本
     * @param leftText      左边按钮文本
     * @param rightText     右边按钮文本
     * @param rightListener 右边按钮监听
     */
    public void showUpdateApkDialog(String content, String leftText, String rightText, View.OnClickListener rightListener) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.dialog_view, null);
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCancelable(false);
        dialog.show();

//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = (int) (LazyCatApplication.getHeightAndWidth(Constant.numZero, mContext) * 0.9); // 设置宽度
//        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);

        //初始化
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        final TextView dialog_cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView dialog_confirm = (TextView) view.findViewById(R.id.dialog_confirm);
        dialog_content.setText(content);
        dialog_cancel.setVisibility(Constant.stringIsAir.equals(leftText) ? View.GONE : View.VISIBLE);
        dialog_confirm.setText(Constant.stringIsAir.equals(rightText) ? defaultRightButtonText : rightText);
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_confirm.setOnClickListener(rightListener == null ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } : rightListener);
    }

    /**
     * 关闭对话框
     */
    public void shutdownDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 判断dialog是否打开
     */
    public boolean dialogIsOpen() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }
}
