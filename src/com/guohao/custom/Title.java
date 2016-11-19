package com.guohao.custom;

import com.guohao.schoolproject.R;
import com.guohao.schoolproject.StartExamActivity;
import com.guohao.util.Util;

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
	
	//---set---
	public void setImageVisibility(int flag) {
		backImg.setVisibility(flag);
	}
	public void setTitleText(String text) {
		title.setText(text);
	}
	public void setTitleOtherText(String text) {
		titleOther.setText(text);
	}
	//---getter
	public TextView getTitle() {
		return title;
	}
	public TextView getTitleOther() {
		return titleOther;
	}
	
	@Override
	public void onClick(View v) {
		Activity activity = ((Activity)mContext);
		if (activity instanceof StartExamActivity) {
			Util.showAlertDialog05(activity, "是否放弃本次答题？", MyAlertDialog.Layout07);
		}else {
			activity.finish();
		}
	}
}
