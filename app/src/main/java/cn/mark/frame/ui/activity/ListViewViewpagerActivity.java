package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mark.videoplay.JCVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityViewpagerBinding;
import cn.mark.frame.ui.adapter.VideoListAdapter;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-11 09:48
 */
public class ListViewViewpagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ActivityViewpagerBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("viewpager");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listview_vp);
        initView();
    }

    @Override
    protected void initView() {
        List<View> listViews = new ArrayList<>();
        ListView listView1 = (ListView) getLayoutInflater().inflate(R.layout.layout_list, null);
        ListView listView2 = (ListView) getLayoutInflater().inflate(R.layout.layout_list, null);
        ListView listView3 = (ListView) getLayoutInflater().inflate(R.layout.layout_list, null);

        listView1.setAdapter(new VideoListAdapter(this, 0));
        listView2.setAdapter(new VideoListAdapter(this, 1));
        listView3.setAdapter(new VideoListAdapter(this, 2));

        listViews.add(listView1);
        listViews.add(listView2);
        listViews.add(listView3);


        MyAdapter myAdapter = new MyAdapter(listViews);
        mBinding.listViewViewpager.setAdapter(myAdapter);
        mBinding.listViewViewpager.setOnPageChangeListener(this);
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


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyAdapter extends PagerAdapter {
        List<View> mList;

        public MyAdapter(List<View> lists) {
            mList = lists;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mList.get(position), 0);
            return mList.get(position);
        }
    }
}
