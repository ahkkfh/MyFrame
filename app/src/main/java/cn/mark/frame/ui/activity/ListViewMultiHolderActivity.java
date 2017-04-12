package cn.mark.frame.ui.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mark.videoplay.JCVideoPlayer;
import com.mark.videoplay.JCVideoPlayerStandard;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityListviewContentBinding;
import cn.mark.frame.ui.VideoConstant;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-11 09:48
 */
public class ListViewMultiHolderActivity extends BaseActivity {
    private ActivityListviewContentBinding mBinding;
    private VideoListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ListViewMultiHolder");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listview_content);
        initView();
    }

    @Override
    protected void initView() {
        mAdapter = new VideoListAdapter();
        mBinding.listview.setAdapter(mAdapter);
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

    private class VideoListAdapter extends BaseAdapter {
        int[] viewtype = {0, 0, 0, 1, 0, 0, 0, 1, 0, 0};//1 = jcvd, 0 = textView

        Context context;
        LayoutInflater mInflater;

        @Override
        public int getCount() {
            return viewtype.length;
        }

        @Override
        public Object getItem(int position) {
            return viewtype[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
                ((VideoHolder) convertView.getTag()).jcVideoPlayer.release();
            }
            if (getItemViewType(position) == 1) {
                VideoHolder videoHolder;
                if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
                    videoHolder = (VideoHolder) convertView.getTag();
                } else {
                    videoHolder = new VideoHolder();
                    convertView = mInflater.inflate(R.layout.item_video_layout, null);
                    videoHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
                    convertView.setTag(videoHolder);
                }
                videoHolder.jcVideoPlayer.setUp(VideoConstant.videoUrls[0][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        VideoConstant.videoTitles[0][position]);
                Glide.with(ListViewMultiHolderActivity.this).load(VideoConstant.videoThumbs[0][position])
                        .into(videoHolder.jcVideoPlayer.thumbImageView);
            } else {
                TextViewHolder textViewHolder;
                if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof TextViewHolder) {
                    textViewHolder = (TextViewHolder) convertView.getTag();
                } else {
                    textViewHolder = new TextViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(ListViewMultiHolderActivity.this);
                    convertView = inflater.inflate(R.layout.item_textview, null);
                    textViewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
                    convertView.setTag(textViewHolder);
                }
            }
            return convertView;
        }


        @Override
        public int getItemViewType(int position) {
            return viewtype[position];
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        class VideoHolder {
            JCVideoPlayerStandard jcVideoPlayer;
        }

        class TextViewHolder {
            TextView textView;
        }
    }
}
