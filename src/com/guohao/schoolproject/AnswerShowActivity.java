package com.guohao.schoolproject;

import com.guohao.custom.AnswerShowBg;
import com.guohao.custom.Title;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

public class AnswerShowActivity extends Activity {
	private static int flag = 0;
	public static final int Exam_Test = 1;
	public static final int Book_Read = 2;
	public static final int Error_Content = 3;
	public static final int My_Record = 4;
	
	private Title customTitle;
	private int imageId;
	private LinearLayout allLayout,contentLayout;
	private Activity mActivity;
	private String testString;
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_show);
		
		initView();
		initData();
		//测试所用---模拟请求到的数据
		initNetworkData();
	}
	
	private void initNetworkData() {
		for (int i = 0; i < 5; i++) {
			AnswerShowBg answerShowBg = new AnswerShowBg(mActivity);
			answerShowBg.setImage(imageId);
			answerShowBg.setText(testString);
			
			contentLayout.addView(answerShowBg);
			if (i != 4) {
				View view = new View(mActivity);
				view.setBackgroundColor(Color.parseColor("#E0E0E0"));
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
				view.setLayoutParams(params);
				contentLayout.addView(view);
			}
		}
	}
	private void initData() {
		customTitle.setImageVisibility(View.VISIBLE);
		switch (flag) {
		case Exam_Test:
			customTitle.setTitleText("试卷选择");
			imageId = R.drawable.img42;
			view.setVisibility(View.VISIBLE);
			testString = "试卷选择";
			break;
		case Book_Read:
			customTitle.setTitleText("书籍大全");
			imageId = R.drawable.img15;
			view.setVisibility(View.VISIBLE);
			testString = "书籍大全";
			break;
		case Error_Content:
			customTitle.setTitleText("错题书籍");
			imageId = R.drawable.img63;
			testString = "错题书籍";
			break;
		case My_Record:
			customTitle.setTitleText("考试记录");
			imageId = R.drawable.img56;
			allLayout.setBackgroundColor(Color.parseColor("#ffffff"));
			testString = "考试记录";
			break;
		}
	}
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		allLayout = (LinearLayout) findViewById(R.id.id_linearlayout);
		contentLayout = (LinearLayout) findViewById(R.id.id_linearlayout_content);
		view = findViewById(R.id.id_view_space);
		mActivity = AnswerShowActivity.this;
	}


	public static void actionStart(Context context, int flag) {
		Intent intent = new Intent(context, AnswerShowActivity.class);
		context.startActivity(intent);
		AnswerShowActivity.flag = flag;
	}
}
