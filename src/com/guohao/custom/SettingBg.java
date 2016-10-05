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
import android.widget.ImageView;
import android.widget.TextView;

public class SettingBg extends FrameLayout implements OnClickListener {
	private View view;
	private Context mContext;

	public SettingBg(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		view = LayoutInflater.from(context).inflate(R.layout.custom_setting_bg, this);
		ImageView imageView = (ImageView) view.findViewById(R.id.id_imageview);
		TextView textView = (TextView) view.findViewById(R.id.id_textview);
		
		TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.SettingBg);
		int imgId = tArray.getResourceId(R.styleable.SettingBg_img, R.drawable.img104);
		String text = tArray.getString(R.styleable.SettingBg_text);
		imageView.setImageResource(imgId);
		textView.setText(text);
		tArray.recycle();
		
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_setting01:
			Util.showToast(mContext, "版本更新");
			break;
		case R.id.id_setting02:
			Util.showToast(mContext, "意见反馈");
			break;
		case R.id.id_setting03:
			Util.showToast(mContext, "修改密码");
			break;
		case R.id.id_setting04:
			Util.showAlertDialog(mContext, "是否确认注销？", MyAlertDialog.ReLogin);
			break;
		case R.id.id_setting05:
			Util.showAlertDialog(mContext, "是否退出程序？", MyAlertDialog.Exit);
			break;
		case R.id.id_setting06:
			Util.showToast(mContext, "关于");
			break;
		}
	}

}
