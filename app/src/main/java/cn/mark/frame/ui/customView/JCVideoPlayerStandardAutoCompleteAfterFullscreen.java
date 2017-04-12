package cn.mark.frame.ui.customView;

import android.content.Context;
import android.util.AttributeSet;

import com.mark.videoplay.JCVideoPlayerStandard;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-10 16:10
 */
public class JCVideoPlayerStandardAutoCompleteAfterFullscreen extends JCVideoPlayerStandard {
    public JCVideoPlayerStandardAutoCompleteAfterFullscreen(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardAutoCompleteAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            setUiWitStateAndScreen(CURRENT_STATE_AUTO_COMPLETE);
        } else {
            super.onAutoCompletion();
        }
    }
}
