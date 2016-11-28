package com.psj.plugin;

public class BABasePlugin {
	/**
	 * 防止js注入
	 */
	public Object getClass(Object o) {
		return null;
	}
}
