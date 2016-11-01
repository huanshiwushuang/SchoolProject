package com.guohao.schoolproject;

import java.text.SimpleDateFormat;
import java.util.Locale;

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

public class ExamDetail extends Activity {
	private Title customTitle;
	private Activity mActivity;
	private TextView examName,examCourse,examClass,examTime,totalGrade,passGrade,startTime,endTime,examDescription;
	private SharedPreferences p;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examdetail);
		
		initView();
		initBaseData();
	}

	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		customTitle.setTitleText("考试详情");
		
		examName.setText(p.getString(Data.EXAM_PAPER_name, ""));
		examCourse.setText(p.getString(Data.EXAM_PAPER_course, ""));
		examClass.setText(p.getString(Data.EXAM_PAPER_classNo, ""));
		int tempInt = p.getInt(Data.EXAM_PAPER_examTime, -1);
		examTime.setText(tempInt == -1 ? "" : tempInt+"分钟");
		tempInt = p.getInt(Data.EXAM_PAPER_totalScore, -1);
		totalGrade.setText(tempInt == -1 ? "" : tempInt+"分");
		tempInt = p.getInt(Data.EXAM_PAPER_passScore, -1);
		passGrade.setText(tempInt == -1 ? "" : tempInt+"分");
		
		long tempLong = p.getLong(Data.EXAM_PAPER_beginTime, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("zh", "CN"));
		if (tempLong == -1) {
			startTime.setText("");
		}else {
			
			startTime.setText(format.format(tempLong));
		}
		
		tempLong = p.getLong(Data.EXAM_PAPER_endTime, -1);
		if (tempLong == -1) {
			endTime.setText("");
		}else {
			endTime.setText(format.format(tempLong));
		}
		
		examDescription.setText(p.getString(Data.EXAM_PAPER_descript, ""));
	}

	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		mActivity = ExamDetail.this;
		p = Util.getPreference(mActivity);
		
		examName = (TextView) findViewById(R.id.id_textview_name);
		examCourse = (TextView) findViewById(R.id.id_textview_course);
		examClass = (TextView) findViewById(R.id.id_textview_class);
		examTime = (TextView) findViewById(R.id.id_textview_time);
		totalGrade = (TextView) findViewById(R.id.id_textview_total_grade);
		passGrade = (TextView) findViewById(R.id.id_textview_pass_grade);
		startTime = (TextView) findViewById(R.id.id_textview_start_time);
		endTime = (TextView) findViewById(R.id.id_textview_end_time);
		examDescription = (TextView) findViewById(R.id.id_textview_description);

	}
	
	public void startExam(View view) {
		Util.showToast(mActivity, "开始考试");
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ExamDetail.class);
		context.startActivity(intent);
	}
}
