package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.AbsoluteLayout;

import com.bumptech.glide.Glide;
import com.mark.videoplay.JCUtils;
import com.mark.videoplay.JCVideoPlayer;
import com.mark.videoplay.JCVideoPlayerStandard;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityWebviewBinding;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-10 15:02
 */
public class WebViewActivity extends BaseActivity {
    private ActivityWebviewBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("AboutWebView");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        initView();
    }

    protected void initView() {
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.addJavascriptInterface(new JCCallBack(), "jcvd");
        mBinding.webview.loadUrl("file:///android_asset/jcvd.html");
    }

    public class JCCallBack {
        //html页面调用java代码
        @JavascriptInterface
        public void adViewJieCaoVideoPlayer(final int width, final int height, final int top, final int left, final int index) {
            if (index == 0) {
                JCVideoPlayerStandard standard = new JCVideoPlayerStandard(WebViewActivity.this);
                standard.setUp("http://video.jiecao.fm/11/16/c/68Tlrc9zNi3JomXpd-nUog__.mp4",
                        JCVideoPlayer.SCREEN_LAYOUT_LIST, "mark.ruo");
                //设置缩率图
                Glide.with(WebViewActivity.this).load("http://img4.jiecaojingxuan.com/2016/11/16/1d935cc5-a1e7-4779-bdfa-20fd7a60724c.jpg@!640_360")
                        .into(standard.thumbImageView);

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(params);
                layoutParams.y = JCUtils.dip2px(WebViewActivity.this, top);
                layoutParams.x = JCUtils.dip2px(WebViewActivity.this, left);
                layoutParams.height = JCUtils.dip2px(WebViewActivity.this, height);
                layoutParams.width = JCUtils.dip2px(WebViewActivity.this, width);
                mBinding.webview.addView(standard, layoutParams);
            } else {
                JCVideoPlayerStandard webVieo = new JCVideoPlayerStandard(WebViewActivity.this);
                webVieo.setUp("http://video.jiecao.fm/11/14/xin/%E5%90%B8%E6%AF%92.mp4",
                        JCVideoPlayer.SCREEN_LAYOUT_LIST, "mark.ruo");
                Glide.with(WebViewActivity.this).load("http://img4.jiecaojingxuan.com/2016/11/14/a019ffc1-556c-4a85-b70c-b1b49811d577.jpg@!640_360")
                        .into(webVieo.thumbImageView);
                ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                layoutParams.y = JCUtils.dip2px(WebViewActivity.this, top);
                layoutParams.x = JCUtils.dip2px(WebViewActivity.this, left);
                layoutParams.height = JCUtils.dip2px(WebViewActivity.this, height);
                layoutParams.width = JCUtils.dip2px(WebViewActivity.this, width);
                mBinding.webview.addView(webVieo, layoutParams);
            }
        }
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
