package cn.mark.frame.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.utils.Constant;
import cn.mark.utils.ToastUtil;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-06-05 16:52
 */
public class VideoViewPlayActivity extends BaseActivity {
    private String path;
    private Button try_shoot_video;
    private TextView jump_home;
    private VideoView mVideoView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_play);
        path = "android.resource://" + getPackageName() + "/" + R.raw.guide;
        initLayout();
    }

    private void initLayout() {
        try_shoot_video = (Button) findViewById(R.id.try_shoot_video);
        jump_home = (TextView) findViewById(R.id.jump_home);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        MediaController mediacontroller = new MediaController(VideoViewPlayActivity.this);
        mediacontroller.setVisibility(View.GONE);
        mediacontroller.setAnchorView(mVideoView);
        Uri video = Uri.parse(path);
        mVideoView.setMediaController(mediacontroller);
        mVideoView.setVideoURI(video);
        mVideoView.start();
        mVideoView.requestFocus();
        RxView.clicks(try_shoot_video).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.instance().show("试拍一下");
            }
        });
        RxView.clicks(jump_home).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.instance().show("进入首页");
            }
        });
    }
}
