package cn.mark.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;

/**
 * Created by yaoping on 2016/6/14.
 * 安装的帮助类
 */
public class InstallUtils {
    private static final String type = "application/vnd.android.package-archive";

    public static void installApk(Context context, String apkUrl) {
        File file = new File(apkUrl);
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(apkUrl), type);
        context.startActivity(intent);
    }
}
