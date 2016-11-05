package com.guohao.schoolproject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
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
	private Boolean getExamPaperOK = true;
	
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
						JSONObject chooseOne = object.getJSONObject("��ѡ��");
						JSONObject chooseMore = object.getJSONObject("��ѡ��");
						JSONObject judge = object.getJSONObject("�ж���");
						//�ر���Ҫ---ɾ�����ݿ�---֮ǰ���ڵ�����---�Ծ�����
						//ɾ��֮ǰ���ڵ����⣬֮�������ε����⡣
						db.delete(Data.EXAM_PAPER_TABLE_NAME, null, null);
						
						saveExamTiInSqlite(chooseOne, Data.CHOOSE_ONE_TI);
						saveExamTiInSqlite(chooseMore, Data.CHOOSE_MORE_TI);
						saveExamTiInSqlite(judge, Data.JUDGE_TI);
						
						//�������ݿ�洢�Ƿ���������Ƿ���ת
						if (getExamPaperOK) {
							StartExamActivity.actionStart(mActivity);
						}else {
							Util.showToast(mActivity, "����������洢����");
						}
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

	protected void saveExamTiInSqlite(JSONObject tiObject, String tiType) {
		try {
			int examPaperId = p.getInt(Data.EXAM_PAPER_id, -1);
			int itemScore = tiObject.getInt("itemScore");
			int selectScore = tiObject.getInt("selectScore");
			ContentValues values = new ContentValues();
			JSONArray array = tiObject.getJSONArray("items");
			for (int i = 0; i < array.length(); i++) {
				tiObject = array.getJSONObject(i);
				int score = tiObject.getInt("score");
				int answerCount = tiObject.getInt("answerCount");
				String strandAnswer = tiObject.getString("strandAnswer");
				String selectAnswers = tiObject.getString("selectAnswers");
				String createAdmin = tiObject.getString("createAdmin");
				long createTime = tiObject.getLong("createTime");
				String course = tiObject.getString("course");
				int id = tiObject.getInt("id");
				String content = tiObject.getString("content");
				values.clear();
				values.put("examPaperId", examPaperId);
				values.put("itemScore", itemScore);
				values.put("selectScore", selectScore);
				values.put("examTiType", tiType);
				values.put("score", score);
				values.put("answerCount", answerCount);
				values.put("strandAnswer", strandAnswer);
				values.put("selectAnswers", selectAnswers);
				values.put("createAdmin", createAdmin);
				values.put("createTime", createTime);
				values.put("course", course);
				values.put("id", id);
				values.put("content", content);
				long rowId = db.insert(Data.EXAM_PAPER_TABLE_NAME, null, values);
				if (rowId == -1) {
					getExamPaperOK = false;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			getExamPaperOK = false;
		}
	}

	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		customTitle.setTitleText("��������");
		
		examName.setText(p.getString(Data.EXAM_PAPER_name, ""));
		examCourse.setText(p.getString(Data.EXAM_PAPER_course, ""));
		examClass.setText(p.getString(Data.EXAM_PAPER_classNo, ""));
		int tempInt = p.getInt(Data.EXAM_PAPER_examTime, -1);
		examTime.setText(tempInt == -1 ? "" : tempInt+"����");
		tempInt = p.getInt(Data.EXAM_PAPER_totalScore, -1);
		totalGrade.setText(tempInt == -1 ? "" : tempInt+"��");
		tempInt = p.getInt(Data.EXAM_PAPER_passScore, -1);
		passGrade.setText(tempInt == -1 ? "" : tempInt+"��");
		
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
		//������---��ʼ����---��ȡ�Ծ�id��Ӧ������---�洢�����ݿ�----ʹ��ʱ���ٶ�ȡ����
		//��ȡ����ɹ������洢�ɹ�����ת��������档
		//����---���ݿ�
		PackageManager manager = getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		MySqliteOpenHelper helper = new MySqliteOpenHelper(mActivity, Data.SCHOOL_PROJECT_DB, null, info.versionCode);
		db = helper.getReadableDatabase();
		//��������
		if (db != null) {
			Util.showAlertDialog04(mActivity, "������������......");
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
			Util.showToast(mActivity, "SchoolExam.db ���ݿⴴ��ʧ��");
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
