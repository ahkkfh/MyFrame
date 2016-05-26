package cn.mark.frame;

import android.app.Application;

import cn.mark.network.ApplicationHelper;

/**
 * Created by yaoping on 2016/5/26.
 */

public class FrameApplication extends Application {
    static ApplicationHelper applicationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationHelper = new ApplicationHelper(this);
    }

    public static ApplicationHelper getApplicationHelper() {
        return applicationHelper;
    }
}
