package com.guohao.util;

import android.os.Environment;

public class Data {
	//统一编码
	public static final String ENCODE = "UTF-8";
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
	//获取课程地址
	public static final String URL_GET_COURSE = IP+PROJECT_NAME+"/api/course/getCourses";
	//获取试卷地址
	public static final String URL_GET_EXAM_PAPER = IP+PROJECT_NAME+"/api/exams/getExams";
	//获取试题地址
	public static final String URL_GET_EXAM_TI = IP+PROJECT_NAME+"/api/items/getItems";
	//提交考试成绩地址
	public static final String URL_POST_EXAM_PAPER_GRADE = IP+PROJECT_NAME+"/api/records/commitRecord";
	
	//根路径
	public static final String PATH_APP_ROOT = Environment.getExternalStorageDirectory()+"/SchoolExam";
	//图片保存路径
	public static final String PATH_PHOTO = PATH_APP_ROOT+"/Image";
	
	public static final String NETWORK_EXCEPTION = "网络异常";
	
	
	//数据库
	public static final String SCHOOL_PROJECT_DB = "schoolProject.db";
	public static final String EXAM_PAPER_TABLE_NAME = "examPaperTableName";
	public static final String CHOOSE_ONE_TI = "chooseOneTi";
	public static final String CHOOSE_MORE_TI = "chooseMoreTi";
	public static final String JUDGE_TI = "judgeTi";
	
	
	
	
	
	//用户输入---数据保存--------------------------------------
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
	
	
	//开始答题的时间
	public static final String EXAM_PAPER_START_TIME = "exam_paper_start_time";
	//结束答题时间
	public static final String EXAM_PAPER_END_TIME = "exam_paper_end_time";
	
	//考试试卷---点击的哪一套试卷，将该套试卷的属性保存下来---------------------------------------------------------
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
