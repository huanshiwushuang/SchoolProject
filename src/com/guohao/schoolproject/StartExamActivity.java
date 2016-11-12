package com.guohao.schoolproject;


import java.util.ArrayList;
import java.util.List;

import com.guohao.custom.Title;
import com.guohao.entity.ExamTi;
import com.guohao.fragment.ChooseMoreTiFragment;
import com.guohao.fragment.ChooseOneTiFragment;
import com.guohao.fragment.JudgeTiFragment;
import com.guohao.util.Data;
import com.guohao.util.StringUtil;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class StartExamActivity extends FragmentActivity implements OnClickListener,OnDismissListener,OnPageChangeListener {
	private Activity mActivity;
	private SharedPreferences p;
	
	private Title customTitle;
	private TextView timeTitle;
	private TextView submit;
	
	private View alertCeng;
	private LinearLayout chooseTi;
	private TextView serialNumber;
	
	private ChooseOneTiFragment oneTiFragment;
	private ChooseMoreTiFragment moreTiFragment;
	private JudgeTiFragment judgeTiFragment;
	
	private Runnable r;
	private PopupWindow popupWindow;
	private ViewPager mViewPager;
	private SQLiteDatabase db;
	
	private View windowView;
	private TextView tiIndex;
	//键值对，键：第几个Fragment；值：试题主键id。
	private SparseIntArray tiArray;
	private int index = 0;
	
	private long startTime,endTime,examTime,nextTime;
	private Boolean onlyOnce = true;
	//final值
	private final String TAG_CHOOSE_TI = "tag_choose_ti";
	//存储TextView的，popuwindow里面的。
	private List<TextView> textViews;
	
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_exam);
		
		initView();
		initLocalData();
		initAdapter();
	}

	private void initAdapter() {
		mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return tiArray.size();
			}
			@Override
			public Fragment getItem(int index) {
				int dataId = tiArray.get(index);
				ExamTi examTi = getExamTi(dataId);
				if (examTi != null) {
					String[] answers = examTi.getSelectAnswers().split("\\|");
					switch (examTi.getExamTiType()) {
					case Data.CHOOSE_ONE_TI:
						oneTiFragment = new ChooseOneTiFragment();
						oneTiFragment.setTiContent((index+1)+". "+examTi.getContent());
						oneTiFragment.setTiList(answers);
						
						return oneTiFragment;
					case Data.CHOOSE_MORE_TI:
						moreTiFragment = new ChooseMoreTiFragment();
						moreTiFragment.setTiContent((index+1)+". "+examTi.getContent());
						moreTiFragment.setTiList(answers);
						
						return moreTiFragment;
					case Data.JUDGE_TI:
						judgeTiFragment = new JudgeTiFragment();
						judgeTiFragment.setTiContent((index+1)+". "+examTi.getContent());
						judgeTiFragment.setTiList(answers);
						
						return judgeTiFragment;
					}
					
				}else {
					Util.showToast(mActivity, "该题不存在！");
				}
				return null;
			}
		});
	}

	protected ExamTi getExamTi(int dataId) {
		ExamTi examTi = null;
		Cursor cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"examTiType","content","selectAnswers","chooseAnswer"}, "dataId=?", new String[]{dataId+""}, null, null, null);
		String examTiType = "";
		String content = "";
		String selectAnswers = "";
		String chooseAnswer = "";
		if (cursor.moveToFirst()) {
			examTiType = cursor.getString(cursor.getColumnIndex("examTiType"));
			content = cursor.getString(cursor.getColumnIndex("content"));
			selectAnswers = cursor.getString(cursor.getColumnIndex("selectAnswers"));
			chooseAnswer = cursor.getString(cursor.getColumnIndex("chooseAnswer"));
			examTi = new ExamTi(examTiType, content, selectAnswers,chooseAnswer);
		}
		cursor.close();
		return examTi;
	}

	private void initLocalData() {
		//查询试题的---主键id，存储到 HashMap 中。
		queryDataId(tiArray,Data.CHOOSE_ONE_TI);
		queryDataId(tiArray,Data.CHOOSE_MORE_TI);
		queryDataId(tiArray,Data.JUDGE_TI);
		
		//设置第几题 和 共几题
		serialNumber.setText("1/"+tiArray.size());
		tiIndex.setText("1/"+tiArray.size());
		
		//popuwindow，选择试题
		LinearLayout layout = (LinearLayout) windowView.findViewById(R.id.id_linearlayout_select_ti);
		DisplayMetrics metrics = Util.getDisplayMetrics(mActivity);
		int width = metrics.widthPixels;
		int cols = 7;
		int rows = (int) Math.ceil((double)tiArray.size()/(double)cols);
		int imagesize = width/cols;
		int count = 1;
		int lastRow = -1;
		if (tiArray.size() <= cols) {
			cols = tiArray.size();
		}else {
			lastRow = tiArray.size()%cols;
		}
		for (int i = 0; i < rows; i++) {
			LinearLayout layout2 = new LinearLayout(mActivity);
			if (i == rows-1 && lastRow != -1) {
				cols = lastRow;
			}
			for (int j = 0; j < cols; j++) {
				TextView t = new TextView(mActivity);
				t.setLayoutParams(new LayoutParams(imagesize, imagesize));
				t.setText(""+count++);
				t.setGravity(Gravity.CENTER);
				t.setTag(TAG_CHOOSE_TI);
				t.setOnClickListener(this);
				t.setBackgroundResource(R.drawable.img349);
				textViews.add(t);
				layout2.addView(t);
			}
			layout.addView(layout2);
		}
		
		
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

	private void queryDataId(SparseIntArray intArray, String tiType) {
		Cursor cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"dataId"}, "examTiType=?", new String[]{tiType}, null, null, null);
		while (cursor.moveToNext()) {
			intArray.append(index++, cursor.getInt(cursor.getColumnIndex("dataId")));
		}
		cursor.close();
	}

	private void initView() {
		mActivity = StartExamActivity.this;
		p = Util.getPreference(mActivity);
		tiArray = new SparseIntArray();
		
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.addOnPageChangeListener(this);
		
		//选择试题的弹出窗
		windowView = LayoutInflater.from(mActivity).inflate(R.layout.custom_select_ti, new FrameLayout(mActivity));
		tiIndex = (TextView) windowView.findViewById(R.id.id_textview_ti_index);
		
		
		db = Util.getDatabase(mActivity);
		textViews = new ArrayList<TextView>();
		
		customTitle = (Title) findViewById(R.id.id_custom_title);
		timeTitle = customTitle.getTitle();
		submit = customTitle.getTitleOther();
		submit.setOnClickListener(this);
		serialNumber = (TextView) findViewById(R.id.id_textview_num);
		alertCeng = findViewById(R.id.id_view_alert_ceng);
		chooseTi = (LinearLayout) findViewById(R.id.id_linearlayout_choose_ti);
		chooseTi.setOnClickListener(this);
		
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
		if (v.getTag() != null && v.getTag() instanceof String) {
			String tag = (String) v.getTag();
			if (tag.equals(TAG_CHOOSE_TI)) {
				mViewPager.setCurrentItem(Integer.valueOf(((TextView)v).getText().toString())-1);
			}
		}
		switch (v.getId()) {
		case R.id.id_textview_title_other:
			Util.showToast(mActivity, "交卷");
			break;
		case R.id.id_linearlayout_choose_ti:
			//这里---已答题，改变背景颜色---------------------------------------------
			for (int i = 0; i < tiArray.size(); i++) {
				Cursor cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"chooseAnswer"}, "dataId=?", new String[]{tiArray.get(i)+""}, null, null, null);
				while (cursor.moveToNext()) {
					String chooseAnswer = cursor.getString(cursor.getColumnIndex("chooseAnswer"));
					if (StringUtil.isEmpty(chooseAnswer)) {
						textViews.get(i).setBackgroundResource(R.drawable.img349);
					}else {
						textViews.get(i).setBackgroundResource(R.drawable.img348);
					}
				}
			}
			
			//弹出层
			alertCeng.setVisibility(View.VISIBLE);
			//点击 popup外面，dismiss。
			View dismissView = windowView.findViewById(R.id.id_view_dismiss);
			dismissView.setOnClickListener(this);
			
			popupWindow = new PopupWindow(windowView, 
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

	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int index) {
		//设置第几题 和 共几题
		serialNumber.setText((index+1)+"/"+tiArray.size());
		tiIndex.setText((index+1)+"/"+tiArray.size());
	}
	public int getDataId(int key) {
		return tiArray.get(key);
	}
}
