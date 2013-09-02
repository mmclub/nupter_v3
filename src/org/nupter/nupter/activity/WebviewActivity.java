package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Log;

/**
 * 载入一个网页的Activity
 * <p/>
 * 目前作用是为手机报具体内容提供服务
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
public class WebviewActivity extends Activity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);


        setContentView(R.layout.activity_webview);

        WebView webView = (WebView) findViewById(R.id.webView);


        WebSettings wbset=webView.getSettings();
        wbset.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                WebviewActivity.this.setTitle("努力载入中...");
                WebviewActivity.this.setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100)
                    setTitle(getIntent().getStringExtra(EXTRA_TITLE));
            }
        });


        String url = getIntent().getStringExtra(EXTRA_URL);
        webView.loadUrl(url);


        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }
}