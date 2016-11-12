package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class MeSetActivity extends Activity implements OnClickListener {
	private static int flag;
	public static final int OTHER_NAME = 1;
	public static final int NAME = 2;
	public static final int EMAIL = 3;
	
	private Title customTitle;
	private EditText editText;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_set);
		
		initView();
		initData();
	}
	
	
	private void initData() {
		switch (flag) {
		case OTHER_NAME:
			customTitle.setTitleText("昵称");
			textView.setText("输入您的个性昵称");
			break;
		case NAME:
			customTitle.setTitleText("姓名");
			textView.setText("输入您的真实姓名");
			break;
		case EMAIL:
			customTitle.setTitleText("邮箱");
			textView.setText("输入您的邮箱");
			break;
		}
		customTitle.setTitleOtherText("完成");
		customTitle.setImageVisibility(View.VISIBLE);
	}

	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		editText = (EditText) findViewById(R.id.id_edittext);
		textView = (TextView) findViewById(R.id.id_textview);
		customTitle.getTitleOther().setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Util.showToast(MeSetActivity.this, "完成");
	}


	public static void actionStart(Context context, int flag) {
		Intent intent = new Intent(context, MeSetActivity.class);
		context.startActivity(intent);
		MeSetActivity.flag = flag;
	}
}
