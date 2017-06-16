package cn.mark.frame.system;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.liulishuo.filedownloader.FileDownloader;

import cn.mark.network.ApplicationHelper;
import longimage.bigImageView.BigImageViewer;
import longimage.fresco.FrescoImageLoader;

/**
 * Created by yaoping on 2016/5/26.
 */

public class FrameApplication extends Application {
    static ApplicationHelper applicationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationHelper = new ApplicationHelper(this);
        //Chrome plug-in initialization code debugging tools
        //Chome插件调试工具的初始化代码
        Stetho.initializeWithDefaults(this);
        Fresco.initialize(this);
        FileDownloader.init(getApplicationContext());
        initBigImageView();//初始化BigImageView
    }

    private void initBigImageView() {
        BigImageViewer.initialize(FrescoImageLoader.with(this));
    }

    public static ApplicationHelper getApplicationHelper() {
        return applicationHelper;
    }
}
