package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisteActivity extends Activity {
	private Title customTitle;
	private EditText phoneNum,vCode,pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registe);
		
		initView();
		initData();
	}

	private void initData() {
		customTitle.setTitleText("×¢²á");
		customTitle.setImageVisibility(View.VISIBLE);
	}
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		phoneNum = (EditText) findViewById(R.id.id_edittext_phone_num);
		vCode = (EditText) findViewById(R.id.id_edittext_v_code);
		pwd = (EditText) findViewById(R.id.id_edittext_pwd);
	}
	
	
	
	public void check(View view) {
		Util.showToast(RegisteActivity.this, "·¢ËÍÑéÖ¤Âë");
	}
	public void registe(View view) {
		Util.showToast(RegisteActivity.this, "×¢²á");
	}
	
	
	
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, RegisteActivity.class);
		context.startActivity(intent);
	}
}
