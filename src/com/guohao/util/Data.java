package com.guohao.util;

import android.os.Environment;

public class Data {
	//ͳһ����
	public static final String ENCODE = "UTF-8";
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
	//��ȡ�γ̵�ַ
	public static final String URL_GET_COURSE = IP+PROJECT_NAME+"/api/course/getCourses";
	//��ȡ�Ծ��ַ
	public static final String URL_GET_EXAM_PAPER = IP+PROJECT_NAME+"/api/exams/getExams";
	//��ȡ�����ַ
	public static final String URL_GET_EXAM_TI = IP+PROJECT_NAME+"/api/items/getItems";
	
	//��·��
	public static final String PATH_APP_ROOT = Environment.getExternalStorageDirectory()+"/SchoolExam";
	//ͼƬ����·��
	public static final String PATH_PHOTO = PATH_APP_ROOT+"/Image";
	
	public static final String NETWORK_EXCEPTION = "�����쳣";
	
	
	//���ݿ�
	public static final String SCHOOL_PROJECT_DB = "schoolProject.db";
	public static final String EXAM_PAPER_TABLE_NAME = "examPaperTableName";
	public static final String CHOOSE_ONE_TI = "chooseOneTi";
	public static final String CHOOSE_MORE_TI = "chooseMoreTi";
	public static final String JUDGE_TI = "judgeTi";
	
	
	
	
	
	//�û�����---���ݱ���--------------------------------------
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
	
	//�����Ծ�---�������һ���Ծ��������Ծ�����Ա�������---------------------------------------------------------
	public static final String EXAM_PAPER_isDelete = "exam_paper_isdelete";
	public static final String EXAM_PAPER_totalScore = "exam_paper_totalscore";
	public static final String EXAM_PAPER_passScore = "exam_paper_passscore";
	public static final String EXAM_PAPER_mode = "exam_paper_mode";
	public static final String EXAM_PAPER_createAdmin = "exam_paper_createadmin";
	public static final String EXAM_PAPER_createTime = "exam_paper_createtime";
	public static final String EXAM_PAPER_isSelectType = "exam_paper_isselecttype";
	public static final String EXAM_PAPER_name = "exam_paper_name";
	public static final String EXAM_PAPER_classNo = "exam_paper_classno";
	public static final String EXAM_PAPER_course = "exam_paper_course";
	public static final String EXAM_PAPER_id = "exam_paper_id";
	public static final String EXAM_PAPER_beginTime = "exam_paper_begintime";
	public static final String EXAM_PAPER_endTime = "exam_paper_endtime";
	public static final String EXAM_PAPER_examTime = "exam_paper_examtime";
	public static final String EXAM_PAPER_descript = "exam_paper_descript";
}
