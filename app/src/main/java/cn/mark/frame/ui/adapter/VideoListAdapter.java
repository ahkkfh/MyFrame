package cn.mark.frame.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.mark.videoplay.JCVideoPlayer;
import com.mark.videoplay.JCVideoPlayerStandard;

import cn.mark.frame.R;
import cn.mark.frame.ui.VideoConstant;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-11 09:56
 */
public class VideoListAdapter extends BaseAdapter {
    private Context mContext;
    public static final String TAG = "JieCaoVideoPlayer";

    private int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private int pager = -1;

    public VideoListAdapter(Context context) {
        mContext = context;
    }

    public VideoListAdapter(Context context, int pager) {
        this.mContext = context;
        this.pager = pager;
    }


    @Override
    public int getCount() {
        return pager == -1 ? videoIndexs.length : 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_video_layout, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
        if (pager == -1) {
            viewHolder.jcVideoPlayer.setUp(
                    VideoConstant.videoUrls[0][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    VideoConstant.videoTitles[0][position]);
            System.out.println("fdsfdsfdsfdsfa setup " + position);
            Glide.with(convertView.getContext())
                    .load(VideoConstant.videoThumbs[0][position])
                    .into(viewHolder.jcVideoPlayer.thumbImageView);
        } else {
            viewHolder.jcVideoPlayer.setUp(
                    VideoConstant.videoUrls[pager][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    VideoConstant.videoTitles[pager][position]);
            Glide.with(convertView.getContext())
                    .load(VideoConstant.videoThumbs[pager][position])
                    .into(viewHolder.jcVideoPlayer.thumbImageView);
        }
        return convertView;
    }

    class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
