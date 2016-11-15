package com.guohao.schoolproject;

import com.guohao.custom.Title;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExamGrade extends Activity implements OnClickListener {
	private int isPass;
	private int score;
	private String useTime;
	
	private ImageView imageIV;
	private TextView gradeTV,useTimeTV;
	private Button backBT;
	private Title customTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_grade);
		
		initView();
		initListener();
		initBaseData();
	}


	private void initBaseData() {
		Intent intent = getIntent();
		isPass = intent.getIntExtra("isPass", -1);
		score = intent.getIntExtra("score", -1);
		useTime = intent.getStringExtra("useTime");
		
		customTitle.setTitleText("考试成绩");
		if (isPass == 1) {
			imageIV.setBackgroundResource(R.drawable.img338);
		}else {
			imageIV.setBackgroundResource(R.drawable.img334);
		}
		gradeTV.setText(score+"分");
		useTimeTV.setText("用时："+useTime);
		
	}

	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		imageIV = (ImageView) findViewById(R.id.id_imageview_img);
		gradeTV = (TextView) findViewById(R.id.id_textview_grade);
		useTimeTV = (TextView) findViewById(R.id.id_textview_use_time);
		backBT = (Button) findViewById(R.id.id_button_back);
	}

	private void initListener() {
		backBT.setOnClickListener(this);
	}
	
	
	public static void actionStart(Context context, int isPass, int score, String useTime) {
		Intent intent = new Intent(context, ExamGrade.class);
		intent.putExtra("isPass", isPass);
		intent.putExtra("score", score);
		intent.putExtra("useTime", useTime);
		context.startActivity(intent);
	}


	@Override
	public void onClick(View v) {
		finish();
	}
}
