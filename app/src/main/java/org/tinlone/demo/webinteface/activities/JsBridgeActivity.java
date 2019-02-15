package org.tinlone.demo.webinteface.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.SnackbarUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import org.tinlone.demo.webinteface.R;

public class JsBridgeActivity extends AppCompatActivity {

    private View rootView;
    private Button java2JsDefault;
    private Button java2JsSpec;
    private BridgeWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_bridge);
        initView();
        webView.loadUrl("file:///android_asset/html/jsbridge.html");
        java2JsDefault.setOnClickListener(v ->
                webView.send("这是Native数据给js默认接收",
                        data -> SnackbarUtils.with(rootView).setMessage(data).showSuccess()));
        java2JsSpec.setOnClickListener(v ->
                webView.callHandler("functionInJs", "这是Native数据给js指定接收",
                        data -> SnackbarUtils.with(rootView).setMessage(data).showWarning()));
        //默认接收
        webView.setDefaultHandler((data, function) -> {
            String msg = "默认接收到js的数据：" + data;
            SnackbarUtils.with(rootView).setMessage(msg).showSuccess();
            function.onCallBack("Native默认接收完毕，并回传数据给js"); //回传数据给js
        });
        //指定接收 submitFromWeb 与js保持一致
        webView.registerHandler("submitFromWeb", (data, function) -> {
            String msg = "指定接收到js的数据：" + data;
            SnackbarUtils.with(rootView).setMessage(msg).showWarning();
            function.onCallBack("Native指定接收完毕，并回传数据给js"); //回传数据给js
        });
    }

    private void initView() {
        rootView = findViewById(R.id.root_view);
        webView = findViewById(R.id.webView);
        java2JsSpec = findViewById(R.id.java_to_js_spec);
        java2JsDefault = findViewById(R.id.java_to_js_default);
    }
}
