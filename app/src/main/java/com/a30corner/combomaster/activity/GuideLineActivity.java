package com.a30corner.combomaster.activity;

import com.a30corner.combomaster.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GuideLineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);
		mWebView = (WebView) findViewById(R.id.webview);

		mWebView.setWebViewClient(mWebViewClient);
		mWebView.loadUrl("http://188.166.227.62/combomaster/guideline/?lang="+getString(R.string.lang));
	}

	WebView mWebView = null;

	WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};
}
