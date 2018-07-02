package org.tinlone.demo.webinteface;

import android.util.Log;

import com.tencent.smtt.sdk.WebChromeClient;

public class MyWebClient extends WebChromeClient {

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
