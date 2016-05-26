package cn.mark.network.controller;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import cn.mark.network.db.User;
import cn.mark.network.state.ApplicationState;


/**
 * Created by yaoping on 2016/5/19.
 */
public abstract class BaseUIController<U extends BaseUIController.Ui<UC>, UC> extends BaseController {

    public interface Ui<UC> {
        //设置回调
        void setCallbacks(UC callbacks);
    }

    public interface SubUi {
    }

    ApplicationState applicationState;

    private final Set<U> mUis;
    private final Set<U> mUnmodifiableUis;

    public BaseUIController() {
        super();
        mUis = new CopyOnWriteArraySet<>();
        mUnmodifiableUis = Collections.unmodifiableSet(mUis);
    }

    //添加当前activity到集合中
    public synchronized final void attachUi(U ui) {
        mUis.add(ui);
        ui.setCallbacks(createUiCallbacks(ui));
        if (isInited()) {
            onUiAttached(ui);
            populateUi(ui);
        }
    }

    //在集合中取消activity中
    public synchronized final void detachUi(U ui) {
        onUiDetached(ui);
        ui.setCallbacks(null);
        mUis.remove(ui);
    }

    protected final Set<U> getUis() {
        return mUnmodifiableUis;
    }

    @Override
    protected void onInited() {
        if (!mUis.isEmpty()) {
            for (U ui : mUis) {
                onUiAttached(ui);//添加ui
                populateUi(ui);//填充
            }
        }
    }

    protected void onUiAttached(U ui) {
    }

    protected void onUiDetached(U ui) {
    }

    protected synchronized final void populateUis() {
        for (U ui : mUis) {
            populateUi(ui);
        }
    }

    //创建回调方法
    protected abstract UC createUiCallbacks(U ui);

    protected int getId(U ui) {
        return ui.hashCode();
    }

    protected synchronized U findUi(final int id) {
        for (U ui : mUis) {
            if (getId(ui) == id) {
                return ui;
            }
        }
        return null;
    }

    protected void populateUi(U ui) {
    }

    public void setApplicationState(ApplicationState applicationState) {
        this.applicationState = applicationState;
    }

    //判断用户是否登录
    protected boolean hasLogined() {
        return applicationState.getLoginedUser() != null && applicationState.getLoginedUser().getIsLogin();
    }

    //获取user信息
    protected User getLoginedUser() {
        return applicationState.getLoginedUser();
    }
}
