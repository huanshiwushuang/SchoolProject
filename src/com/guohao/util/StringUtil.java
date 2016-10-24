package com.guohao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
	public static Boolean isEmpty(String text) {
		if (text == null || text.equals("") || text == "") {
			return true;
		}
		return false;
	}
	
	public static Boolean checkAccount(String account) {
		Boolean isTrue = true;
		Pattern pattern = Pattern.compile("^\\d{8}$");
		Matcher matcher = pattern.matcher(account);
		if (isEmpty(account) || !matcher.find()) {
			isTrue = false;
		}
		return isTrue;
	}
	public static Boolean checkPwd(String pwd) {
		Boolean isTrue = true;
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9]{6,16})$");
		Matcher matcher = pattern.matcher(pwd);
		if (isEmpty(pwd) || !matcher.find()) {
			isTrue = false;
		}
		return isTrue;
	}
}
