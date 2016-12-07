package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Data;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChapterListActivity extends Activity {
	private Title customTitle;
	private Activity mActivity;
	private SharedPreferences p;
	
	private TextView courseName,examPaperName,studentId,grade,startTime,endTime,isPass,useTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter_list);
		
		initView();
		initBaseData();
	}
	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		customTitle.setTitleText("记录详情");
		
		courseName.setText("课程名："+p.getString(Data.EXAM_PAPER_course_name, "暂无"));
		examPaperName.setText("试卷名："+p.getString(Data.EXAM_PAPER_exam_paper_name, "暂无"));
		studentId.setText(p.getString(Data.EXAM_PAPER_student_id, "暂无"));
		grade.setText(p.getString(Data.EXAM_PAPER_grade, "暂无"));
		startTime.setText(p.getString(Data.EXAM_PAPER_start_time, "暂无"));
		endTime.setText(p.getString(Data.EXAM_PAPER_end_time, "暂无"));
		isPass.setText(p.getString(Data.EXAM_PAPER_is_pass, "暂无"));
		useTime.setText(p.getString(Data.EXAM_PAPER_use_time, "暂无"));
	}
	private void initView() {
		mActivity = ChapterListActivity.this;
		p = Util.getPreference(mActivity);
		
		customTitle = (Title) findViewById(R.id.id_custom_title);
		
		courseName = (TextView) findViewById(R.id.id_textview_course_name);
		examPaperName = (TextView) findViewById(R.id.id_textview_exam_paper_name);
		studentId = (TextView) findViewById(R.id.id_textview_student_id);
		grade = (TextView) findViewById(R.id.id_textview_grade);
		startTime = (TextView) findViewById(R.id.id_textview_start_time);
		endTime = (TextView) findViewById(R.id.id_textview_end_time);
		isPass = (TextView) findViewById(R.id.id_textview_is_pass);
		useTime = (TextView) findViewById(R.id.id_textview_use_time);
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ChapterListActivity.class);
		context.startActivity(intent);
	}
}
