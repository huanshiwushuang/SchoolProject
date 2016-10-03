package com.guohao.custom;

import com.guohao.schoolproject.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Title extends LinearLayout implements OnClickListener {
	private View view;
	private Context mContext;
	
	private ImageView backImg;
	private TextView title,titleOther;

	public Title(Context context) {
		this(context,null);
	}
	public Title(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		view = LayoutInflater.from(context).inflate(R.layout.custom_title, this);
		initView();
		initListener();
		
	}
	private void initListener() {
		backImg.setOnClickListener(this);
	}
	private void initView() {
		backImg = (ImageView) view.findViewById(R.id.id_imageview_back);
		title = (TextView) view.findViewById(R.id.id_textview_title);
		titleOther = (TextView) view.findViewById(R.id.id_textview_title_other);
	}
	public void setImageVisibility(int flag) {
		backImg.setVisibility(flag);
	}
	public void setTitleText(String text) {
		title.setText(text);
	}
	public void setTitleOtherText(String text) {
		titleOther.setText(text);
	}
	
	@Override
	public void onClick(View v) {
		((Activity)mContext).finish();
	}
}
