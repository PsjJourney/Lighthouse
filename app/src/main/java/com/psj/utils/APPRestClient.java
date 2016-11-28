package com.psj.utils;

import com.lidroid.xutils.HttpUtils;

public class APPRestClient {

	private static HttpUtils http;

	public HttpUtils getHttpClient() {

		if (http == null) {
			http = new HttpUtils();
		}

		http.configCurrentHttpCacheExpiry(1000 * 10);// 设置超时时间
		// RequestParams httpParams = new RequestParams();
		// httpParams.setHeader(header);
		// ((Object) httpParams).setContentCharset("GBK?");

		return http;
	}
}
