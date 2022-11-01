package com.filepreview.application.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.filepreview.application.R;
import com.filepreview.application.constant.ConstValue;
import com.filepreview.application.databinding.ActivityBrowserBinding;

import java.lang.ref.SoftReference;

public class BrowserActivity extends BaseActivity<ActivityBrowserBinding> {

    private final BrowserHandler mHandler = new BrowserHandler(this);
    protected String mWebUrl = "";
    protected String mHtmlContent = "";

    public static void launch(Context context, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(ConstValue.BROWSER_URL, url);
        context.startActivity(intent);
    }

    public static void launchHtml(Context context, String html) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(ConstValue.HTML_CONTENT, html);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_browser;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void init() {
        WebSettings webSettings = mBinding.web.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);

        mBinding.web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d("BrowserActivity","newProgress : " + newProgress);
                mHandler.sendEmptyMessageDelayed(newProgress, 500);
            }
        });

        mBinding.web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.d("BrowserActivity","onReceivedSslError error : " + error);
            }
        });

        mWebUrl = getIntent().getStringExtra(ConstValue.BROWSER_URL);
        mHtmlContent = getIntent().getStringExtra(ConstValue.HTML_CONTENT);
        Log.d("BrowserActivity", "mWebUrl : " + mWebUrl);
        Log.d("BrowserActivity", "mHtmlContent : " + mHtmlContent);
        if (mWebUrl != null) {
            mBinding.web.loadUrl(mWebUrl);
        } else {
            mBinding.web.loadDataWithBaseURL(null, mHtmlContent, "text/html", "utf-8", null);
        }
//        mBinding.web.loadUrl("file:///android_asset/privacy_policy_zh.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBinding.web.canGoBack()) {
                mBinding.web.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class BrowserHandler extends Handler {

        private SoftReference<BrowserActivity> softReference;

        public BrowserHandler(BrowserActivity activity) {
            softReference = new SoftReference<>(activity);
        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int newProgress = msg.what;
            if (newProgress == 100) {
                softReference.get().mBinding.webProgress.setVisibility(View.GONE);
            } else {
                softReference.get().mBinding.webProgress.setProgress(newProgress);
            }
        }
    }
}