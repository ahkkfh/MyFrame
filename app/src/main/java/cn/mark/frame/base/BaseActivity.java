package cn.mark.frame.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import cn.mark.frame.R;
import cn.mark.utils.StatusBarUtil;

/**
 * Created by yaoping on 2016/6/22.
 * BaseActivity
 */
public class BaseActivity extends AutoLayoutActivity {
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
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

    protected void initView() {
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    protected void back() {
        setBackButtonVisibility(View.VISIBLE);
    }

    protected void setBaseHeadBG(int colorId) {
        RelativeLayout base_head_layout = (RelativeLayout) findViewById(R.id.base_head_layout);
        base_head_layout.setBackgroundColor(colorId);
    }

    protected void setBackButtonVisibility(int visibility) {
        ImageView base_back_img = (ImageView) findViewById(R.id.base_back_img);
        base_back_img.setVisibility(visibility);
        base_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setHeadTitle(String title) {
        TextView base_head_title = (TextView) findViewById(R.id.base_head_title);
        base_head_title.setText(title);
    }

    protected void setHeadTitle(int resId) {
        setHeadTitle(getString(resId));
    }

    protected void setHeadTitleColor(int colorId) {
        TextView base_head_title = (TextView) findViewById(R.id.base_head_title);
        base_head_title.setTextColor(colorId);
    }

    protected void setHeadTitleAndBackground(String title, int colorId) {
        setHeadTitle(title);
        setBaseHeadBG(colorId);
    }
}
