package com.guohao.custom;

import java.util.List;

import com.guohao.adapter.MeListviewBgAdapter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
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
	//---布局02---碎片02--->岗位
	public final static int Layout02 = 5;
	private ListView listView;
	private List<Object[]> list;
	private int clickPosition = -1;
	private MeListviewBgAdapter adapter;
	//---布局03---碎片02--->性别
	public final static int Layout03 = 6;
	
	//当前布局
	private static int currentLayout = Layout01;
	
	//---构造函数---布局01
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
		case Layout01:
			mYes.setOnClickListener(this);
			mNo.setOnClickListener(this);
			break;
		case Layout02:
			mYes.setOnClickListener(this);
			mNo.setOnClickListener(this);
			listView.setOnItemClickListener(this);
			break;
		case Layout03:
			listView.setOnItemClickListener(this);
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
		case Layout02:
			adapter = new MeListviewBgAdapter(mContext, R.layout.custom_me_listview_radio, list);
			listView.setAdapter(adapter);
			break;
		case Layout03:
			adapter = new MeListviewBgAdapter(mContext, R.layout.custom_me_listview_radio, list);
			listView.setAdapter(adapter);
			break;
		}
	}
	private void initView() {
		builder = new AlertDialog.Builder(mContext);
		inflater = LayoutInflater.from(mContext);
		
		switch (currentLayout) {
		case Layout01:
			v = inflater.inflate(R.layout.custom_alertdialog, new FrameLayout(mContext));
			mTitle = (TextView) v.findViewById(R.id.id_textview_title);
			mMessage = (TextView) v.findViewById(R.id.id_textview_message);
			mYes = (TextView) v.findViewById(R.id.id_textview_yes);
			mNo = (TextView) v.findViewById(R.id.id_textview_no);
			break;
		case Layout02:
			v = inflater.inflate(R.layout.custom_me_listview, new FrameLayout(mContext));
			mYes = (TextView) v.findViewById(R.id.id_textview_yes);
			mNo = (TextView) v.findViewById(R.id.id_textview_no);
			listView = (ListView) v.findViewById(R.id.id_listview);
			break;
		case Layout03:
			v = inflater.inflate(R.layout.custom_me_sex, new FrameLayout(mContext));
			listView = (ListView) v.findViewById(R.id.id_listview);
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
		case Layout02:
			switch (v.getId()) {
			case R.id.id_textview_yes:
				Util.showToast(mContext, "确定---");
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
		
		switch (currentLayout) {
		case Layout02:
			
			break;
		case Layout03:
			
			break;
		}
	}
}
