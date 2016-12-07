package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisteActivity extends Activity {
	private Title customTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registe);
		
		initView();
		initData();
	}

	private void initData() {
		customTitle.setTitleText("注册");
		customTitle.setImageVisibility(View.VISIBLE);
	}
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
	}
	
	
	
	public void check(View view) {
		Util.showToast(RegisteActivity.this, "发送验证码");
	}
	public void registe(View view) {
		Util.showToast(RegisteActivity.this, "注册");
	}
	
	
	
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, RegisteActivity.class);
		context.startActivity(intent);
	}
}
