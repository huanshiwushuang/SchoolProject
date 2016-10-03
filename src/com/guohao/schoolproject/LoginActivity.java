package com.guohao.schoolproject;

import com.guohao.custom.Title;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends Activity {
	private Title customTitle;
	
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
	}
	
	public void buttonClick(View view) {
		Log.d("guohao", "µã»÷ÁË°´Å¥");
	}

	public static void actionStart(Context c) {
		Intent intent = new Intent(c, LoginActivity.class);
		c.startActivity(intent);
	}
}
