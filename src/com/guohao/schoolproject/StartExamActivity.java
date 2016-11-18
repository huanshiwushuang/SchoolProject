package com.guohao.schoolproject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.custom.Title;
import com.guohao.entity.ExamTi;
import com.guohao.entity.KV;
import com.guohao.fragment.ChooseMoreTiFragment;
import com.guohao.fragment.ChooseOneTiFragment;
import com.guohao.fragment.JudgeTiFragment;
import com.guohao.util.Data;
import com.guohao.util.HttpUtil;
import com.guohao.util.StringUtil;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.ViewGroup;
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
	//当前页码
	private int currentPage = 0;
	//答题最后总成绩
	private int score;
	//答题用时
	private String useTime;
	//答题合格与否
	private int isPass;
	
	private Cursor cursor;
	
	private long startTime,endTime,examTime,nextTime;
	private Boolean onlyOnce = true;
	//final值
	private final String TAG_CHOOSE_TI = "tag_choose_ti";
	//存储TextView的，popuwindow里面的。
	private List<TextView> textViews;
	
	private Handler handler;
	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String msgString = msg.obj.toString();;
			switch (msg.what) {
			case HttpCallBack.CALL_BACK_OK:
				Util.dismiss();
				try {
					JSONObject object = new JSONObject(msgString);
					String status = object.getString("status");
					Util.showToast(mActivity, object.getString("msg"));
					if (status.equals("1")) {
						//提交成功之后，将试卷删除，避免第二次作答
						db.delete(Data.EXAM_PAPER_TABLE_NAME, null, null);
						ExamGrade.actionStart(mActivity, isPass, score, useTime);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case HttpCallBack.CALL_BACK_FAIL:
				Util.dismiss();
				Util.showToast(mActivity, msgString);
				break;
			}
			
		}
	};
	//getter---setter
	public int getCurrentPage() {
		return currentPage;
	}
	
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
		cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"examTiType","content","selectAnswers","chooseAnswer"}, "dataId=?", new String[]{dataId+""}, null, null, null);
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
		if (cursor != null) {
			cursor.close();
		}
		return examTi;
	}

	private void initLocalData() {
		//开始考试，将考试结束---置为false。
		Editor editor = p.edit();
		editor.putBoolean(Data.EXAM_PAPER_IS_COMPLETE, false);
		editor.commit();
		
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
		
		//初始化时---先遍历一次答过的题，用于继续答题直接进入的时候。
		//注意：必须在 textviews 这个选项卡的视图---创建以后。
		Cursor cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"chooseAnswer"}, null, null, null, null, "dataId asc");
		int j = 0;
		while (cursor != null && cursor.moveToNext()) {
			String choose = cursor.getString(cursor.getColumnIndex("chooseAnswer"));
			if (!StringUtil.isEmpty(choose)) {
				textViews.get(j).setBackgroundResource(R.drawable.img348);
			}
			j++;
		}
		if (cursor != null) {
			cursor.close();
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
			long i = p.getLong(Data.EXAM_PAPER_NEXT_TIME, -1);
			if (i == -1) {
				nextTime = temp/1000;
			}else {
				nextTime = i;
			}
			handler.post(r);
		}else {
			Util.showToast(mActivity, "试卷可答题时间获取失败！");
			finish();
		}
	}

	private void queryDataId(SparseIntArray intArray, String tiType) {
		cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"dataId"}, "examTiType=?", new String[]{tiType}, null, null, null);
		while (cursor.moveToNext()) {
			intArray.append(index++, cursor.getInt(cursor.getColumnIndex("dataId")));
		}
		if (cursor != null) {
			cursor.close();
		}
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
						Util.showToast(mActivity, "剩余时间不多啦！---"+second+"---"+nextTime+"---"+(5*60));
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
						//提交试卷，计算分数。
						Util.showToast(mActivity, "考试时间结束！");
						submitExamPaper();
					}
				}
			}
		};
	}
	
	public void submitExamPaper() {
		Util.showAlertDialog04(mActivity, "正在提交试卷......");
		//最后的答题总成绩
		score = 0;
		for (int i = 0; i < tiArray.size(); i++) {
			int dataId = tiArray.get(i);
			cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"itemScore","strandAnswer","chooseAnswer"}, "dataId=?", new String[]{dataId+""}, null, null, null);
			if (cursor.moveToNext()) {
				int itemScore = cursor.getInt(cursor.getColumnIndex("itemScore"));
				String strandAnswer = cursor.getString(cursor.getColumnIndex("strandAnswer"));
				String chooseAnswer = cursor.getString(cursor.getColumnIndex("chooseAnswer"));
				if (chooseAnswer != null && strandAnswer != null) {
					if (chooseAnswer.equals(strandAnswer)) {
						score += itemScore;
					}
				}
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		String token = p.getString(Data.TOKEN, "");
		int examId = p.getInt(Data.EXAM_PAPER_id, -1);
		int passScore = p.getInt(Data.EXAM_PAPER_passScore, -1);
		isPass = (score >= passScore && passScore != -1) ? 1 : 0;
		long beginTime = Util.getPreference(mActivity).getLong(Data.EXAM_PAPER_START_TIME, -1);
		long endTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault());
		String startDateTime = format.format(new Date(beginTime));
		String endDateTime = format.format(new Date(endTime));
		useTime = ((endTime-beginTime)/1000)/60+"分"+((endTime-beginTime)/1000)%60+"秒";
		
		List<KV> list = new ArrayList<KV>();
		list.add(new KV("token", token));
		list.add(new KV("examId", examId+""));
		list.add(new KV("score", score+""));
		list.add(new KV("isPass", isPass+""));
		list.add(new KV("beginTime", startDateTime));
		list.add(new KV("endTime", endDateTime));
		list.add(new KV("examTime", useTime));
		
		HttpUtil.requestData(HttpUtil.getPostHttpUrlConnection(Data.URL_POST_EXAM_PAPER_GRADE), list, new HttpCallBack() {
			@Override
			public void onFinish(Object object) {
				Message msg = handler2.obtainMessage();
				msg.what = HttpCallBack.CALL_BACK_OK;
				msg.obj = object;
				handler2.sendMessage(msg);
			}
			
			@Override
			public void onError(Object object) {
				Message msg = handler2.obtainMessage();
				msg.what = HttpCallBack.CALL_BACK_FAIL;
				msg.obj = object;
				handler2.sendMessage(msg);
			}
		});
		
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
			//提交试卷，计算分数。
			Cursor cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"chooseAnswer"}, "chooseAnswer=? or chooseAnswer is null", new String[]{""}, null, null, null);
			String string = "是否提交试卷？";
			if (cursor != null && cursor.moveToNext()) {
				string = "答题尚未完成，"+string;
			}
			Util.showAlertDialog05(mActivity, string);
			break;
		case R.id.id_linearlayout_choose_ti:
			//设置当前选中的题的背景颜色
			textViews.get(currentPage).setBackgroundResource(R.drawable.img350);
			
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
			//????????????????????????????????????????????????????????????????????
			if(windowView != null && windowView.getParent() != null) {
				((ViewGroup)windowView.getParent()).removeAllViews();
			}
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
		handler2.removeCallbacksAndMessages(null);
		handler.removeCallbacksAndMessages(null);
		db.close();
		//如果中途答题Activity被销毁，记录剩余时间，下一次接着做
		Editor editor = p.edit();
		editor.putLong(Data.EXAM_PAPER_NEXT_TIME, nextTime);
		editor.commit();
	}
	@Override
	public void onBackPressed() {
		Util.showAlertDialog07(mActivity, "是否放弃本次答题？");
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
		//更改背景视图
		setItemColor(currentPage);
		textViews.get(index).setBackgroundResource(R.drawable.img350);
		cursor.close();
		
		currentPage = index;
		//设置第几题 和 共几题
		serialNumber.setText((index+1)+"/"+tiArray.size());
		tiIndex.setText((index+1)+"/"+tiArray.size());
	}
	private void setItemColor(int currentIndex) {
		cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"chooseAnswer"}, "dataId=?", new String[]{tiArray.get(currentIndex)+""}, null, null, null);
		int resid = R.drawable.img349;
		if (cursor != null && cursor.moveToNext()) {
			if (!StringUtil.isEmpty(cursor.getString(cursor.getColumnIndex("chooseAnswer")))) {
				resid = R.drawable.img348;
			}
		}
		textViews.get(currentIndex).setBackgroundResource(resid);
	}
	public List<TextView> getTextViewList() {
		return textViews;
	}
	public int getDataId(int key) {
		return tiArray.get(key);
	}
}
