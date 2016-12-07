package com.guohao.schoolproject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.adapter.AnswerShowAdapter;
import com.guohao.custom.MyAlertDialog;
import com.guohao.custom.Title;
import com.guohao.entity.ExamPaper;
import com.guohao.entity.ExamRecord;
import com.guohao.entity.KV;
import com.guohao.util.Data;
import com.guohao.util.HttpUtil;
import com.guohao.util.Util;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class AnswerShowActivity extends Activity implements OnItemClickListener {
	private static int flag = 0;
	public static final int Exam_Test = 1;
	public static final int Exam_Record = 2;
	public static final int My_Course = 3;
	
	private final int Get_Course_OK = 4;
	private final int Get_Course_Fail = 5;
	private final int Get_Exam_Paper_OK = 6;
	private final int Get_Exam_Paper_Fail = 7;
	private final int Get_Exam_Record_OK = 10;
	private final int Get_Exam_Record_Fail = 11;
	
	private Title customTitle;
	private int imageId;
	private Activity mActivity;
	private String testString;
	private SharedPreferences p;
	private String token;
	
	private ILoadingLayout loadingLayout;
	
	//用户点击的试卷是哪一个，记录下来，在答题之后清除对应试卷，防止重复多次答题。
	private int clickExamPaper = -1;
	//显示---没有更多了
	private TextView promptTV;
	
	//考试记录---加载的页码
	private int examRecordPage = 1;
	//保存考试记录的---list
	private List<ExamRecord> examRecords;
	
	private List<ExamPaper> examPapers;
	
	private List<Object[]> list;
	private AnswerShowAdapter adapter;
	private PullToRefreshListView pullListView;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Get_Course_OK:
				Util.dismiss();
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					String status = object.getString("status");
					if (status.equals("1")) {
						JSONArray array = object.getJSONArray("courses");
						for (int i = 0; i < array.length(); i++) {
							object = array.getJSONObject(i);
							testString = object.getString("course")+"（"+object.getString("name")+"）";
							list.add(new Object[]{imageId,testString});
						}
						if (list.size() <= 0) {
							promptTV.setText("没有更多了");
						}else {
							promptTV.setText("");
						}
						adapter.notifyDataSetChanged();
					}else {
						promptTV.setText(object.getString("msg"));
					}
				} catch (JSONException e) {
					promptTV.setText(e.toString());
				}
				break;
			case Get_Course_Fail:
				Util.dismiss();
				promptTV.setText(msg.obj.toString());
				break;
			case Get_Exam_Paper_OK:
				Util.dismiss();
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					String status = object.getString("status");
					if (status.equals("1")) {
						JSONArray array = object.getJSONArray("exams");
						for (int i = 0; i < array.length(); i++) {
							object = array.getJSONObject(i);
							Boolean isDelete,isSelectType;
							int totalScore,passScore,mode,id,examTime;
							String createAdmin,name,classNo,course,descript;
							long createTime,beginTime,endTime;
							
							isDelete = object.getBoolean("isDelete");
							totalScore = object.getInt("totalScore");
							passScore = object.getInt("passScore");
							mode = object.getInt("mode");
							createAdmin = object.getString("createAdmin");
							createTime = object.getLong("createTime");
							isSelectType = object.getBoolean("isSelectType");
							name = object.getString("name");
							classNo = object.getString("classNo");
							course = object.getString("course");
							id = object.getInt("id");
							beginTime = object.getLong("beginTime");
							endTime = object.getLong("endTime");
							examTime = object.getInt("examTime");
							descript = object.getString("descript");
							
							ExamPaper paper = new ExamPaper(isDelete, totalScore, passScore, mode, 
													createAdmin, createTime, isSelectType, name, classNo, 
													course, id, beginTime, endTime, examTime, descript);
							examPapers.add(paper);
							list.add(new Object[]{imageId,name});
						}
						if (list.size() <= 0) {
							promptTV.setText("没有更多了");
						}else {
							promptTV.setText("");
						}
						adapter.notifyDataSetChanged();
					}else {
						promptTV.setText(object.getString("msg"));
					}
				} catch (JSONException e) {
					promptTV.setText(e.toString());
				}
				break;
			case Get_Exam_Paper_Fail:
				Util.dismiss();
				promptTV.setText(msg.obj.toString());
				break;
			case Get_Exam_Record_OK:
				Util.dismiss();
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					String status = object.getString("status");
					if (status.equals("1")) {
						JSONArray array = object.getJSONArray("records");
						if (array.length() <= 0) {
							if (list.size() <= 0) {
								promptTV.setText("没有更多了");
							}else {
								Util.showToast(mActivity, "没有更多了");
							}
						}else {
							promptTV.setText("");
							for (int i = 0; i < array.length(); i++) {
								object = array.getJSONObject(i);
								testString = object.getString("examName");
								list.add(new Object[]{imageId,testString});
								examRecords.add(new ExamRecord(object.getString("stuNumber"),
																object.getString("score"), 
																object.getString("examName"), 
																object.getString("course"), 
																object.getString("beginTime"), 
																object.getString("endTime"), 
																object.getString("isPass"), 
																object.getString("examTime")));
							}
							adapter.notifyDataSetChanged();
							//数据加载成功，且有数据，当前的page +1，下拉刷新的时候，本体的增加，这里增加。
							examRecordPage++;
						}
					}else {
						Util.showToast(mActivity, object.getString("msg"));
					}
				} catch (JSONException e) {
					Util.showToast(mActivity, e.toString());
				}
				pullListView.onRefreshComplete();
				break;
			case Get_Exam_Record_Fail:
				Util.dismiss();
				Util.showToast(mActivity, msg.obj.toString());
				pullListView.onRefreshComplete();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_show);
		
		initView();
		initBaseData();
		//测试所用---模拟请求到的数据
		initNetworkData();
		initAdapter();
		initListener();
	}
	
	private void initListener() {
		pullListView.setOnItemClickListener(this);
	}
	private void initAdapter() {
		pullListView.setAdapter(adapter);
	}
	private void initNetworkData() {
		List<KV> mList = null;
		HttpURLConnection connection = null;
		switch (flag) {
		case Exam_Test:
			Util.showAlertDialog04(mActivity, "正在请求数据......");
			mList = new ArrayList<KV>();
			mList.add(new KV("token", token));
			connection = HttpUtil.getGetHttpUrlConnection(Data.URL_GET_EXAM_PAPER,mList);
			HttpUtil.requestData(connection, null, new HttpCallBack() {
				@Override
				public void onFinish(Object object) {
					Message msg = handler.obtainMessage();
					msg.what = Get_Exam_Paper_OK;
					msg.obj = object;
					handler.sendMessage(msg);
				}
				@Override
				public void onError(Object object) {
					Message msg = handler.obtainMessage();
					msg.what = Get_Exam_Paper_Fail;
					msg.obj = object;
					handler.sendMessage(msg);
				}
			});
			break;
		case My_Course:
			Util.showAlertDialog04(mActivity, "正在请求数据......");
			mList = new ArrayList<KV>();
			mList.add(new KV("token", token));
			connection = HttpUtil.getGetHttpUrlConnection(Data.URL_GET_COURSE,mList);
			HttpUtil.requestData(connection, null, new HttpCallBack() {
				@Override
				public void onFinish(Object object) {
					Message msg = handler.obtainMessage();
					msg.what = Get_Course_OK;
					msg.obj = object;
					handler.sendMessage(msg);
				}
				@Override
				public void onError(Object object) {
					Message msg = handler.obtainMessage();
					msg.what = Get_Course_Fail;
					msg.obj = object;
					handler.sendMessage(msg);
				}
			});
			break;
		case Exam_Record:
			Util.showAlertDialog04(mActivity, "正在请求数据......");
			getExamRecord(examRecordPage);
			break;
		}
	}
	private void getExamRecord(int page) {
		List<KV> mList = new ArrayList<KV>();
		mList.add(new KV("token", token));
		mList.add(new KV("page", page));
		HttpURLConnection connection = HttpUtil.getPostHttpUrlConnection(Data.URL_GET_EXAM_RECORD);
		HttpUtil.requestData(connection, mList, new HttpCallBack() {
			@Override
			public void onFinish(Object object) {
				Message msg = handler.obtainMessage();
				msg.what = Get_Exam_Record_OK;
				msg.obj = object;
				handler.sendMessage(msg);
			}
			@Override
			public void onError(Object object) {
				Message msg = handler.obtainMessage();
				msg.what = Get_Exam_Record_Fail;
				msg.obj = object;
				handler.sendMessage(msg);
			}
		});
	}

	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		token = p.getString(Data.TOKEN, "");
		switch (flag) {
		case Exam_Test:
			customTitle.setTitleText("试卷选择");
			imageId = R.drawable.img42;
			testString = "试卷选择";
			//存储---请求到的试卷。
			examPapers = new ArrayList<ExamPaper>();
			break;
		case Exam_Record:
			customTitle.setTitleText("考试记录");
			imageId = R.drawable.img15;
			testString = "考试记录";
			break;
		case My_Course:
			customTitle.setTitleText("我的课程");
			imageId = R.drawable.img347;
			testString = "我的课程";
			adapter.setDisplayNext(false);
			break;
		}
	}
	private void initView() {
		mActivity = AnswerShowActivity.this;
		p = Util.getPreference(mActivity);
		list = new ArrayList<Object[]>();
		adapter = new AnswerShowAdapter(mActivity, R.layout.custom_answer_show_bg, list);
		customTitle = (Title) findViewById(R.id.id_custom_title);
		
		promptTV = (TextView) findViewById(R.id.id_textview_no_have_more);
		
		pullListView = (PullToRefreshListView) findViewById(R.id.id_pulllistview);
		
		
		switch (flag) {
		case Exam_Record:
			examRecords = new ArrayList<ExamRecord>();
			
			pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
			loadingLayout = pullListView.getLoadingLayoutProxy(false, true);
			loadingLayout.setPullLabel("上拉加载");
			loadingLayout.setRefreshingLabel("正在加载...");
			loadingLayout.setReleaseLabel("松开加载");
			pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					getExamRecord(examRecordPage+1);
				}
			});
			break;
		default:
			pullListView.setMode(PullToRefreshBase.Mode.DISABLED);
			break;
		}
	}


	public static void actionStart(Context context, int flag) {
		Intent intent = new Intent(context, AnswerShowActivity.class);
		context.startActivity(intent);
		AnswerShowActivity.flag = flag;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= 1;
		switch (flag) {
		case Exam_Test:
			//这个点击的位置----一定要写对位置，否则如果是继续答题，就直接跳转了。
			clickExamPaper = position;
			//如果还有尚未答完的试题，询问是否跳转，采用标识位
			Boolean isComplete = p.getBoolean(Data.EXAM_PAPER_IS_COMPLETE, true);
			if (!isComplete) {
				Util.showAlertDialog05(mActivity, "答题尚未完成，是否继续？", MyAlertDialog.Layout06);
				return;
			}
			
			ExamPaper examPaper = examPapers.get(position);
			Editor editor = p.edit();
			editor.putBoolean(Data.EXAM_PAPER_isDelete, examPaper.getIsDelete());
			editor.putInt(Data.EXAM_PAPER_totalScore, examPaper.getTotalScore());
			editor.putInt(Data.EXAM_PAPER_passScore, examPaper.getPassScore());
			editor.putInt(Data.EXAM_PAPER_mode, examPaper.getMode());
			editor.putString(Data.EXAM_PAPER_createAdmin, examPaper.getCreateAdmin());
			editor.putLong(Data.EXAM_PAPER_createTime, examPaper.getCreateTime());
			editor.putBoolean(Data.EXAM_PAPER_isSelectType, examPaper.getIsSelectType());
			editor.putString(Data.EXAM_PAPER_name, examPaper.getName());
			editor.putString(Data.EXAM_PAPER_classNo, examPaper.getClassNo());
			editor.putString(Data.EXAM_PAPER_course, examPaper.getCourse());
			editor.putInt(Data.EXAM_PAPER_id, examPaper.getId());
			editor.putLong(Data.EXAM_PAPER_beginTime, examPaper.getBeginTime());
			editor.putLong(Data.EXAM_PAPER_endTime, examPaper.getEndTime());
			editor.putInt(Data.EXAM_PAPER_examTime, examPaper.getExamTime());
			editor.putString(Data.EXAM_PAPER_descript, examPaper.getDescript());
			editor.commit();
			ExamDetailActivity.actionStart(mActivity);
			break;
		case Exam_Record:
			ExamRecord examRecord = examRecords.get(position);
			Editor edit = p.edit();
			String isPass = "";
			if (examRecord.getIsPass().equals("true")) {
				isPass = "是";
			}else {
				isPass = "否";
			}
			edit.putString(Data.EXAM_PAPER_course_name, examRecord.getCourse());
			edit.putString(Data.EXAM_PAPER_exam_paper_name, examRecord.getExamName());
			edit.putString(Data.EXAM_PAPER_student_id, examRecord.getStuNumber());
			edit.putString(Data.EXAM_PAPER_grade, examRecord.getScore());
			edit.putString(Data.EXAM_PAPER_start_time, examRecord.getBeginTime());
			edit.putString(Data.EXAM_PAPER_end_time, examRecord.getEndTime());
			edit.putString(Data.EXAM_PAPER_is_pass, isPass);
			edit.putString(Data.EXAM_PAPER_use_time, examRecord.getExamTime());
			edit.commit();
			ChapterListActivity.actionStart(mActivity);
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//如果提交了试卷之后，回到这个试卷展示页面，需要移除之前答过的试卷
		if (p.getBoolean(Data.EXAM_PAPER_IS_SUBMIT, false)) {
			if (clickExamPaper > -1 && clickExamPaper < list.size()) {
				list.remove(clickExamPaper);
				examPapers.remove(clickExamPaper);
				//如果提交试卷后，没有试卷了，需提示。
				if (list.size() <= 0) {
					promptTV.setText("没有更多了");
				}
				//刷新界面
				adapter.notifyDataSetChanged();
				//将是否提交新试卷---重置为false
				Editor editor = p.edit();
				editor.putBoolean(Data.EXAM_PAPER_IS_SUBMIT, false);
				editor.commit();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

}
