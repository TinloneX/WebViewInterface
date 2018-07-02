package org.tinlone.demo.webinteface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.Toast;

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
        mWebView.addJavascriptInterface(new JsInterface(), keyToWeb);
        mWebView.loadUrl(htmlURL);
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
