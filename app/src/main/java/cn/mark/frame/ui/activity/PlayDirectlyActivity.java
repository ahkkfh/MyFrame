package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.mark.videoplay.JCVideoPlayer;
import com.mark.videoplay.JCVideoPlayerStandard;

import java.util.concurrent.TimeUnit;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityDirectlyPlayBinding;
import cn.mark.utils.Constant;
import rx.functions.Action1;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-10 15:00
 */
public class PlayDirectlyActivity extends BaseActivity {
    private ActivityDirectlyPlayBinding mBinding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("PlayDirectlyWithoutLayout");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_directly_play);
        initView();
    }

    @Override
    protected void initView() {
        RxView.clicks(mBinding.fullscreen).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                JCVideoPlayerStandard.startFullscreen(PlayDirectlyActivity.this, JCVideoPlayerStandard.class,
                        "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "嫂子辛苦了");
            }
        });
        RxView.clicks(mBinding.tinyWindow).throttleFirst(Constant.defaultClickTime, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(PlayDirectlyActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
