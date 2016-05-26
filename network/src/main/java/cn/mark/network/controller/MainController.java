package cn.mark.network.controller;

import javax.inject.Inject;

import cn.mark.network.Display;
import cn.mark.network.moduls.component.ControllersComponent;
import cn.mark.network.moduls.component.DaggerControllersComponent;
import cn.mark.network.state.ApplicationState;

/**
 * Created by yaoping on 2016/5/26.
 */

public class MainController extends BaseUIController<MainController.MainUi, MainController.MainUiCallback> {
    @Inject
    UserController mUserController;

    public MainController() {
        ControllersComponent component = DaggerControllersComponent.create();
        component.inject(this);
    }

    @Override
    protected MainUiCallback createUiCallbacks(MainUi ui) {
        return null;
    }


    public interface MainUi extends BaseUIController.Ui<MainUiCallback> {

    }

    public interface MainUiCallback {

    }

    public void attachDisplay(Display display) {
        setDisplay(display);
    }

    public void detachDisplay(Display display) {
        setDisplay(null);
        mUserController.setDisplay(null);
    }

    @Override
    public void setDisplay(Display display) {
        super.setDisplay(display);
        mUserController.setDisplay(display);
    }

    @Override
    protected void onSuspended() {
        mUserController.suspend();
        super.onSuspended();
    }

    @Override
    protected void onInited() {
        mUserController.init();
        super.onInited();
    }

    public void setApplicationState(ApplicationState applicationState) {
        super.setApplicationState(applicationState);
        mUserController.setApplicationState(applicationState);
    }

    public UserController getUserController() {
        return mUserController;
    }
}
