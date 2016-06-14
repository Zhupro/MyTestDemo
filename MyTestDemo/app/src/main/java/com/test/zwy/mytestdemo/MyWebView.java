package com.test.zwy.mytestdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by FERO010 on 2016/6/13.
 */
public class MyWebView extends AppCompatActivity {
    WebView webview;
    Button httpURLConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        init();
    }

    private void init() {
        webview = (WebView) findViewById(R.id.webview);
        httpURLConnection = (Button) findViewById(R.id.httpURLConnection);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 根据传⼊入的参数再去加载新的⺴⽹网⻚页
                return true; // 表⽰示当前WebView可以处理打开新⺴⽹网⻚页的请求，不⽤用借助系统浏览器
            }
        });
        webview.loadUrl("http://www.baidu.com");

        httpURLConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyWebView.this,HttpURlConnection.class);
                startActivity(i);
            }
        });
    }
}
