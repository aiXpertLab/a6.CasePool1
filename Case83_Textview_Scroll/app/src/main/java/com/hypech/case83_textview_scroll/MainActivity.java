package com.hypech.case83_textview_scroll;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();

    TextView tv;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "启动.");

        tv = findViewById(R.id.text_view);
        tv.setMovementMethod(new ScrollingMovementMethod());

    }

    public void myClick(View view){
        tv.setTextColor(Color.BLUE);
        tv.setGravity(Gravity.BOTTOM);
        text = "text  with the given element E. ";
        tv.append(text);

        WebView webView = (WebView) findViewById(R.id.webView);  ;
        String string ="用本应用前点击<a href=\"https://seeingvoice.gitee.io/privacy/\">《隐私政";
        Spannable sp = new SpannableString(Html.fromHtml(string));
        Linkify.addLinks(sp, Linkify.ALL);
        final String html = "<body>" + Html.toHtml(sp) + "</body>";

    }
}