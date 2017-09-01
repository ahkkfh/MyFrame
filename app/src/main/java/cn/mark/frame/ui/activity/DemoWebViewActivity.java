package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityDemoWebviewBinding;
import cn.mark.utils.ToastUtil;
import cn.mark.utils.widget.ActionSelectListener;
import cn.mark.utils.widget.CustomActionWebView;

/***
 * @author marks.luo
 * @Description:()
 * @date:2017-09-01 10:27
 *
 */
public class DemoWebViewActivity extends BaseActivity {
    private ActivityDemoWebviewBinding mBinding;
    private CustomActionWebView mCustomActionWebView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo_webview);
        initView();
    }

    @Override
    protected void initView() {
        List<String> list = new ArrayList<>();
        list.add("Item1");
        list.add("Item2");
        list.add("APIWeb");
        mCustomActionWebView = (CustomActionWebView) findViewById(R.id.customActionWebView);
        mCustomActionWebView.setWebViewClient(new CustomWebViewClient());
        //设置item
        mCustomActionWebView.setActionList(list);

        //链接js注入接口，使能选中返回数据
        mCustomActionWebView.linkJSInterface();

        mCustomActionWebView.getSettings().setBuiltInZoomControls(true);
        mCustomActionWebView.getSettings().setDisplayZoomControls(false);
        //使用javascript
        mCustomActionWebView.getSettings().setJavaScriptEnabled(true);
        mCustomActionWebView.getSettings().setDomStorageEnabled(true);


        //增加点击回调
        mCustomActionWebView.setActionSelectListener(new ActionSelectListener() {
            @Override
            public void onClick(String title, String selectText) {
                if(title.equals("APIWeb")) {
                    ToastUtil.instance().show("显示更多");
                    return;
                }
                Toast.makeText(DemoWebViewActivity.this, "Click Item: " + title + "。\n\nValue: " + selectText, Toast.LENGTH_LONG).show();
            }
        });

        //加载url
        mCustomActionWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCustomActionWebView.loadUrl("http://www.jianshu.com/p/b32187d6e0ad");
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCustomActionWebView != null) {
            mCustomActionWebView.dismissAction();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private class CustomWebViewClient extends WebViewClient {

        private boolean mLastLoadFailed = false;

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            if (!mLastLoadFailed) {
                CustomActionWebView customActionWebView = (CustomActionWebView) webView;
                customActionWebView.linkJSInterface();
                mBinding.loadingView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mLastLoadFailed = true;
            mBinding.loadingView.setVisibility(View.GONE);
        }
    }
}
