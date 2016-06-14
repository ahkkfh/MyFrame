package cn.mark.utils;

import android.content.Context;
import android.widget.Toast;

import cn.mark.network.ApplicationHelper;

/**
 * Created by Duncan on 16/3/23.
 */
public class ToastUtil {
    private static String emptyStr = "";
    private Toast mToast;
    private static ToastUtil toastUtil;
    public static ToastUtil instance(){
        if(null == toastUtil){
            toastUtil = new ToastUtil(ApplicationHelper.instance());
        }

        return toastUtil;
    }

    private ToastUtil(Context context){
        mToast = Toast.makeText(context, emptyStr, Toast.LENGTH_SHORT);
    }

    public void show(String text){
        mToast.setText(text);
        mToast.show();
    }
}
