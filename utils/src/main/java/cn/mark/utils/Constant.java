package cn.mark.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by yaoping on 2016/5/26.
 * 常量
 */
public class Constant {
    public static final String stringIsAir = "";
    public static final long defaultClickTime = 500;

    //    public final static String SERVER ="http://192.168.1.5:8090";
    public final static String SERVER = "https://api.cattrip.net";

    public static final int requestSuccess = 0;
    public static final String numTwo = "2";
    public static final String requestOk = "0000";

    public static final String saveApkPath = Environment.getExternalStorageDirectory() + File.separator + "download";

    public static final String downloadFileName = "lazycat.apk";

    public static final String numTen = "10";
}
