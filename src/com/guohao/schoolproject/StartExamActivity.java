package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Data;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class StartExamActivity extends Activity implements OnClickListener,OnDismissListener {
	private Activity mActivity;
	private SharedPreferences p;
	
	private Title customTitle;
	private TextView timeTitle;
	private TextView submit;
	
	private ImageView tiType;
	private TextView ti;
	private ListView listView;
	private View alertCeng;
	private LinearLayout chooseTi;
	
	private Runnable r;
	private PopupWindow popupWindow;
	
	private long startTime,endTime,examTime,nextTime;
	private Boolean onlyOnce = true;
	
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_exam);
		
		initView();
		initLocalData();
	}

	private void initLocalData() {
		customTitle.setImageVisibility(View.VISIBLE);
		submit.setText("交卷");
		startTime = p.getLong(Data.EXAM_PAPER_beginTime, -1);
		endTime = p.getLong(Data.EXAM_PAPER_endTime, -1);
		//分钟转换成毫秒
		examTime = p.getInt(Data.EXAM_PAPER_examTime, -1)*60*1000;
		if (startTime != -1 && endTime != -1 && examTime != -1) {
			//开始答题毫秒时间+允许答题的毫秒时间
			long currentTime = System.currentTimeMillis();
			long temp = currentTime+examTime;
			//比较允许答题时间的结束时间-和-系统结束答题时间，取最小值为终止时间
			temp = Math.min(endTime, temp);
			//计算得到剩余的可答题的时间---毫秒数
			temp -= currentTime;
			//计算得到剩余的可答题的时间---秒数
			nextTime = temp/1000;
			handler.post(r);
		}else {
			Util.showToast(mActivity, "试卷可答题时间获取失败！");
			finish();
		}
	}

	private void initView() {
		mActivity = StartExamActivity.this;
		p = Util.getPreference(mActivity);
		
		customTitle = (Title) findViewById(R.id.id_custom_title);
		timeTitle = customTitle.getTitle();
		submit = customTitle.getTitleOther();
		submit.setOnClickListener(this);
		alertCeng = findViewById(R.id.id_view_alert_ceng);
		chooseTi = (LinearLayout) findViewById(R.id.id_linearlayout_choose_ti);
		chooseTi.setOnClickListener(this);
		
		tiType = (ImageView) findViewById(R.id.id_imageview_ti_type);
		ti = (TextView) findViewById(R.id.id_textview_ti);
		listView = (ListView) findViewById(R.id.id_listview);
		
		handler = new Handler();
		r = new Runnable() {
			@Override
			public void run() {
				if (nextTime > 0) {
					long second = --nextTime;
					if (second <= 5*60 && onlyOnce) {
						Util.showToast(mActivity, "剩余时间不多啦！");
						onlyOnce = false;
					}
					String hours = second/60/60+"";
					String minutes = second/60%60+"";
					String seconds = second%60+"";
					hours = (hours.length() < 2 ? "0"+hours : hours );
					minutes = (minutes.length() < 2 ? "0"+minutes : minutes );
					seconds = (seconds.length() < 2 ? "0"+seconds : seconds );
					timeTitle.setText("倒计时 "+hours+":"+minutes+":"+seconds);
					if (nextTime > 0) {
						handler.postDelayed(this, 1*1000);
					}else {
						Util.showToast(mActivity, "时间结束！");
					}
				}
			}
		};
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_textview_title_other:
			Util.showToast(mActivity, "交卷");
			break;
		case R.id.id_linearlayout_choose_ti:
			//弹出层
			alertCeng.setVisibility(View.VISIBLE);
			
			View view = LayoutInflater.from(mActivity).inflate(R.layout.custom_select_ti, new FrameLayout(mActivity));
			//点击 popup外面，dismiss。
			View dismissView = view.findViewById(R.id.id_view_dismiss);
			dismissView.setOnClickListener(this);
			
			popupWindow = new PopupWindow(view, 
					WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
			// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
			popupWindow.setFocusable(true);
			
			// 实例化一个ColorDrawable颜色为半透明 ---必要的，当背景不为空的时候，才能够返回键 dismiss
			ColorDrawable dw = new ColorDrawable(0x00000000);
			popupWindow.setBackgroundDrawable(dw);
			
			// 设置popWindow的显示和消失动画
			popupWindow.setAnimationStyle(R.style.StyleSelectPhoto);
			//设置dismiss监听事件
			popupWindow.setOnDismissListener(this);
			// 在底部显示
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.id_view_dismiss:
			popupWindow.dismiss();
			break;
		}
	}
	
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, StartExamActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onDismiss() {
		alertCeng.setVisibility(View.GONE);
	}
}
