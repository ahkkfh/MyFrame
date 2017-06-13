package com.Lyp.bigimageview.bigImageView.view;

/**
 * Created by Ruoly on 2017/6/11.
 */

public abstract class ProgressNotifyRunnable implements Runnable {
    private int mProgress = -1;

    public boolean update(int progress) {
        boolean notified = mProgress == -1;
        this.mProgress = progress;
        return notified;
    }

    public void notified() {
        mProgress = -1;
    }
}
