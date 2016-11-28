package com.psj.utils;

import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * 用户信息管理帮助类
 */
public class UserManager {

	private static UserManager manager = null;

	/**
	 * 用户帮助类实例（单例模式）
	 */
	public synchronized static UserManager getInstance() {
		if (null == manager) {
			manager = new UserManager();
		}
		return manager;
	}

	private List<Cookie> cookies = null;

	public List<Cookie> getCookies() {
		return cookies;
	}

	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}
}