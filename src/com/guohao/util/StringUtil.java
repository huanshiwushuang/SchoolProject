package com.guohao.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	
	public static String encodeMD5(String string) {
		MessageDigest digest = null;
		StringBuilder builder = null;
		try {
			digest = MessageDigest.getInstance("MD5");
			byte[] bs = digest.digest(string.getBytes("UTF-8"));
			builder = new StringBuilder();
			for (int i = 0; i < bs.length; i++) {
				int j = bs[i]&0xff;
				String hex = Integer.toHexString(j);
				if (hex.length() < 2) {
					hex = "0"+hex;
				}
				builder.append(hex);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
