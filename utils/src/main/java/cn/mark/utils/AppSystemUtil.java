package cn.mark.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by yaoping on 2016/5/18.
 */
public class AppSystemUtil {
    private final static String OSVERSION = Build.VERSION.RELEASE;//获取android系统版本
    private final static String DEVICE_NAME = Build.BRAND + Build.MODEL;//获取设备名称
    private final static String DEVICE_TYPE = "android";//设备类型
    private final static String DEVICE_ID = Build.SERIAL;//设备id
    private static String versionName;
    private static String packageName;
    private static int versionCode;
    private static int AppUserID;
    private static String UserToken;

    public static void initAppSystemData(Context context){
        //获取PackageManager的实例
        PackageManager packageManager=context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packageInfo=null;
        String version;
        try{
            packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            packageName = packageInfo.packageName;
        }catch (Exception e){
            e.printStackTrace();
            versionName ="-1.0.0";
            versionCode = -1;
        }
    }

    public static String getOSVERSION() {
        return OSVERSION;
    }

    public static String getDeviceName() {
        return DEVICE_NAME;
    }

    public static String getDeviceType() {
        return DEVICE_TYPE;
    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }

    public static String getVersionName() {
        return versionName;
    }

    public static int getVersionCode() {
        return versionCode;
    }

    public static int getAppUserID() {
        return AppUserID;
    }

    public static void setVersionName(String versionName) {
        AppSystemUtil.versionName = versionName;
    }

    public static void setVersionCode(int versionCode) {
        AppSystemUtil.versionCode = versionCode;
    }

    public static void setAppUserID(int appUserID) {
        AppUserID = appUserID;
    }

    public static String getUserToken() {
        return UserToken;
    }

    public static void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public static String getPackageName() {
        return packageName;
    }
}
