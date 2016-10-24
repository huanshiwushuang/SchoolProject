package com.guohao.schoolproject;

import java.util.ArrayList;
import java.util.List;

import com.guohao.adapter.AnswerShowAdapter;
import com.guohao.custom.Title;
import com.guohao.util.Data;
import com.guohao.util.Util;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AnswerShowActivity extends Activity implements OnRefreshListener<ListView>,OnItemClickListener {
	private static int flag = 0;
	public static final int Exam_Test = 1;
	public static final int Book_Read = 2;
	
	private Title customTitle;
	private int imageId;
	private Activity mActivity;
	private String testString;
	
	private List<Object[]> list;
	private AnswerShowAdapter adapter;
	private PullToRefreshListView pullListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_show);
		
		initView();
		initBaseData();
		//��������---ģ�����󵽵�����
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
		for (int i = 0; i < 15; i++) {
			list.add(new Object[]{imageId,testString});
		}
	}
	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		switch (flag) {
		case Exam_Test:
			customTitle.setTitleText("�Ծ�ѡ��");
			imageId = R.drawable.img42;
			testString = "�Ծ�ѡ��";
			break;
		case Book_Read:
			customTitle.setTitleText("�鼮��ȫ");
			imageId = R.drawable.img15;
			testString = "�鼮��ȫ";
			break;
		}
	}
	private void initView() {
		mActivity = AnswerShowActivity.this;
		list = new ArrayList<Object[]>();
		adapter = new AnswerShowAdapter(mActivity, R.layout.custom_answer_show_bg, list);
		customTitle = (Title) findViewById(R.id.id_custom_title);
		
		pullListView = (PullToRefreshListView) findViewById(R.id.id_pulllistview);
		pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		ILoadingLayout loadingLayout = pullListView.getLoadingLayoutProxy();
		loadingLayout.setPullLabel("����ˢ��");
		loadingLayout.setRefreshingLabel("����ˢ��...");
		loadingLayout.setReleaseLabel("�ɿ�ˢ��");
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
		Util.showToast(mActivity, "������˵ڣ�"+position+" ��");
		switch (flag) {
		case Exam_Test:
			ExamDetail.actionStart(mActivity);
			break;
		case Book_Read:
			ChapterListActivity.actionStart(mActivity);
			break;
		}
	}
}
