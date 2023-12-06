package com.newmysmileQR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.newmysmileQR.R;

public class TermsConditionActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar pbWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setTitle("Terms & Condition");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void initView() {
        pbWeb = findViewById(R.id.pbWeb);
        mWebView = findViewById(R.id.mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mWebView.getSettings().setSupportMultipleWindows(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.loadUrl("https://mysmileqr.com/SchoolQR/schoolterms");

        mWebView.setWebChromeClient(new WebChromeClient() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 95) {

                    pbWeb.setVisibility(View.VISIBLE);
                } else {
                    pbWeb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

    }


}
