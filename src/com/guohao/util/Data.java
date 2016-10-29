package com.guohao.util;

import android.os.Environment;

public class Data {
	//IP
	public static final String IP = "http://61.157.243.107:8080";
	//项目名字
	public static final String PROJECT_NAME = "/Exam";
	//登录地址
	public static final String URL_LOGIN = IP+PROJECT_NAME+"/api/users/login";
	//修改密码地址
	public static final String URL_CHANGE_PWD = IP+PROJECT_NAME+"/api/users/modifyPassword";
	//修改头像地址
	public static final String URL_CHANGE_HEAD_PHOTO = IP+PROJECT_NAME+"/api/users/modifyHead";
	
	//根路径
	public static final String PATH_APP_ROOT = Environment.getExternalStorageDirectory()+"/SchoolExam";
	//图片保存路径
	public static final String PATH_PHOTO = PATH_APP_ROOT+"/Image";
	
	public static final String NETWORK_EXCEPTION = "网络异常";
	
	
	
	
	
	
	
	//用户输入---数据保存
	//用户账户---学号
	public static final String ACCOUNT = "account";
	//用户密码
	public static final String PWD = "pwd";
	
	//---本地数据保存---
	//学号
	public static final String STU_NUMBER = "stuNumber";
	//班级
	public static final String GRADE = "garde";
	//密码
	public static final String PASSWORD = "password";
	//头像路径
	public static final String HEAD_IMAGE = "headImage";
	//姓名
	public static final String NAME = "name";
	//
	public static final String CLASS_NO = "classNo";
	//
	public static final String ID = "id";
	//令牌
	public static final String TOKEN = "token";
	
}
