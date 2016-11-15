package com.guohao.schoolproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ExamGrade extends Activity {
	private int isPass;
	private int score;
	private String useTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_grade);
		
		initBaseData();
		initView();
	}

	private void initBaseData() {
		Intent intent = getIntent();
		isPass = intent.getIntExtra("isPass", -1);
		score = intent.getIntExtra("score", -1);
		useTime = intent.getStringExtra("useTime");
	}

	private void initView() {
		Log.d("guohao", "isPass"+isPass);
		Log.d("guohao", "score"+score);
		Log.d("guohao", "useTime"+useTime);
	}
	
	
	
	
	public static void actionStart(Context context, int isPass, int score, String useTime) {
		Intent intent = new Intent(context, ExamGrade.class);
		intent.putExtra("isPass", isPass);
		intent.putExtra("score", score);
		intent.putExtra("useTime", useTime);
		context.startActivity(intent);
	}
}
