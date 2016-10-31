package com.guohao.schoolproject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guohao.Interface.HttpCallBack;
import com.guohao.adapter.AnswerShowAdapter;
import com.guohao.custom.Title;
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

public class AnswerShowActivity extends Activity implements OnRefreshListener<ListView>,OnItemClickListener {
	private static int flag = 0;
	public static final int Exam_Test = 1;
	public static final int Exam_Record = 2;
	public static final int My_Course = 3;
	
	private final int Get_Course_OK = 4;
	private final int Get_Course_Fail = 5;
	
	private Title customTitle;
	private int imageId;
	private Activity mActivity;
	private String testString;
	private SharedPreferences p;
	
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
						adapter.notifyDataSetChanged();
					}else {
						Util.showToast(mActivity, object.getString("msg"));
					}
				} catch (JSONException e) {
					Util.showToast(mActivity, e.toString());
				}
				break;
			case Get_Course_Fail:
				Util.dismiss();
				Util.showToast(mActivity, msg.obj.toString());
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
		switch (flag) {
		case Exam_Test:
			//??????????????????????????????????????????????????????????????????????
			break;
		case My_Course:
			String token = p.getString(Data.TOKEN, "");
			Util.showAlertDialog04(mActivity, "正在请求数据......");
			List<KV> mList = new ArrayList<KV>();
			mList.add(new KV("token", token));
			HttpURLConnection connection = HttpUtil.getGetHttpUrlConnection(Data.URL_GET_COURSE,mList);
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
		default:
			for (int i = 0; i < 15; i++) {
				list.add(new Object[]{imageId,testString});
			}
			break;
		}
		
	}
	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		switch (flag) {
		case Exam_Test:
			customTitle.setTitleText("试卷选择");
			imageId = R.drawable.img42;
			testString = "试卷选择";
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
		
		pullListView = (PullToRefreshListView) findViewById(R.id.id_pulllistview);
		pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		ILoadingLayout loadingLayout = pullListView.getLoadingLayoutProxy();
		loadingLayout.setPullLabel("下拉刷新");
		loadingLayout.setRefreshingLabel("正在刷新...");
		loadingLayout.setReleaseLabel("松开刷新");
		pullListView.setOnRefreshListener(this);
	}


	public static void actionStart(Context context, int flag) {
		Intent intent = new Intent(context, AnswerShowActivity.class);
		context.startActivity(intent);
		AnswerShowActivity.flag = flag;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				pullListView.onRefreshComplete();
			}
		}, 3*1000);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Util.showToast(mActivity, "您点击了第："+position+" 个");
		switch (flag) {
		case Exam_Test:
			ExamDetail.actionStart(mActivity);
			break;
		case Exam_Record:
			ChapterListActivity.actionStart(mActivity);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
}
