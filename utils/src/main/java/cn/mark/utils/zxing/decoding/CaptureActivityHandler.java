package cn.mark.utils.zxing.decoding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.Vector;
import cn.mark.utils.R;
import cn.mark.utils.zxing.CaptureActivity;
import cn.mark.utils.zxing.camera.CameraManager;
import cn.mark.utils.zxing.view.ViewfinderResultPointCallback;


/***
 * @author marks.luo
 * @Description: TODO(This class handles all the messaging which comprises the state machine for capture.)
 * @date:2017-04-17 11:24
 */
public class CaptureActivityHandler extends Handler {
    private static final String TAG = CaptureActivityHandler.class.getSimpleName();

    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(CaptureActivity activity, Vector<BarcodeFormat> decodeFormats,
                                  String characterSet) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
                new ViewfinderResultPointCallback(activity.getViewfinderView()));
        decodeThread.start();
        state = State.SUCCESS;
        // Start ourselves capturing previews and decoding.
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what > 0) {
            int what = message.what;
            if (R.id.auto_focus == what) {
                //Log.d(TAG, "Got auto-focus message");
                // When one auto focus pass finishes, start another. This is the closest thing to
                // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
                if (state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
                }
            } else if (R.id.restart_preview == what) {
                Log.d(TAG, "Got restart preview message");
                restartPreviewAndDecode();
            } else if (R.id.decode_succeeded == what) {
                Log.d(TAG, "Got decode succeeded message");
                state = State.SUCCESS;
                Bundle bundle = message.getData();

                /***********************************************************************/
                Bitmap barcode = bundle == null ? null :
                        (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);//���ñ����߳�

                activity.handleDecode((Result) message.obj, barcode);//���ؽ��
                /***********************************************************************/
            } else if (R.id.decode_failed == what) {
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            } else if (R.id.return_scan_result == what) {
                Log.d(TAG, "Got return scan result message");
                activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.finish();
            } else if (R.id.launch_product_query == what) {
                Log.d(TAG, "Got product query message");
                String url = (String) message.obj;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                activity.startActivity(intent);
            }
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
//             continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            activity.drawViewfinder();
        }
    }
}
