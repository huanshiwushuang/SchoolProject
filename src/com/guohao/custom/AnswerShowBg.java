package com.guohao.custom;

import com.guohao.schoolproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class AnswerShowBg extends FrameLayout {
	private View view;
	private ImageView imageView;
	private TextView textView;
	private Context mContext;

	public AnswerShowBg(Context context) {
		super(context);
		
		mContext = context;
		initView();
	}

	private void initView() {
		view = LayoutInflater.from(mContext).inflate(R.layout.custom_answer_show_bg, this);
		imageView = (ImageView) view.findViewById(R.id.id_imageview);
		textView = (TextView) findViewById(R.id.id_textview);
	}

	public void setText(String text) {
		textView.setText(text);
	}
	public void setImage(int resId) {
		imageView.setImageResource(resId);
	}
}
