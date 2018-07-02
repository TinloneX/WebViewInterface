package org.tinlone.demo.webinteface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;


import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebViewClient;

import static org.tinlone.demo.webinteface.R.id.webview;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Keep
    public static final String keyToWeb = "Interface";
    @Keep
    private final String htmlURL = "file:///android_asset/html/test.html";
//    private final String htmlURL = "http://192.168.1.11:3004/mobile/NewActive";

    private static String message = "Default Message !";

    private X5WebView mWebView;

    private Button hello;
    private Button world;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mWebView = (X5WebView) findViewById(webview);
        hello = (Button) findViewById(R.id.hello);
        world = (Button) findViewById(R.id.world);
        button = (Button) findViewById(R.id.button);
    }

    private void initSetting() {
        com.tencent.smtt.sdk.WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JsInterface(), keyToWeb);
        mWebView.loadUrl(htmlURL);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebClient());
    }

    @Override
    public void onClick(View v) {
        doClick("" + ((Button) v).getText().toString());
    }

    @SuppressLint("JavascriptInterface")
    private class JsInterface {
        @JavascriptInterface
        public void sendInfoToAndroid(String obj) {
            Toast.makeText(MainActivity.this, "" + obj, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String getDataFromAndroid() {
            return message;
        }

    }

    private void doClick(final String tag) {
        message = tag;
        mWebView.post(new Runnable() {
            @Override
            public void run() {
//                mWebView.loadUrl("javascript:click_button()");
                mWebView.loadUrl("javascript:click_button2('" + tag + "')");
            }
        });

        if (getString(R.string.button).equals(tag)) {
            startActivity(new Intent(MainActivity.this, TwoActivity.class));
        }
    }

    private class MyWebClient extends WebChromeClient {
        @Override
        public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
            super.onProgressChanged(webView, i);
        }

        @Override
        public boolean onJsPrompt(com.tencent.smtt.sdk.WebView webView, String s, String s1, String s2, com.tencent.smtt.export.external.interfaces.JsPromptResult jsPromptResult) {
            Log.i("TAG", "onJsPrompt: message = " + s1);
            return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
        }

        @Override
        public boolean onJsAlert(com.tencent.smtt.sdk.WebView webView, String s, String s1, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
            Log.i("TAG", "onJsAlert: message = " + s1);
            return super.onJsAlert(webView, s, s1, jsResult);
        }

        @Override
        public boolean onJsConfirm(com.tencent.smtt.sdk.WebView webView, String s, String s1, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
            Log.i("TAG", "onJsConfirm: message = " + s1);
            return super.onJsConfirm(webView, s, s1, jsResult);
        }

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String url) {
            Log.i("TAG", "shouldOverrideUrlLoading----1: " + url);
            if (url.startsWith("abc:")) {
                Toast.makeText(MainActivity.this, "" + url, Toast.LENGTH_SHORT).show();
                return true;
            }
            if ((url.toLowerCase().startsWith("http://")) || (url.toLowerCase().startsWith("https://"))) {
                webView.loadUrl(url);
                return true;
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest request) {
           /*
直接用有个坑，targetApi --- LOLLIPOP
view.loadUrl(request.getUrl().toString());
return true ;会拦截shouldOverrideUrlLoading(WebView, String)方法，但此方法内部实际还是调用的shouldOverrideUrlLoading(WebView, String)
*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.i("TAG", "shouldOverrideUrlLoading----2: " + request.getUrl().toString());
                webView.loadUrl(request.getUrl().toString());
                return true;
            } else {
                Log.i("TAG", "shouldOverrideUrlLoading----2: " + request.toString());
                return false;
            }
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
}
