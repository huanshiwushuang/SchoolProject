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
			customTitle.setTitleText("�ǳ�");
			textView.setText("�������ĸ����ǳ�");
			break;
		case NAME:
			customTitle.setTitleText("����");
			textView.setText("����������ʵ����");
			break;
		case EMAIL:
			customTitle.setTitleText("����");
			textView.setText("������������");
			break;
		}
		customTitle.setTitleOtherText("���");
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
		Util.showToast(MeSetActivity.this, "���");
	}


	public static void actionStart(Context context, int flag) {
		Intent intent = new Intent(context, MeSetActivity.class);
		context.startActivity(intent);
		MeSetActivity.flag = flag;
	}
}
