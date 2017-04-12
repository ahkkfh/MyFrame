package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.mark.videoplay.JCVideoPlayer;
import com.mark.videoplay.JCVideoPlayerStandard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityListviewContentBinding;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-11 16:14
 */
public class AutoTinyNormalActivity extends BaseActivity {
    private ActivityListviewContentBinding mBinding;
    LinearLayout headLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("AutoTinyNormal");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listview_content);
        initView();
    }

    @Override
    protected void initView() {
        headLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head_auto_tiny_normal, null);
        mBinding.listview.addHeaderView(headLayout);

        JCVideoPlayerStandard standard = (JCVideoPlayerStandard) headLayout.findViewById(R.id.jc_video);
        standard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子坐这");
        Glide.with(this).load("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg")
                .into(standard.thumbImageView);

        Map<String, String> keyValuePair = new HashMap<>();
        keyValuePair.put("key", "list item");
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(keyValuePair);
        }

        ListAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_1, new String[]{"key"}, new int[]{android.R.id.text1});

        mBinding.listview.setAdapter(adapter);
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
