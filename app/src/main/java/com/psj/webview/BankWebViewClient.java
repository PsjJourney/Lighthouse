package com.psj.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.psj.plugin.NativePlugin;

import org.apache.http.cookie.Cookie;


public class BankWebViewClient extends WebViewClient {

	private Activity activity;
	private NativePlugin plugin;
	private ShowBack showTitleBack;
	private TextView showTitle;
	private boolean isFrist = false;
	private long lastTime;

	public BankWebViewClient(Activity activity, Cookie sessionCookie) {
		this(activity);
	}

	public BankWebViewClient(final Activity activity) {
		this.activity = activity;
	}

	public BankWebViewClient(Activity activity, NativePlugin plugin) {
		this(activity);
		this.plugin = plugin;
	}

	public BankWebViewClient(Activity activity, NativePlugin plugin, boolean isFrist) {
		this(activity);
		this.plugin = plugin;
		this.isFrist = isFrist;
	}

	public BankWebViewClient(Activity activity, NativePlugin plugin, ShowBack showTitleBack, TextView showTitle) {
		this(activity);
		this.plugin = plugin;
		this.showTitleBack = showTitleBack;
		this.showTitle = showTitle;
	}



	// 在WebView中而不是默认浏览器中显示页面
	public boolean shouldOverrideUrlLoading(WebView view, final String url) {

		if (url.startsWith("tel:")) {
			// 拨打电话

		} else {
			view.requestFocus();
			view.loadUrl(url);
		}
		return true;
	}

	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		// 代理失败消除等待层
		String errorFlagString = "";
		switch (errorCode) {
		case ERROR_AUTHENTICATION:
			errorFlagString = "用户认证失败";
			break;
		case ERROR_CONNECT:
			errorFlagString = "连接服务器失败";
			break;
		case ERROR_TIMEOUT:
			errorFlagString = "网络连接超时";
			break;
		case ERROR_PROXY_AUTHENTICATION:
			errorFlagString = "用户代理验证失败";
			break;
		case ERROR_HOST_LOOKUP:
			errorFlagString = "服务器绑定或代理失败";
			break;
		case ERROR_BAD_URL:
			errorFlagString = "URL 格式错误";
			break;
		default:
			errorFlagString = "网络异常，请稍后重试";
			break;
		}

		if (view.canGoBack()) {
			view.goBack();
		} else {
			final String htmlText = "<html>" + "<head></head>" + "<body>" + errorFlagString + "</body></html>";
			view.loadDataWithBaseURL("about:blank", htmlText, "text/html", "utf-8", null);
		}


	}

	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		if (!isFrist) {
		}

		lastTime = System.currentTimeMillis();

	}

	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if (!isFrist) {
		}

		view.requestFocus();

		/**
		 * 为个人中心WebView提供
		 */
		if (null != showTitleBack) {
			if ("http://dd2.pigcms.com/wap/my.php".equals(url)) {
				// 不展示返回按钮
				showTitleBack.showTitleBack(false);
			} else {
				// 展示返回按钮
				showTitleBack.showTitleBack(true);

			}
		}



		/**
		 * 调取页面的方法通知页面是android客户端在加载
		 */
		view.loadUrl("javascript:appFromAndroid()");
	}

	public interface ShowBack {
		public void showTitleBack(boolean isShow);
	}
}