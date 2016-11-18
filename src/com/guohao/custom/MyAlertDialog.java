package com.guohao.custom;

import java.util.List;

import com.guohao.adapter.MeListviewBgAdapter;
import com.guohao.schoolproject.LoginActivity;
import com.guohao.schoolproject.R;
import com.guohao.schoolproject.StartExamActivity;
import com.guohao.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAlertDialog implements OnClickListener,OnItemClickListener {
	private Context mContext;
	private View v;
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	private int width,height;
	public int screenWidth,screenHeight;
	private LayoutInflater inflater;
	
	private int flag = 1;
	private TextView mYes,mNo;
	//---布局01---退出和注销的布局---默认
	public final static int Layout01 = 2;
	private TextView mTitle,mMessage;
	public final static int Exit = 3;
	public final static int ReLogin = 4;
	
	private List<Object[]> list;
	private int clickPosition = -1;
	private MeListviewBgAdapter adapter;
	//---布局04---正在加载......
	public final static int Layout04 = 7;
	private ImageView rotateImage;
	private TextView loadingPrompt;
	//---布局05---是否提交试卷。
	public final static int Layout05 = 8;
	//---布局06---是否继续答题。
	public final static int Layout06 = 9;
	//---布局07---是否放弃本次答题
	public final static int Layout07 = 10;
	
	//当前布局
	private static int currentLayout = Layout01;
	
	//---构造函数---布局01、布局04
	public MyAlertDialog(Context context, int layout) {
		this(context,layout,null);
	}
	//---构造函数---布局02 和 03
	public MyAlertDialog(Context context, int layout, List<Object[]> list) {
		mContext = context;
		currentLayout = layout;
		this.list = list;
		
		initView();
		initData();
		initListener();
	}
	
	private void initListener() {
		switch (currentLayout) {
		case Layout07:
		case Layout06:
		case Layout05:
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
		
		switch (currentLayout) {
		case Layout04:
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.custom_rotate_image);
			rotateImage.startAnimation(animation);
			break;
		}
	}
	private void initView() {
		builder = new AlertDialog.Builder(mContext);
		inflater = LayoutInflater.from(mContext);
		
		switch (currentLayout) {
		case Layout07:
		case Layout06:
		case Layout05:
		case Layout01:
			v = inflater.inflate(R.layout.custom_alertdialog, new FrameLayout(mContext));
			mTitle = (TextView) v.findViewById(R.id.id_textview_title);
			mMessage = (TextView) v.findViewById(R.id.id_textview_message);
			mYes = (TextView) v.findViewById(R.id.id_textview_yes);
			mNo = (TextView) v.findViewById(R.id.id_textview_no);
			break;
		case Layout04:
			v = inflater.inflate(R.layout.custom_alertdialog_loading, new FrameLayout(mContext));
			rotateImage = (ImageView) v.findViewById(R.id.id_imageview_rotate);
			loadingPrompt = (TextView) v.findViewById(R.id.id_textview_prompt);
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
	//---布局04---
	public void setLoadingPrompt(String text) {
		if (loadingPrompt != null) {
			loadingPrompt.setText(text);
		}
	}
	//---指明当前点击 确定按钮的作用---在switch里面
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	//---通用方法-------------------------------------------------------------------------
	public void setYesText(String text) {
		mYes.setText(text);
	}
	public void setNoText(String text) {
		mNo.setText(text);
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setheight(int height) {
		this.height = height; 
	}
	//---getter---
	public int getScreenWidth() {
		return screenWidth;
	}
	public int getScreenHeight() {
		return screenHeight;
	}
	public AlertDialog getAlertDialog() {
		return dialog;
	}
	public void show() {
		dialog = builder.show();
		dialog.setContentView(v);
		dialog.getWindow().setLayout(width, height);
	}
	public void setCancelable(Boolean flag) {
		if (builder != null) {
			builder.setCancelable(flag);
		}
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
					//注销，清除所有键值对
					Editor editor = Util.getPreference(mContext).edit();
					editor.clear();
					editor.commit();
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
		case Layout05:
			switch (v.getId()) {
			case R.id.id_textview_yes:
				dismiss();
				((StartExamActivity)mContext).submitExamPaper();
				break;
			case R.id.id_textview_no:
				dismiss();
				break;
			}
			break;
		case Layout06:
			switch (v.getId()) {
			case R.id.id_textview_yes:
				dismiss();
				StartExamActivity.actionStart(mContext);
				break;
			case R.id.id_textview_no:
				dismiss();
				break;
			}
			break;
		case Layout07:
			switch (v.getId()) {
			case R.id.id_textview_yes:
				dismiss();
				((StartExamActivity)mContext).finish();
				break;
			case R.id.id_textview_no:
				dismiss();
				break;
			}
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		list.get(position)[0] = R.drawable.img296;
		if (clickPosition != -1) {
			list.get(clickPosition)[0] = R.drawable.img265;
		}
		adapter.notifyDataSetChanged();
		clickPosition = position;
	}
}
