package com.guohao.custom;

import com.guohao.schoolproject.R;
import com.guohao.util.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MeBg extends FrameLayout implements OnClickListener {
	private View view;
	private Context mContext;
	private TextView textView01,textView02;

	public MeBg(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.custom_me_bg, this);
		textView01 = (TextView) view.findViewById(R.id.id_textview_text01);
		textView02 = (TextView) view.findViewById(R.id.id_textview_text02);
		
		TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MeBg);
		String text01 = typedArray.getString(R.styleable.MeBg_text01);
		String text02 = typedArray.getString(R.styleable.MeBg_text02);
		textView01.setText(text01);
		textView02.setText(text02);
		typedArray.recycle();
		
		setOnClickListener(this);
	}

	public void setTextViewText01(String text) {
		textView01.setText(text);
	}
	public void setTextViewText02(String text) {
		textView02.setText(text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_mebg01:
			Util.showToast(mContext, "昵称");
			break;
		case R.id.id_mebg02:
			Util.showToast(mContext, "姓名");
			break;
		case R.id.id_mebg03:
			Util.showToast(mContext, "性别");
			break;
		case R.id.id_mebg04:
			Util.showToast(mContext, "电话");
			break;
		case R.id.id_mebg05:
			Util.showToast(mContext, "邮箱");
			break;
		case R.id.id_mebg06:
			Util.showToast(mContext, "岗位");
			break;
		}
	}
}
