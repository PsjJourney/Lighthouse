package com.psj.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.psj.plugin.NativePlugin;
import com.psj.utils.APPRestClient;
import com.psj.utils.ToastTools;
import com.psj.webview.MyWebViewClient;
import com.psj.webview.WebViewManage;


/**
 * 加载页面
 */

public class WebViewActivity extends AppCompatActivity implements OnClickListener, WebViewManage.WebViewManageListener {
	private static final String TAG = "WebViewActivity";
	/**
	 * 标题头信息
	 */
	private View webview_title_topView;
	private LinearLayout webview_title_leftLin;// 返回按钮

	private TextView webview_title_text;// 标题头
	private LinearLayout webview_title_rightLin;
	private ImageView webview_title_right_icon;// 分享按钮

	private WebView webview;
	private RelativeLayout activity_webview_wv_tail;
	private String webUrl;// 加载路径
	private boolean needRemoveTitle;// 是否去除标题头
	private boolean needRemoveTail;// 是否去除标题头

	private NativePlugin nativPlugin;// 交互插件
	private WebViewManage webViewManage;// WebView参数设置

	/**
	 * 分享信息
	 */
	private String shareTitle = "";// 分享标题
	private String shareDesc = "";// 分享描述
	private String shareLink = "";// 分享链接
	private String shareImgUrl = "";// 分享路径
	/**
	 * 接收的数据
	 */
	@SuppressWarnings("unused")
	private String storeId = "";
	private int backPressCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		webview_title_topView = findViewById(R.id.webview_title_topView);
//		setTransparent(webview_title_topView);
		webview_title_leftLin = (LinearLayout) findViewById(R.id.webview_title_leftLin);
		webview_title_text = (TextView) findViewById(R.id.webview_title_text);
		webview_title_rightLin = (LinearLayout) findViewById(R.id.webview_title_rightLin);
		webview_title_right_icon = (ImageView) findViewById(R.id.webview_title_right_icon);
		webview = (WebView) findViewById(R.id.activity_webview_wv);
		activity_webview_wv_tail = (RelativeLayout)findViewById(R.id.activity_webview_wv_tail);

		initAction();
		initData();
	}

	private void initAction() {

		webview_title_leftLin.setOnClickListener(this);
		webview_title_right_icon.setOnClickListener(this);

		if (Build.VERSION.SDK_INT >= 19) {
			webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}

		nativPlugin = new NativePlugin(WebViewActivity.this, webview);
		webViewManage = new WebViewManage(WebViewActivity.this, webview, nativPlugin, webview_title_text);
		webViewManage.addJavascriptInterface(nativPlugin, NativePlugin.NAME);
		// 设置setWebChromeClient对象
		webview.setWebChromeClient(new MyWebChromeClient());
		//拦截部分链接
		MyWebViewClient myWebViewClient = new MyWebViewClient(WebViewActivity.this, webview);
		
		webview.setWebViewClient(myWebViewClient);
		
	}
	
	private class MyWebChromeClient extends WebChromeClient{
		/**
		 * 设置标题头
		 */
		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			Log.e(TAG, "MyWebChromeClient中执行onReceivedTitle方法");
			shareTitle = title;
			webview_title_text.setText(title);
//			Constant.url_title.put(view.getUrl(), title);
		}
		// 处理Alert事件  
	    @Override  
	    public boolean onJsAlert(WebView view, String url, String message,  
	            JsResult result) {  
	    	Log.e(TAG, "MyWebChromeClient中执行onJsAlert方法");
	        return super.onJsAlert(view, url, message, result);  
	    } 
	}

	private void initData() {
		Log.e(TAG, "initData()");
		/**
		 * 获取传递信息
		 */
		webUrl = getIntent().getStringExtra("URL");
		// webTitle = getIntent().getStringExtra("TITLE");
		needRemoveTitle = getIntent().getBooleanExtra("NEED_REMOVE_TITLE", false);
		
		needRemoveTail = getIntent().getBooleanExtra("NEED_REMOVE_TAIL", true);
		
		storeId = getIntent().getStringExtra("STORE_ID");
		Log.e(TAG, "webUrl:" + webUrl);
		if (webUrl.contains(".png")) {
			Log.e(TAG, "webUrl包含.png");
			WebSettings ws = webview.getSettings();
			ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			webview.setInitialScale(80);
			webview.loadUrl(webUrl);
			
		}else {
			Log.e(TAG, "webUrl不包含.png");
			if (webUrl.contains("/unitary/")) {
				Log.e(TAG, "webUrl包含/unitary/");
//				webview.loadUrl(ServiceUrlManager.getServiceBaseUrl()+"/webapp/snatch/#/userindex");
			}
			else if (webUrl.contains("c=unitary&a=payend&orderid=")) {
				Log.e(TAG, "webUrl包含c=unitary&a=payend&orderid=");
				String str = "";
				if (webUrl.contains("&orderid=")) {
					Log.e(TAG, "webUrl包含&orderid=");
					int location = webUrl.indexOf("&orderid=");
					str = webUrl.substring(location+9,location+9+3);
				}
//				webview.loadUrl(ServiceUrlManager.getServiceBaseUrl()+"/wap/db_order_paid.php?orderid="+str);
			}
			else if (webUrl.contains("order.php?orderid")) {
				Log.e(TAG, "webUrl包含order.php?orderid");
				needRemoveTail  =true;
				webview.loadUrl(webUrl);
			}
			else if (webUrl.contains("rights_detail.php?order_no=")) {
				Log.e(TAG, "webUrl包含rights_detail.php?order_no=");
				needRemoveTail  =true;
				webview.loadUrl(webUrl);
			}
			else if (webUrl.contains("return_detail.php?order_no=")) {
				Log.e(TAG, "webUrl包含return_detail.php?order_no=");
				needRemoveTail  =true;
				webview.loadUrl(webUrl);
			}
			else if (webUrl.contains("/groupbuy/")||webUrl.contains("/chanping/")) {
				Log.e(TAG, "webUrl包含/groupbuy/或者/chanping/");
				webview_title_rightLin.setVisibility(View.VISIBLE);
				webview_title_right_icon.setVisibility(View.VISIBLE);
				webview.loadUrl(webUrl);
				shareLink = webUrl;
				shareImgUrl = "http://d.pigcms.com/upload/zc/2016/0418/76bf89bd380923776e1ec1c22809c491.jpg";
			}
			else if (webUrl.contains("my_point.php")) {
				Log.e(TAG, "webUrl包含my_point.php");
				webview.loadUrl(webUrl);
			}
			else {
				webview.loadUrl(webUrl);
				/**
				 * 获取页面信息
				 */
				getWebContent();
			}
		}
		/**
		 * 特殊页面去除页面标题头
		 */
		if (needRemoveTitle) {
			setMargins(webview, 0, -80, 0, 0);
		}
		
		if (needRemoveTail) {
			setMargins(webview, 0, 0, 0, -100);
			activity_webview_wv_tail.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.webview_title_leftLin) {
			// 返回监听
			onBackPressed();
		}
	}

	/**
	 * 动态设置高度
	 */
	public void setMargins(View view, int left, int top, int right, int bottom) {
		if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			p.setMargins(left, top, right, bottom);
			view.requestLayout();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (webview.canGoBack()) {
			webview.goBack();
			backPressCount++;
			if (backPressCount>2) {
				webview.clearHistory();
			}
		} else {
			finish();
		}
	}

	@Override
	public void onClickErrorConfirm(int errorCode, String description) {
		ToastTools.showLong(WebViewActivity.this, description);
		finish();
	}

	/**
	 * 获取页面信息
	 */
	public void getWebContent() {
		
		RequestParams reParams = new RequestParams();
		
		new APPRestClient().getHttpClient().send(HttpRequest.HttpMethod.POST, webUrl, reParams, new RequestCallBack<String>() {
			@Override
			public void onStart() {
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Log.e(TAG, "result:" + arg0.result);
				/**
				 * 获取网页信息（有标题展示分享按钮，无标题不展示）
				 */
				if (arg0.result.contains("app_share_title")) {
					Log.e(TAG, "result包含app_share_title，显示右上角图标" );
					shareTitle = getShareMsg(arg0.result, "app_share_title");
					webview_title_rightLin.setVisibility(View.VISIBLE);
					webview_title_right_icon.setVisibility(View.VISIBLE);
				} else {
					Log.e(TAG, "result不包含app_share_title，不显示右上角图标" );
					webview_title_rightLin.setVisibility(View.INVISIBLE);
					webview_title_right_icon.setVisibility(View.INVISIBLE);
				}

				if (arg0.result.contains("app_share_desc")) {
					Log.e(TAG, "result包含app_share_desc" );
					shareDesc = getShareMsg(arg0.result, "app_share_desc");
				}

				if (arg0.result.contains("app_share_link")) {
					Log.e(TAG, "result包含app_share_link" );
					shareLink = getShareMsg(arg0.result, "app_share_link");
				}

				if (arg0.result.contains("app_share_imgUrl")) {
					Log.e(TAG, "result包含app_share_imgUrl" );
					shareImgUrl = getShareMsg(arg0.result, "app_share_imgUrl");
				}


			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
	}

	/**
	 * 根据html全部内容获取想要的分享信息
	 */
	private String getShareMsg(String all, String key) {

		String str01 = all.substring(all.indexOf(key));

		String str02 = str01.substring(str01.indexOf("'"));

		String str03 = str02.substring(1);

		String str04 = str03.substring(0, str03.indexOf("'"));

		return str04;
	}
	
	@Override  
	protected void onPause ()  
	{  
	    webview.reload ();  
	    super.onPause ();  
	}
	
}