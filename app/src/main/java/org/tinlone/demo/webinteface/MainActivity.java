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
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import tbsplus.tbs.tencent.com.tbsplus.TbsPlus;

import static org.tinlone.demo.webinteface.R.id.webview;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Keep
    public static final String keyToWeb = "Interface";
    @Keep
    private final String htmlURL = "file:///android_asset/html/test.html";
//    private final String htmlURL = "http://192.168.1.11:3004/mobile/NewActive";

    private static String message = "Default Message !";

    private WebView mWebView;

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
        mWebView = (WebView) findViewById(webview);
        hello = (Button) findViewById(R.id.hello);
        world = (Button) findViewById(R.id.world);
        button = (Button) findViewById(R.id.button);
    }

    private void initSetting() {
        WebSettings settings = mWebView.getSettings();
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
//            startActivity(new Intent(MainActivity.this, TwoActivity.class));
            TbsPlus.openUrl(this,"https://www.baidu.com");
        }
    }

    private class MyWebClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.i("TAG", "onJsPrompt: message = " + message);
//            return false;
//            result.confirm("OK");
//            return true;
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.i("TAG", "onJsAlert: message = " + message);
//            return true;
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.i("TAG", "onJsConfirm: message = " + message);
            return super.onJsConfirm(view, url, message, result);
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("TAG", "shouldOverrideUrlLoading----1: " + url);
            if (url.startsWith("abc:")) {
                Toast.makeText(MainActivity.this, "" + url, Toast.LENGTH_SHORT).show();
                return true;
            }
            if ((url.toLowerCase().startsWith("http://")) || (url.toLowerCase().startsWith("https://"))) {
                view.loadUrl(url);
                return true;
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
/*
直接用有个坑，targetApi --- LOLLIPOP
view.loadUrl(request.getUrl().toString());
return true ;会拦截shouldOverrideUrlLoading(WebView, String)方法，但此方法内部实际还是调用的shouldOverrideUrlLoading(WebView, String)
*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.i("TAG", "shouldOverrideUrlLoading----2: " + request.getUrl().toString());
                view.loadUrl(request.getUrl().toString());
                return true;
            } else {
                Log.i("TAG", "shouldOverrideUrlLoading----2: " + request.toString());
                return false;
            }
//            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

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
