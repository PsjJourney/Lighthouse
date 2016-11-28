package com.psj.plugin;

import android.app.Activity;
import android.webkit.WebView;

public class NativePlugin extends BABasePlugin {

	private Activity activity;
	public static final String NAME = "SysClientJs";

	public NativePlugin(Activity activity, WebView webView) {
		this.activity = activity;
	}

}