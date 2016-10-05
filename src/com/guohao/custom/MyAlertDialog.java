package com.guohao.custom;

import com.guohao.schoolproject.LoginActivity;
import com.guohao.schoolproject.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyAlertDialog implements OnClickListener {
	private Context mContext;
	private TextView mTitle,mMessage,mYes,mNo;
	private View v;
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	//标识该AlertDialog的作用，用于处理 确定 点击事件
	private int flag = -1;
	
	public final static int Default = 0;
	public final static int Exit = 1;
	public final static int ReLogin = 2;
	
	public MyAlertDialog(Context context) {
		mContext = context;
		
		initView();
		initListener();
	}

	private void initListener() {
		mYes.setOnClickListener(this);
		mNo.setOnClickListener(this);
	}

	private void initView() {
		v = LayoutInflater.from(mContext).inflate(R.layout.custom_alertdialog, new FrameLayout(mContext));
		mTitle = (TextView) v.findViewById(R.id.id_textview_title);
		mMessage = (TextView) v.findViewById(R.id.id_textview_message);
		mYes = (TextView) v.findViewById(R.id.id_textview_yes);
		mNo = (TextView) v.findViewById(R.id.id_textview_no);
		
		builder = new AlertDialog.Builder(mContext);
	}

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
	public void show() {
		dialog = builder.show();
		dialog.setContentView(v);
		dialog.getWindow().setLayout(400, 200);
	}
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public Boolean isShowing() {
		if (dialog != null) {
			return dialog.isShowing();
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
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
	}
}
