package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private Title customTitle;
	private EditText phoneNum,pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
		initData();
	}
	private void initData() {
		customTitle.setTitleText("µÇÂ¼");
	}
	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		phoneNum = (EditText) findViewById(R.id.id_edittext_phone_num);
		pwd = (EditText) findViewById(R.id.id_edittext_pwd);
	}
	
	
	
	
	public void login(View view) {
		MainActivity.actionStart(LoginActivity.this);
	}
	public void registe(View view) {
		RegisteActivity.actionStart(LoginActivity.this);
		overridePendingTransition(R.anim.anim_in_trans, R.anim.anim_out_trans);
	}
	public void forgetPwd(View view) {
		ForgetPwdActivity.actionStart(LoginActivity.this);
		overridePendingTransition(R.anim.anim_in_trans, R.anim.anim_out_trans);
	}
	public void loginWeiXin(View view) {
		Util.showToast(LoginActivity.this, "µã»÷Î¢ÐÅµÇÂ¼");
	}
	public void loginQQ(View view) {
		Util.showToast(LoginActivity.this, "µã»÷QQµÇÂ¼");
	}

	
	
	
	public static void actionStart(Context c) {
		Intent intent = new Intent(c, LoginActivity.class);
		c.startActivity(intent);
	}
}
