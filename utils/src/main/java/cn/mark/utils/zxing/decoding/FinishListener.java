package cn.mark.utils.zxing.decoding;

import android.app.Activity;
import android.content.DialogInterface;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-17 11:19
 */
public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {
    private final Activity activityToFinish;

    public FinishListener(Activity activityToFinish) {
        this.activityToFinish = activityToFinish;
    }

    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    public void run() {
        activityToFinish.finish();
    }

}
