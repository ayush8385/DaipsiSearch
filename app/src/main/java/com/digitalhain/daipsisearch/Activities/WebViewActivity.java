package com.digitalhain.daipsisearch.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.digitalhain.daipsisearch.R;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        progrees = findViewById(R.id.progrees);
        progrees.setVisibility(View.GONE);
        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.loadUrl("https://daipsi.com/blogs/");

    }

}