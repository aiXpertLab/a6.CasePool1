package com.hypech.case84_webview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Date:2021/1/23
 * auther:Leo Reny@hypech.com
 */
@SuppressWarnings("FieldCanBeLocal")

public class Privacy extends Activity {

    private WebView mWV_disclaimer;
    private ProgressBar mCircleProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy);


        mWV_disclaimer = findViewById(R.id.WV_pure_course);
        mCircleProgress = findViewById(R.id.progress_Bar);
//        WebSettings mysettings = mWV_disclaimer.getSettings();
//        mysettings.setSupportZoom(true);
//        mysettings.setBuiltInZoomControls(true);
//        mWV_disclaimer.loadUrl(L0_Data.URL_PRIVACY);//URL需要改成隐私政策的
        mWV_disclaimer.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
/*        mWV_disclaimer.canGoBack();
        mWV_disclaimer.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mCircleProgress.setVisibility(View.GONE);
                } else {
                    mCircleProgress.setVisibility(View.VISIBLE);
                    mCircleProgress.setProgress(newProgress);
                }
            }
        });
    }

 */

    }
}