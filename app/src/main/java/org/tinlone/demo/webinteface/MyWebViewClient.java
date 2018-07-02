package org.tinlone.demo.webinteface;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String url) {
        Log.i("TAG", "shouldOverrideUrlLoading----1: " + url);
        if (url.startsWith("abc:")) {
            Toast.makeText(webView.getContext(), "" + url, Toast.LENGTH_SHORT).show();
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
