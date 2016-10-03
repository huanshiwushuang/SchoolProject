package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ForgetPwdActivity extends Activity {
	private Title customTitle;
	private EditText phoneNum,vCode,pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		
		initView();
		initData();
	}
	
	private void initData() {
		customTitle.setTitleText("忘记密码");
		customTitle.setImageVisibility(View.VISIBLE);
	}
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		phoneNum = (EditText) findViewById(R.id.id_edittext_phone_num);
		vCode = (EditText) findViewById(R.id.id_edittext_v_code);
		pwd = (EditText) findViewById(R.id.id_edittext_pwd);
	}
	
	
	public void check(View view) {
		Util.showToast(ForgetPwdActivity.this, "发送验证码");
	}
	public void registe(View view) {
		Util.showToast(ForgetPwdActivity.this, "修改");
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ForgetPwdActivity.class);
		context.startActivity(intent);
	}
	
}
