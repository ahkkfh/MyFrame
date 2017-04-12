package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.mark.videoplay.JCVideoPlayer;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityListviewContentBinding;
import cn.mark.frame.ui.adapter.VideoListAdapter;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-11 16:14
 */
public class AutoTinyListActivity extends BaseActivity {
    private ActivityListviewContentBinding mBinding;
    VideoListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("AutoTinyList");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listview_content);
        initView();
    }

    @Override
    protected void initView() {
        mAdapter = new VideoListAdapter(this);
        mBinding.listview.setAdapter(mAdapter);
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
