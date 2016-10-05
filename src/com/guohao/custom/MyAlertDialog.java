package com.guohao.custom;

import com.guohao.schoolproject.LoginActivity;
import com.guohao.schoolproject.R;
import com.guohao.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyAlertDialog implements OnClickListener {
	private Context mContext;
	private View v;
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	private int width,height;
	public int screenWidth,screenHeight;
	
	//---布局01---退出和注销的布局---默认
	public final static int Layout01 = 1;
	private TextView mTitle,mMessage,mYes,mNo;
	private int flag = 2;
	public final static int Exit = 3;
	public final static int ReLogin = 4;
	//---布局02---
//	public final static int Layout02 = 5;
	
	//当前布局
	private static int currentLayout = Layout01;
	
	public MyAlertDialog(Context context) {
		this(context, currentLayout);
	}
	//第二个参数是布局 子view
	public MyAlertDialog(Context context,int selectLayout) {
		mContext = context;
		currentLayout = selectLayout;
		
		initView();
		initData();
		initListener();
	}
	
	private void initListener() {
		switch (currentLayout) {
		case Layout01:
			mYes.setOnClickListener(this);
			mNo.setOnClickListener(this);
			break;
		}
	}
	private void initData() {
		DisplayMetrics metrics = Util.getDisplayMetrics((Activity)mContext);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		width = (int) (screenWidth/(1.13));
		height = (int) (screenHeight/4.3);
	}
	private void initView() {
		builder = new AlertDialog.Builder(mContext);
		
		switch (currentLayout) {
		case Layout01:
			v = LayoutInflater.from(mContext).inflate(R.layout.custom_alertdialog, new FrameLayout(mContext));
			mTitle = (TextView) v.findViewById(R.id.id_textview_title);
			mMessage = (TextView) v.findViewById(R.id.id_textview_message);
			mYes = (TextView) v.findViewById(R.id.id_textview_yes);
			mNo = (TextView) v.findViewById(R.id.id_textview_no);
			break;
		}
	}

	
	//---布局01---设置----------------------------------------------------------------------
	public void setTitle(String title) {
		mTitle.setText(title);
	}
	public void setMessage(String message) {
		mMessage.setText(message);
	}
	public void setYesText(String text) {
		mYes.setText(text);
	}
	public void setNoText(String text) {
		mNo.setText(text);
	}
	//---指明当前点击 确定按钮的作用---在switch里面
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	//---通用方法-------------------------------------------------------------------------
	public void setWidth(int width) {
		this.width = width;
	}
	public void height(int height) {
		this.height = height; 
	}
	//---getter---
	public int getScreenWidth() {
		return screenWidth;
	}
	public int getScreenHeight() {
		return screenHeight;
	}
	public void show() {
		dialog = builder.show();
		dialog.setContentView(v);
		dialog.getWindow().setLayout(width, height);
	}
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	public Boolean isShowing() {
		if (dialog != null) {
			return dialog.isShowing();
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		switch (currentLayout) {
		case Layout01:
			switch (v.getId()) {
			case R.id.id_textview_yes:
				switch (flag) {
				case ReLogin:
					dismiss();
					LoginActivity.actionStart(mContext);
					((Activity)mContext).finish();
					break;
				case Exit:
					dismiss();
					((Activity)mContext).finish();
					break;
				}
				break;
			case R.id.id_textview_no:
				dismiss();
				break;
			}
			break;
		}
	}
}
