package longimage.bigImageView.view;
/***
 * @author marks.luo
 * @Description: TODO(进度改变通知)
 * @date:2017-06-14 11:55
 */
abstract class ProgressNotifyRunnable implements Runnable {
    protected int mProgress = -1;

    public boolean update(int progress) {
        boolean notified = mProgress == -1;
        mProgress = progress;
        return notified;
    }

    public void notified() {
        mProgress = -1;
    }
}
