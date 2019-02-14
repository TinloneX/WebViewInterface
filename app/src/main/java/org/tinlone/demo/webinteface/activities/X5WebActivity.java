package org.tinlone.demo.webinteface.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import org.tinlone.demo.webinteface.R;
import org.tinlone.demo.webinteface.X5WebView;

public class X5WebActivity extends AppCompatActivity {

    private X5WebView webview;
    private long t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        webview = findViewById(R.id.x5_web);
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
        Log.w("qqqqq", "http://api.maowankeji.com/h5/fallGame.jsp?channelCode=LH-JZDK-01");
        Log.w("qqqqq", String.valueOf(t1 = System.currentTimeMillis()));
        webview.loadUrl("http://api.maowankeji.com/h5/fallGame.jsp?channelCode=LH-JZDK-01");
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i==100){
                    Log.w("qqqqqqqq", String.format("%s加载完成,总用时：%s",
                            System.currentTimeMillis(), System.currentTimeMillis() - t1));
                }
            }
        });
    }

    /**
     * 启用硬件加速
     */
    private void initHardwareAccelerate() {
        try {
            getWindow()
                    .setFlags(
                            android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                            android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        } catch (Exception ignored) {
        }
    }

}
