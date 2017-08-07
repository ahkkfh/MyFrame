package cn.mark.frame.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import longimage.photodrawable.CustomPagerAdapter;
import longimage.photodrawable.LoadingDraweeview;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-08-07 13:45
 *
 */
public class ViewPageAdapter extends CustomPagerAdapter {
    private ArrayList<String> mUrlList;
    private LoadingDraweeview mCurrentView;
    private Context mContext;

    public ViewPageAdapter(ArrayList<String> urlList, Context context) {
        mUrlList = urlList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mUrlList == null ? 0 : mUrlList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (LoadingDraweeview) object;
    }

    @Override
    public boolean isViewFromObject(View var1, Object var2) {
        return var1 == var2;
    }

    public LoadingDraweeview getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final LoadingDraweeview loadingDraweeview = new LoadingDraweeview(mContext, mUrlList.get(position));
        try {
            container.addView(loadingDraweeview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loadingDraweeview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) mContext).finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadingDraweeview;
    }
}
