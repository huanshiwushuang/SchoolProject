package com.guohao.schoolproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class ShowActivity extends Activity {
	private Handler handler;
	private Runnable r;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		
		initView();
		jump();
	}

	private void jump() {
		handler.postDelayed(r, 3*1000);
	}

	private void initView() {
		handler = new Handler();
		r = new Runnable() {
			
			@Override
			public void run() {
				//这里需要判断---是否登录，跳转不同的界面
				LoginActivity.actionStart(ShowActivity.this);
				overridePendingTransition(R.anim.anim_in_trans,R.anim.anim_out_trans);
				finish();
			}
		};
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(r);
	}
}
