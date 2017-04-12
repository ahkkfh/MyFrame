package cn.mark.frame.ui.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mark.videoplay.JCVideoPlayerStandard;

import cn.mark.frame.R;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-10 16:12
 */
public class JCVideoPlayerStandardShowShareButtonAfterFullscreen extends JCVideoPlayerStandard {
    public ImageView shareButton;

    public JCVideoPlayerStandardShowShareButtonAfterFullscreen(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardShowShareButtonAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void init(Context context) {
        super.init(context);
        shareButton = (ImageView) findViewById(R.id.share);
        shareButton.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_standard_with_share_button;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.share) {
            Toast.makeText(getContext(), "Whatever the icon means", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            shareButton.setVisibility(View.VISIBLE);
        } else {
            shareButton.setVisibility(View.INVISIBLE);
        }
    }
}
