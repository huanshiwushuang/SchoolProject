package com.guohao.util;

import android.os.Environment;

public class Data {
	//IP
	public static final String IP = "http://61.157.243.107:8080";
	//��Ŀ����
	public static final String PROJECT_NAME = "/Exam";
	//��¼��ַ
	public static final String URL_LOGIN = IP+PROJECT_NAME+"/api/users/login";
	//�޸������ַ
	public static final String URL_CHANGE_PWD = IP+PROJECT_NAME+"/api/users/modifyPassword";
	//�޸�ͷ���ַ
	public static final String URL_CHANGE_HEAD_PHOTO = IP+PROJECT_NAME+"/api/users/modifyHead";
	
	//��·��
	public static final String PATH_APP_ROOT = Environment.getExternalStorageDirectory()+"/SchoolExam";
	//ͼƬ����·��
	public static final String PATH_PHOTO = PATH_APP_ROOT+"/Image";
	
	public static final String NETWORK_EXCEPTION = "�����쳣";
	
	
	
	
	
	
	
	//�û�����---���ݱ���
	//�û��˻�---ѧ��
	public static final String ACCOUNT = "account";
	//�û�����
	public static final String PWD = "pwd";
	
	//---�������ݱ���---
	//ѧ��
	public static final String STU_NUMBER = "stuNumber";
	//�༶
	public static final String GRADE = "garde";
	//����
	public static final String PASSWORD = "password";
	//ͷ��·��
	public static final String HEAD_IMAGE = "headImage";
	//����
	public static final String NAME = "name";
	//
	public static final String CLASS_NO = "classNo";
	//
	public static final String ID = "id";
	//����
	public static final String TOKEN = "token";
	
}
