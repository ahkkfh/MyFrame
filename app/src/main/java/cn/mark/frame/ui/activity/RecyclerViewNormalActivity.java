package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import com.mark.videoplay.JCVideoPlayer;
import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityRecyclerviewContentBinding;
import cn.mark.frame.ui.adapter.RecyclerViewVideoAdapter;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-11 09:48
 */
public class RecyclerViewNormalActivity extends BaseActivity {
    private ActivityRecyclerviewContentBinding mBinding;
    private RecyclerViewVideoAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("NormalRecyclerView");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview_content);
        initView();
    }

    @Override
    protected void initView() {
        mBinding.recyleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewVideoAdapter(this);
        mBinding.recyleview.setAdapter(mAdapter);
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
