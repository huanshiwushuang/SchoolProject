package com.guohao.schoolproject;

import com.guohao.custom.Title;
import com.guohao.util.Data;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StartExamActivity extends Activity implements OnClickListener {
	private Activity mActivity;
	private SharedPreferences p;
	
	private Title customTitle;
	private TextView timeTitle;
	private TextView submit;
	
	private ImageView tiType;
	private TextView ti;
	private ListView listView;
	
	private Runnable r;
	
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

	private void initView() {
		mActivity = StartExamActivity.this;
		p = Util.getPreference(mActivity);
		
		customTitle = (Title) findViewById(R.id.id_custom_title);
		timeTitle = customTitle.getTitle();
		submit = customTitle.getTitleOther();
		
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
}
