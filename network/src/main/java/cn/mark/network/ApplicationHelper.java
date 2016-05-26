package cn.mark.network;

import android.app.Application;

import javax.inject.Inject;

import cn.mark.network.controller.MainController;
import cn.mark.network.moduls.component.ApplicationComponent;
import cn.mark.network.moduls.component.DaggerApplicationComponent;
import cn.mark.network.state.ApplicationState;
import cn.mark.utils.AppSystemUtil;


/**
 * Created by yaoping on 2016/5/26.
 */

public class ApplicationHelper {
    private static Application mApplication;

    public static Application instance() {
        return mApplication;
    }

    public ApplicationComponent applicationComponent = DaggerApplicationComponent.create();

    @Inject
    MainController mainController;

    @Inject
    ApplicationState applicationState;

    public ApplicationHelper(Application _mApplication) {
        mApplication = _mApplication;
        initConfig();
    }

    private void initConfig() {
        initApplicationState();
        AppSystemUtil.initAppSystemData(mApplication);
    }

    private void initApplicationState() {
        applicationComponent.inject(this);
        mainController.setApplicationState(applicationState);
    }

    public MainController getMainController() {
        return mainController;
    }
}
