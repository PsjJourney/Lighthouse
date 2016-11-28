package com.psj.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyWebViewClient extends WebViewClient {
	
	private Activity activity;
	private WebView webview;
	
	public MyWebViewClient(Activity activity, WebView webview) {
		super();
		this.activity = activity;
		this.webview = webview;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, final String url) {
		if (url.contains("tel:")) {

		}
		/**
		 * 普通支付页面
		 */
		else if (url.contains("pay.php")) {

		}
		else if (url.contains("return_apply.php?order_id=")) {

		}
		else if (url.contains("rights_apply.php?order_id=")) {

		}
		else if (url.contains("presale.php?id")||url.contains("good.php?id")||url.contains("bargain.php")||url.contains("seckill.php")||url.contains("crowdfunding.php")||url.contains("lottery.php")||url.contains("/chanping/")) {
			jump("商品详情", url, false, false);
		}
		else {
			return false;
		}
		return true;
	}

    @Override  
    public void onPageFinished(WebView view, String url) {  

        view.getSettings().setJavaScriptEnabled(true);  

        super.onPageFinished(view, url);  
        /**
         * 一元夺宝支付页面
         */
        if (url.contains("?orderid=")) {
			
			webview.loadUrl("javascript:appFromAndroid()");
			
		}
        /**
         * 众筹支付页面
         */
		if (url.contains("zc_order.php?id=")) {
			
			webview.loadUrl("javascript:appFromAndroid()");
			
		}
		/**
		 * 秒杀
		 */
		if (url.contains("seckill.php?")) {
			
			webview.loadUrl("javascript:clear_popup()");
			
		}

    }  

    @Override  
    public void onPageStarted(WebView view, String url, Bitmap favicon) {  
        view.getSettings().setJavaScriptEnabled(true);  

        super.onPageStarted(view, url, favicon);  
    }  

    @Override  
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  

        super.onReceivedError(view, errorCode, description, failingUrl);  

    }
    
    public void jump(String title, String url,Boolean needRemoveTitle,Boolean needRemoveTail) {

	}
  }