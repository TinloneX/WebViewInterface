package org.tinlone.demo.webinteface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;

public class TwoActivity extends AppCompatActivity {

    private X5WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        webview = (X5WebView) findViewById(R.id.x5_web);
        initHardwareAccelerate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSettings();
    }

    private void initSettings() {
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);
        //下面方法去掉
        IX5WebViewExtension ix5 = webview.getX5WebViewExtension();
        if (null != ix5) {
            ix5.setScrollBarFadingEnabled(false);
        }
        webview.loadUrl("https://www.baidu.com/");
        webview.setWebChromeClient(new MyWebClient());
    }

    /**
     * 启用硬件加速
     */
    private void initHardwareAccelerate() {
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
    }

}
