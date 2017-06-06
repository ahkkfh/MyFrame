package cn.mark.frame.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityTopTabBinding;
import cn.mark.frame.ui.adapter.SimpleHomeAdapter;

/***
 * @author marks.luo
 * @Description: TODO(顶部显示Tab的activity)
 * @date:2017-04-25 14:31
 */
public class TopTabActivity extends BaseActivity {
    private String[] mItems = new String[]{"SlidingTabLayout", "CommonTabLayout", "SegmentTabLayout"};
    private final Class<?>[] mClasses = {SlidingTabActivity.class, CommonTabActivity.class,
            SegmentTabActivity.class};
    private ActivityTopTabBinding mTabBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabBinding = DataBindingUtil.setContentView(this, R.layout.activity_top_tab);
        initView();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mTabBinding.topTabBar);
        getSupportActionBar().setTitle("TopTab");
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTabBinding.listview.setAdapter(new SimpleHomeAdapter(this, mItems));
        mTabBinding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(TopTabActivity.this, mClasses[position]));
            }
        });
    }

    private View initContentView() {
        ListView lv = new ListView(this);
        lv.setCacheColorHint(getResources().getColor(R.color.color_02D1B1));
        lv.setBackgroundColor(getResources().getColor(R.color.white));
        lv.setFadingEdgeLength(0);
//        lv.setAdapter();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(TopTabActivity.this, mClasses[position]));
            }
        });
        return lv;
    }
}
