package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ExamDetail extends Activity {
	private Title customTitle;
	private Activity mActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examdetail);
		
		initView();
		initBaseData();
	}

	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		customTitle.setTitleText("øº ‘œÍ«È");
	}

	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		mActivity = ExamDetail.this;
	}
	
	public void startExam(View view) {
		Util.showToast(mActivity, "ø™ ºøº ‘");
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ExamDetail.class);
		context.startActivity(intent);
	}
}
