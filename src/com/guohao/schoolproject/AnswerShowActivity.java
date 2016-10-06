package com.guohao.schoolproject;

import java.util.ArrayList;
import java.util.List;

import com.guohao.adapter.AnswerShowAdapter;
import com.guohao.custom.AnswerShowBg;
import com.guohao.custom.Title;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AnswerShowActivity extends Activity {
	private static int flag = 0;
	public static final int Exam_Test = 1;
	public static final int Book_Read = 2;
	public static final int Error_Content = 3;
	public static final int My_Record = 4;
	
	private Title customTitle;
	private int imageId;
	private LinearLayout allLayout;
	private ListView listView;
	private Activity mActivity;
	private String testString;
	private View view;
	
	private List<Object[]> list;
	private AnswerShowAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_show);
		
		initView();
		initData();
		//��������---ģ�����󵽵�����
		initNetworkData();
		initAdapter();
	}
	
	private void initAdapter() {
		listView.setAdapter(adapter);
	}
	private void initNetworkData() {
		for (int i = 0; i < 15; i++) {
			list.add(new Object[]{imageId,testString});
		}
	}
	private void initData() {
		customTitle.setImageVisibility(View.VISIBLE);
		switch (flag) {
		case Exam_Test:
			customTitle.setTitleText("�Ծ�ѡ��");
			imageId = R.drawable.img42;
			view.setVisibility(View.VISIBLE);
			testString = "�Ծ�ѡ��";
			break;
		case Book_Read:
			customTitle.setTitleText("�鼮��ȫ");
			imageId = R.drawable.img15;
			view.setVisibility(View.VISIBLE);
			testString = "�鼮��ȫ";
			break;
		case Error_Content:
			customTitle.setTitleText("�����鼮");
			imageId = R.drawable.img63;
			testString = "�����鼮";
			break;
		case My_Record:
			customTitle.setTitleText("���Լ�¼");
			imageId = R.drawable.img56;
			allLayout.setBackgroundColor(Color.parseColor("#ffffff"));
			testString = "���Լ�¼";
			break;
		}
	}
	private void initView() {
		mActivity = AnswerShowActivity.this;
		list = new ArrayList<Object[]>();
		adapter = new AnswerShowAdapter(mActivity, R.layout.custom_answer_show_bg, list);
		customTitle = (Title) findViewById(R.id.id_custom_title);
		allLayout = (LinearLayout) findViewById(R.id.id_linearlayout);
		listView = (ListView) findViewById(R.id.id_listview);
		view = findViewById(R.id.id_view_space);
	}


	public static void actionStart(Context context, int flag) {
		Intent intent = new Intent(context, AnswerShowActivity.class);
		context.startActivity(intent);
		AnswerShowActivity.flag = flag;
	}
}
