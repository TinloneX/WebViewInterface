package org.tinlone.demo.webinteface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TwoActivity extends AppCompatActivity {

    private X5WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        webview = (X5WebView) findViewById(R.id.x5_web);
        webview.loadUrl("https://www.baidu.com");
    }


}
