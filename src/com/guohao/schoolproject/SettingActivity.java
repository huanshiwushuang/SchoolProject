package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener,TextWatcher {
	private static int flag;
	public static final int SUGGEST = 1;
	public static final int CHANGE_PWD = 2;
	public static final int ABOUT = 3;
	
	private Title customTitle;
	
	//---01
	private EditText editText;
	private Button submit;
	private TextView contentLength;
	//---02
	private EditText editText02;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (flag) {
		case SUGGEST:
			setContentView(R.layout.activity_setting_suggest);
			break;
		case CHANGE_PWD:
			setContentView(R.layout.activity_setting_change_pwd);
			break;
		case ABOUT:
			setContentView(R.layout.activity_setting_about);
			break;
		}
		
		initView();
	}
	
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		customTitle.setImageVisibility(View.VISIBLE);
		switch (flag) {
		case SUGGEST:
			customTitle.setTitleText("意见反馈");
			editText = (EditText) findViewById(R.id.id_edittext);
			submit = (Button) findViewById(R.id.id_submit);
			contentLength = (TextView) findViewById(R.id.id_textview_length);
			submit.setOnClickListener(this);
			editText.addTextChangedListener(this);
			break;
		case CHANGE_PWD:
			customTitle.setTitleText("修改密码");
			editText = (EditText) findViewById(R.id.id_edittext_pwd01);
			editText02 = (EditText) findViewById(R.id.id_edittext_pwd02);
			submit = (Button) findViewById(R.id.id_submit);
			submit.setOnClickListener(this);
			break;
		case ABOUT:
			customTitle.setTitleText("关于");
			break;
		}
	}

	public static void actionStart(Context context, int flag) {
		SettingActivity.flag = flag;
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (flag) {
		case SUGGEST:
			Util.showToast(SettingActivity.this, "建议提交");
			break;
		case CHANGE_PWD:
			Util.showToast(SettingActivity.this, "密码修改提交");
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		contentLength.setText((255-s.length())+"");
	}
	@Override
	public void afterTextChanged(Editable s) {
		
	}
}

