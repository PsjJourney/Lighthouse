package com.psj.webview;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.psj.plugin.NativePlugin;
import com.psj.utils.UserManager;

import org.apache.http.cookie.Cookie;

import java.util.List;


/**
 * WebView管理工具
 */
public class WebViewManage {

	public static final String TAG = "YTWebViewManage";
	private Context context;
	private WebView webView;
	private NativePlugin plugin = null;
	private BankWebViewClient.ShowBack showTitleBack;
	private TextView showTitle;// 显示标题头
	private boolean isFrist = false;

	public WebViewManage(Context context, WebView webView, NativePlugin plugin, boolean isFrist) {
		this.context = context;
		this.webView = webView;
		this.plugin = plugin;
		this.isFrist = isFrist;
		initWebSettings();
	}

	public WebViewManage(Context context, WebView webView, NativePlugin plugin, TextView showTitle) {
		this.context = context;
		this.webView = webView;
		this.plugin = plugin;
		this.showTitle = showTitle;
		initWebSettings();
	}

	public WebViewManage(Context context, WebView webView, NativePlugin plugin, BankWebViewClient.ShowBack showTitleBack, TextView showTitle) {
		this.context = context;
		this.webView = webView;
		this.plugin = plugin;
		this.showTitleBack = showTitleBack;
		this.showTitle = showTitle;
		initWebSettings();
	}


	/**
	 * 初始化WebView基本配置
	 */
	private void initWebSettings() {

		WebSettings webSettings = webView.getSettings();
		webSettings.setDomStorageEnabled(true); // 使用localStorage
		webSettings.setLoadWithOverviewMode(true); // 设置页面自适应屏幕
		webSettings.setNeedInitialFocus(false); // 阻止内部节点获取焦点
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 不使用webview缓存
		// webSettings.setPluginState(PluginState.ON);
		webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
		webSettings.setAllowContentAccess(true);
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowUniversalAccessFromFileURLs(true);

		setSupportJsAlert(true);
		webSettings.setJavaScriptEnabled(true);// 支持javascript
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		// SDK3.0以上开启硬件加速，部分机器
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		//取消滚动条
		webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setHorizontalScrollBarEnabled(false);//水平不显示
		webView.setVerticalScrollBarEnabled(false); //垂直不显示
		webView.requestFocusFromTouch();
		webView.setFocusable(true);
		webView.setLongClickable(true);
		webView.setOnLongClickListener(new WebView.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});

		initCookie(UserManager.getInstance().getCookies());

	}

	/**
	 * 添加JavaScript插件
	 */
	public void addJavascriptInterface(Object plugin, String name) {
		webView.addJavascriptInterface(plugin, name);
	}

	private void initCookie(List<Cookie> cookies) {

		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		if (cookies != null) {
			for (int i = 0; i < cookies.size(); i++) {
				String sessionString = cookies.get(i).getName() + "=" + cookies.get(i).getValue() + ";domain=" + cookies.get(i).getDomain();
				cookieManager.setCookie("http://www.light-house.cc/", sessionString);
			}
			CookieSyncManager.getInstance().sync();
		} else {
			cookieManager.removeAllCookie();
			cookieManager.removeSessionCookie();
			CookieSyncManager.getInstance().sync();
		}
	}

	/**
	 * 是否支持JS alert 显示
	 */
	public void setSupportJsAlert(boolean isSupport) {
		if (!isSupport) {
			webView.setWebChromeClient(null);
		} else {
			webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

					return true;
				}
			});
		}
	}

	/**
	 * WebViewManage 监听器
	 */
	public interface WebViewManageListener {
		// 联网错误弹出确认框，点击确认框监听
		public void onClickErrorConfirm(int errorCode, String description);
	}
}