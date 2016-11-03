package com.guohao.schoolproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.custom.Title;
import com.guohao.entity.KV;
import com.guohao.java.MySqliteOpenHelper;
import com.guohao.util.Data;
import com.guohao.util.HttpUtil;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ExamDetailActivity extends Activity {
	private Title customTitle;
	private Activity mActivity;
	private TextView examName,examCourse,examClass,examTime,totalGrade,passGrade,startTime,endTime,examDescription;
	private SharedPreferences p;
	private SQLiteDatabase db;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HttpCallBack.CALL_BACK_OK:
				Util.dismiss();
				String data = msg.obj.toString();
				try {
					JSONObject object = new JSONObject(data);
					String status = object.getString("status");
					if (status.equals("1")) {
						object = object.getJSONObject("items");
						JSONObject chooseOne = object.getJSONObject("单选题");
						JSONObject chooseMore = object.getJSONObject("多选题");
						JSONObject judge = object.getJSONObject("判断题");
						Log.d("guohao", "单选题："+chooseOne.toString());
						Log.d("guohao", "多选题："+chooseMore.toString());
						Log.d("guohao", "判断题："+judge.toString());
					}else {
						Util.showToast(mActivity, object.getString("msg"));
					}
				} catch (JSONException e) {
					Util.showToast(mActivity, e.toString());
				}
				
				break;
			case HttpCallBack.CALL_BACK_FAIL:
				Util.dismiss();
				Util.showToast(mActivity, msg.obj.toString());
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examdetail);
		
		initView();
		initBaseData();
	}

	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		customTitle.setTitleText("考试详情");
		
		examName.setText(p.getString(Data.EXAM_PAPER_name, ""));
		examCourse.setText(p.getString(Data.EXAM_PAPER_course, ""));
		examClass.setText(p.getString(Data.EXAM_PAPER_classNo, ""));
		int tempInt = p.getInt(Data.EXAM_PAPER_examTime, -1);
		examTime.setText(tempInt == -1 ? "" : tempInt+"分钟");
		tempInt = p.getInt(Data.EXAM_PAPER_totalScore, -1);
		totalGrade.setText(tempInt == -1 ? "" : tempInt+"分");
		tempInt = p.getInt(Data.EXAM_PAPER_passScore, -1);
		passGrade.setText(tempInt == -1 ? "" : tempInt+"分");
		
		long tempLong = p.getLong(Data.EXAM_PAPER_beginTime, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("zh", "CN"));
		if (tempLong != -1) {
			startTime.setText(format.format(tempLong));
		}
		
		tempLong = p.getLong(Data.EXAM_PAPER_endTime, -1);
		if (tempLong != -1) {
			endTime.setText(format.format(tempLong));
		}
		
		examDescription.setText(p.getString(Data.EXAM_PAPER_descript, ""));
	}

	private void initView() {
		customTitle = (Title) findViewById(R.id.id_custom_title);
		mActivity = ExamDetailActivity.this;
		p = Util.getPreference(mActivity);
		
		examName = (TextView) findViewById(R.id.id_textview_name);
		examCourse = (TextView) findViewById(R.id.id_textview_course);
		examClass = (TextView) findViewById(R.id.id_textview_class);
		examTime = (TextView) findViewById(R.id.id_textview_time);
		totalGrade = (TextView) findViewById(R.id.id_textview_total_grade);
		passGrade = (TextView) findViewById(R.id.id_textview_pass_grade);
		startTime = (TextView) findViewById(R.id.id_textview_start_time);
		endTime = (TextView) findViewById(R.id.id_textview_end_time);
		examDescription = (TextView) findViewById(R.id.id_textview_description);

	}
	
	public void startExam(View view) {
		//这里点击---开始考试---获取试卷id对应的试题---存储到数据库----使用时，再读取出来
		//获取试题成功，并存储成功才跳转到答题界面。
//		StartExamActivity.actionStart(mActivity);
		//创建---数据库
		PackageManager manager = getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		MySqliteOpenHelper helper = new MySqliteOpenHelper(mActivity, "SchoolExam.db", null, info.versionCode);
		db = helper.getReadableDatabase();
		//请求数据
		if (db != null) {
			Util.showAlertDialog04(mActivity, "正在请求试题......");
			List<KV> list = new ArrayList<KV>();
			list.add(new KV("token", p.getString(Data.TOKEN, "")));
			list.add(new KV("examId", p.getInt(Data.EXAM_PAPER_id, -1)+""));
			HttpURLConnection connection = HttpUtil.getGetHttpUrlConnection(Data.URL_GET_EXAM_TI, list);
			HttpUtil.requestData(connection, null, new HttpCallBack() {
				@Override
				public void onFinish(Object object) {
					Message msg = handler.obtainMessage();
					msg.what = HttpCallBack.CALL_BACK_OK;
					msg.obj = object;
					handler.sendMessage(msg);
				}
				@Override
				public void onError(Object object) {
					Message msg = handler.obtainMessage();
					msg.what = HttpCallBack.CALL_BACK_FAIL;
					msg.obj = object;
					handler.sendMessage(msg);
				}
			});
		}else {
			Util.showToast(mActivity, "SchoolExam.db 数据库创建失败");
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ExamDetailActivity.class);
		context.startActivity(intent);
	}
}
