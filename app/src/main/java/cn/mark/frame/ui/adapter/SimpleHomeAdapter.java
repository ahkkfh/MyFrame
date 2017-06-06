package cn.mark.frame.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.mark.frame.R;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-25 15:54
 */
public class SimpleHomeAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mItems;
    private DisplayMetrics mDisplayMetrics;

    public SimpleHomeAdapter(Context mContext, String[] items) {
        this.mContext = mContext;
        this.mItems = items;
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.length;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.top_tab_list_item, null);
            viewHolder.top_tab_item_text = (TextView) convertView.findViewById(R.id.top_tab_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.top_tab_item_text.setText(mItems[position]);/*
        int padding = (int) (mDisplayMetrics.density * 10);
        TextView textView = new TextView(mContext);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        // tv.setGravity(Gravity.CENTER);
        textView.setPadding(padding, padding, padding, padding);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setText(mItems[position]);
        */
        return convertView;
    }

    private static class ViewHolder {
        TextView top_tab_item_text;
    }
}
