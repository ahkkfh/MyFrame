package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.mark.videoplay.JCVideoPlayer;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivitySmallChangeBinding;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-10 15:58
 */
public class UISmallChangeActivity extends BaseActivity {
    private ActivitySmallChangeBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("SmallChangeUI");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_small_change);
        initView();
    }

    protected void initView() {
        mBinding.customVideoplayerStandardWithShareButton.setUp("http://video.jiecao.fm/11/17/c/fxkR4gylyIZKeljem8xTvA__.mp4",
                JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "mark.ruo1");
        Glide.with(this).load("http://img4.jiecaojingxuan.com/2016/11/17/6fc2ae91-36e2-44c5-bb10-29ae5d5c678c.png@!640_360")
                .into(mBinding.customVideoplayerStandardWithShareButton.thumbImageView);

        mBinding.customVideoplayerStandardShowTitleAfterFullscreen.setUp("http://video.jiecao.fm/11/18/xu/%E6%91%87%E5%A4%B4.mp4",
                JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "mark.ruo2");
        Glide.with(this).load("http://img4.jiecaojingxuan.com/2016/11/18/f03cee95-9b78-4dd5-986f-d162c06c385c.png@!640_360")
                .into(mBinding.customVideoplayerStandardShowTitleAfterFullscreen.thumbImageView);

        mBinding.customVideoplayerStandardShowTextureviewAotoComplete.setUp("http://video.jiecao.fm/11/18/c/I-KpaMJ-HMDfAy6tX2Jfag__.mp4",
                JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "mark.ruo3");
        Glide.with(this).load("http://img4.jiecaojingxuan.com/2016/11/18/e7ea659f-c3d2-4979-9ea5-f993b05e5930.png@!640_360")
                .into(mBinding.customVideoplayerStandardShowTextureviewAotoComplete.thumbImageView);

        mBinding.customVideoplayerStandardShowTextureviewAotoComplete.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "mark.ruo4");
        Glide.with(this).load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(mBinding.customVideoplayerStandardShowTextureviewAotoComplete.thumbImageView);

        mBinding.jcVideoplayer11.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "mark.ruo5");
        Glide.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(mBinding.jcVideoplayer11.thumbImageView);
        mBinding.jcVideoplayer11.widthRatio = 1;
        mBinding.jcVideoplayer11.heightRatio = 1;


        mBinding.jcVideoplayer169.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "mark.ruo6");
        Glide.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(mBinding.jcVideoplayer169.thumbImageView);
        mBinding.jcVideoplayer169.widthRatio = 16;
        mBinding.jcVideoplayer169.heightRatio = 9;
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
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
