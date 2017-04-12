package cn.mark.frame.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mark.videoplay.JCVideoPlayer;
import com.mark.videoplay.JCVideoPlayerStandard;

import cn.mark.frame.R;
import cn.mark.frame.ui.VideoConstant;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-11 14:33
 */
public class RecyclerViewVideoAdapter extends RecyclerView.Adapter<RecyclerViewVideoAdapter.MyViewHolder> {
    private Context mContext;
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final String TAG = "RecyclerViewVideoAdapter";

    public RecyclerViewVideoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_video_layout, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.jcVideoPlayer.setUp(
                VideoConstant.videoUrls[0][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                VideoConstant.videoTitles[0][position]);
        Glide.with(holder.jcVideoPlayer.getContext())
                .load(VideoConstant.videoThumbs[0][position])
                .into(holder.jcVideoPlayer.thumbImageView);
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;

        public MyViewHolder(View itemView) {
            super(itemView);
            jcVideoPlayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
        }
    }

}
