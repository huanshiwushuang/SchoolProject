package com.guohao.util;

public class StringUtil {
	public static Boolean isEmpty(String text) {
		if (text == null || text.equals("") || text == "") {
			return true;
		}
		return false;
	}
}
