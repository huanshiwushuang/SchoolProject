package com.guohao.schoolproject;

import java.util.ArrayList;
import java.util.List;

import com.guohao.custom.Title;
import com.guohao.entity.ExamTi;
import com.guohao.fragment.ChooseMoreTiFragment;
import com.guohao.fragment.ChooseOneTiFragment;
import com.guohao.fragment.JudgeTiFragment;
import com.guohao.java.MySqliteOpenHelper;
import com.guohao.util.Data;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.util.Log;
import android.util.SparseIntArray;
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

public class StartExamActivity extends FragmentActivity implements OnClickListener,OnDismissListener,OnPageChangeListener {
	private Activity mActivity;
	private SharedPreferences p;
	
	private Title customTitle;
	private TextView timeTitle;
	private TextView submit;
	
	private TextView ti;
	private ListView listView;
	private View alertCeng;
	private LinearLayout chooseTi;
	private TextView serialNumber;
	
	private ChooseOneTiFragment oneTiFragment;
	private ChooseMoreTiFragment moreTiFragment;
	private JudgeTiFragment judgeTiFragment;
	private int currentPage = 1;
	
	private Runnable r;
	private PopupWindow popupWindow;
	private ViewPager mViewPager;
	private SQLiteDatabase db;
	
	private View windowView;
	private TextView tiIndex;
	//��ֵ�ԣ������ڼ���Fragment��ֵ����������id��
	private SparseIntArray tiArray;
	private int index = 0;
	
	private long startTime,endTime,examTime,nextTime;
	private Boolean onlyOnce = true;
	
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
					Util.showToast(mActivity, "���ⲻ���ڣ�");
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
		//��ѯ�����---����id���洢�� HashMap �С�
		queryDataId(tiArray,Data.CHOOSE_ONE_TI);
		queryDataId(tiArray,Data.CHOOSE_MORE_TI);
		queryDataId(tiArray,Data.JUDGE_TI);
		
		//���õڼ��� �� ������
		serialNumber.setText("1/"+tiArray.size());
		tiIndex.setText("1/"+tiArray.size());
		
		customTitle.setImageVisibility(View.VISIBLE);
		submit.setText("����");
		startTime = p.getLong(Data.EXAM_PAPER_beginTime, -1);
		endTime = p.getLong(Data.EXAM_PAPER_endTime, -1);
		//����ת���ɺ���
		examTime = p.getInt(Data.EXAM_PAPER_examTime, -1)*60*1000;
		if (startTime != -1 && endTime != -1 && examTime != -1) {
			//��ʼ�������ʱ��+�������ĺ���ʱ��
			long currentTime = System.currentTimeMillis();
			long temp = currentTime+examTime;
			//�Ƚ��������ʱ��Ľ���ʱ��-��-ϵͳ��������ʱ�䣬ȡ��СֵΪ��ֹʱ��
			temp = Math.min(endTime, temp);
			//����õ�ʣ��Ŀɴ����ʱ��---������
			temp -= currentTime;
			//����õ�ʣ��Ŀɴ����ʱ��---����
			nextTime = temp/1000;
			handler.post(r);
		}else {
			Util.showToast(mActivity, "�Ծ�ɴ���ʱ���ȡʧ�ܣ�");
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
		
		windowView = LayoutInflater.from(mActivity).inflate(R.layout.custom_select_ti, new FrameLayout(mActivity));
		tiIndex = (TextView) windowView.findViewById(R.id.id_textview_ti_index);
		
		PackageManager manager = getPackageManager();
		int version = -1;
		try {
			version = manager.getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		db = new MySqliteOpenHelper(mActivity, Data.SCHOOL_PROJECT_DB, null, version).getReadableDatabase();
		
		customTitle = (Title) findViewById(R.id.id_custom_title);
		timeTitle = customTitle.getTitle();
		submit = customTitle.getTitleOther();
		submit.setOnClickListener(this);
		serialNumber = (TextView) findViewById(R.id.id_textview_num);
		alertCeng = findViewById(R.id.id_view_alert_ceng);
		chooseTi = (LinearLayout) findViewById(R.id.id_linearlayout_choose_ti);
		chooseTi.setOnClickListener(this);
		
		ti = (TextView) findViewById(R.id.id_textview_ti);
		listView = (ListView) findViewById(R.id.id_listview);
		
		handler = new Handler();
		r = new Runnable() {
			@Override
			public void run() {
				if (nextTime > 0) {
					long second = --nextTime;
					if (second <= 5*60 && onlyOnce) {
						Util.showToast(mActivity, "ʣ��ʱ�䲻������");
						onlyOnce = false;
					}
					String hours = second/60/60+"";
					String minutes = second/60%60+"";
					String seconds = second%60+"";
					hours = (hours.length() < 2 ? "0"+hours : hours );
					minutes = (minutes.length() < 2 ? "0"+minutes : minutes );
					seconds = (seconds.length() < 2 ? "0"+seconds : seconds );
					timeTitle.setText("����ʱ "+hours+":"+minutes+":"+seconds);
					if (nextTime > 0) {
						handler.postDelayed(this, 1*1000);
					}else {
						Util.showToast(mActivity, "ʱ�������");
					}
				}
			}
		};
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_textview_title_other:
			Util.showToast(mActivity, "����");
			break;
		case R.id.id_linearlayout_choose_ti:
			//������
			alertCeng.setVisibility(View.VISIBLE);
			
			
			//��� popup���棬dismiss��
			View dismissView = windowView.findViewById(R.id.id_view_dismiss);
			dismissView.setOnClickListener(this);
			
			popupWindow = new PopupWindow(windowView, 
					WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
			// ����popWindow��������ɵ������仰������ӣ�������true
			popupWindow.setFocusable(true);
			
			// ʵ����һ��ColorDrawable��ɫΪ��͸�� ---��Ҫ�ģ���������Ϊ�յ�ʱ�򣬲��ܹ����ؼ� dismiss
			ColorDrawable dw = new ColorDrawable(0x00000000);
			popupWindow.setBackgroundDrawable(dw);
			
			// ����popWindow����ʾ����ʧ����
			popupWindow.setAnimationStyle(R.style.StyleSelectPhoto);
			//����dismiss�����¼�
			popupWindow.setOnDismissListener(this);
			// �ڵײ���ʾ
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
		currentPage = index;
		//���õڼ��� �� ������
		serialNumber.setText((index+1)+"/"+tiArray.size());
		tiIndex.setText((index+1)+"/"+tiArray.size());
	}
}
