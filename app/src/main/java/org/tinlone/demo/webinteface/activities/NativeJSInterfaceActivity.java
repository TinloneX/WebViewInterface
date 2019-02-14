package org.tinlone.demo.webinteface.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;

import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.tinlone.demo.webinteface.R;
import org.tinlone.demo.webinteface.X5WebView;

import static org.tinlone.demo.webinteface.R.id.webview;

@SuppressLint("SetJavaScriptEnabled")
public class NativeJSInterfaceActivity extends AppCompatActivity implements View.OnClickListener {

    @Keep
    public static final String keyToWeb = "Interface";
    @Keep
    private static final String INJECT_JAVA_SCRIPT =
            "javascript:function feedback() { " +
                    " var input = document.getElementsByTagName('input'); " +
                    " for(var i = 0; i < input.length; i++) { " +
                    "    console.log(input[i].value); " +
//                    "    if(input[i].value.length==11){" +
                    "       window.Interface.toAndroid(input[i].value);" +
                    "       window.location.href='WebInterface://'+input[i].value;" +
//                    "    }" +
                    " } " +
                    "}; " +
                    "feedback();";
    @Keep
//    private static final String htmlURL = "file:///android_asset/html/test.html";
    private final String htmlURL = "http://api.maowankeji.com/h5/fallGame.jsp?channelCode=LH-JZDK-01";

    private static String message = "Default Message !";

    private X5WebView mWebView;

    private Button hello;
    private Button world;
    private Button button;
    private boolean completed;
    private long t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_js);
        initView();
        setListeners();
    }

    private void setListeners() {
        hello.setOnClickListener(this);
        world.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSetting();
    }

    private void initView() {
        mWebView = findViewById(webview);
        hello = findViewById(R.id.hello);
        world = findViewById(R.id.world);
        button = findViewById(R.id.button);
    }

    private void initSetting() {
        mWebView.addJavascriptInterface(new JsInterface(), keyToWeb);
        Log.w("qqqqq", "http://api.maowankeji.com/h5/fallGame.jsp?channelCode=LH-JZDK-01");
        Log.w("qqqqq", String.valueOf(t1 = System.currentTimeMillis()));
        mWebView.loadUrl(htmlURL);
        mWebView.setWebChromeClient(new MyWebClient());
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void onClick(View v) {
        doClick("" + ((Button) v).getText().toString());
    }

    @SuppressLint("JavascriptInterface")
    private class JsInterface {
        @JavascriptInterface
        public void sendInfoToAndroid(String obj) {
            SnackbarUtils.with(mWebView)
                    .setMessage(obj)
                    .showSuccess();
        }

        @JavascriptInterface
        public String getDataFromAndroid() {
            return message;
        }

        public void toAndroid(String input) {
            SnackbarUtils.with(mWebView)
                    .setMessage("toAndroid:" + input)
                    .showSuccess();
        }

    }

    private void doClick(final String tag) {
        message = tag;
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:click_button2('" + tag + "')");
            }
        });

        if (getString(R.string.button).equals(tag)) {
            startActivity(new Intent(NativeJSInterfaceActivity.this, X5WebActivity.class));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
        super.onBackPressed();
    }

    private class MyWebClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            completed = i>=95;
            if (i==100){
                Log.w("qqqqqqqq", String.format("%s加载完成,总用时：%s",
                        System.currentTimeMillis(), System.currentTimeMillis() - t1));
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            ToastUtils.showLong(s);
            if (s.startsWith("WebInterface://")){
                SnackbarUtils.with(webView).setMessage(s).showSuccess();
            }
            if (completed){
                mWebView.post(() -> mWebView.loadUrl(INJECT_JAVA_SCRIPT));
            }
            return super.shouldOverrideUrlLoading(webView, s);
        }
    }
}
