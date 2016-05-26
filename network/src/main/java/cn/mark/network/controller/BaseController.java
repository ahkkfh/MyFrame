package cn.mark.network.controller;


import android.content.Intent;

import cn.mark.network.Display;


/**
 * Created by yaoping on 2016/5/19.
 * 该类主要是对数据层进行一些操作。
 */
public abstract class BaseController {
    private Display mDisplay;
    private boolean mInited;

    public final void init() {
        mInited = true;
        onInited();
    }

    public final void suspend() {
        onSuspended();
        mInited = false;
    }

    //是否暂停
    protected void onSuspended() {
    }

    public boolean handleIntent(Intent intent) {
        return false;
    }

    //维护初始化
    protected final void assertInited() {
    }

    //获取跳转类
    public Display getDisplay() {
        return mDisplay;
    }

    //设置跳转类
    public void setDisplay(Display display) {
        mDisplay = display;
    }
    public boolean isInited() {
        return mInited;
    }

    protected abstract void onInited();
}
