package cn.mark.utils.widget.tablayout.utils;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import cn.mark.utils.widget.tablayout.view.MsgView;

/***
 * @author marks.luo
 * @Description: TODO(未读消息提示View, 显示小红点或者带有数字的红点:
 * 数字一位, 圆
 *数字两位, 圆角矩形, 圆角是高度的一半
 *数字超过两位, 显示99+)
 * @date:2017-04-25 14:51
 */
public class UnreadMsgUtils {
    /***
     * tab的title中显示数量
     * @param msgView
     * @param num
     */
    public static void show(MsgView msgView, int num) {
        if (msgView == null) return;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            msgView.setStrokeWidth(0);
            msgView.setText("");

            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
            msgView.setLayoutParams(lp);
        } else {
            lp.height = (int) (18 * dm.density);
            if (num > 0 && num < 10) {//圆
                lp.width = (int) (18 * dm.density);
                msgView.setText(num + "");
            } else if (num > 9 && num < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                msgView.setText(num + "");
            } else {//数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                msgView.setText("99+");
            }
            msgView.setLayoutParams(lp);
        }
    }

    public static void setSize(MsgView mv, int size) {
        if (mv == null) return;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mv.getLayoutParams();
        params.width = size;
        params.height = size;
        mv.setLayoutParams(params);
    }
}
