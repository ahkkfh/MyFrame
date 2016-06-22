package cn.mark.utils;


import com.orhanobut.logger.Logger;

/**
 * Created by yaoping on 2016/6/22.
 */
public class LogUtils {
    public static final String defaultName = "lbxx";

    public static void verbose(Object obj) {
        Logger.v(defaultName, obj);
    }

    public static void verbose(String tag, Object obj) {
        Logger.v(tag, obj);
    }

    public static void deBug(Object obj) {
        Logger.d(defaultName, obj);
    }

    public static void deBug(String tag, Object obj) {
        Logger.d(tag, obj);
    }

    public static void infoMsg(Object obj) {
        Logger.i(defaultName, obj);
    }

    public static void infoMsg(String tag, Object obj) {
        Logger.i(tag, obj);
    }

    public static void warn(Object msg) {
        Logger.w(defaultName, msg);
    }

    public static void warn(String tag, Object msg) {
        Logger.w(tag, msg);
    }

    public static void error(Object msg) {
        Logger.e(defaultName, msg);
    }

    public static void error(String tag, Object msg) {
        Logger.e(tag, msg);
    }

    public static void Assert(Object msg) {
        Logger.e(defaultName, msg);
    }

    public static void Assert(String tag, Object msg) {
        Logger.e(tag, msg);
    }
}
