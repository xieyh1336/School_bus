package com.example.school_bus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.example.school_bus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    private String Url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Url = intent.getStringExtra("Url");
        initWeb();
    }

    public void initWeb(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        //设置WebView的客户端
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        //让WebView可以执行javaScript
        webSettings.setJavaScriptEnabled(true);
        //让javaScript可以自动打开Windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置缓存
        webSettings.setAppCacheEnabled(true);
        //设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置缓存路径
//        webSettings.setAppCachePath("");
        //支持缩放（适配到当前屏幕）
        webSettings.setSupportZoom(true);
        //将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        //支持内容重新布局，一共有四种方式
        //默认是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        //设置默认字体大小
        webSettings.setDefaultFontSize(12);

        //加载网页
        webView.loadUrl(Url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            if (webView.canGoBack()){
                webView.goBack();
                return true;
            }else {
                finish();
            }
        }
        return false;
    }
}
