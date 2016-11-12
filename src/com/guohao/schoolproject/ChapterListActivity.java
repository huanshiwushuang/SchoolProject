package com.guohao.schoolproject;

import java.util.ArrayList;
import java.util.List;

import com.guohao.adapter.AnswerShowAdapter;
import com.guohao.custom.Title;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChapterListActivity extends Activity implements OnClickListener,OnItemClickListener {
	private Title customTitle;
	private TextView testSelf;
	private Activity mActivity;
	private ListView listview;
	private List<Object[]> list;
	private AnswerShowAdapter adapter;
	private int imageId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter_list);
		
		initView();
		initBaseData();
		initNetworkData();
		initAdapter();
		initListener();
	}

	private void initListener() {
		testSelf.setOnClickListener(this);
		listview.setOnItemClickListener(this);
	}
	private void initAdapter() {
		listview.setAdapter(adapter);
	}
	private void initNetworkData() {
		String testString = "书籍大全测试";
		for (int i = 0; i < 15; i++) {
			list.add(new Object[]{imageId,testString});
		}
	}
	private void initBaseData() {
		customTitle.setImageVisibility(View.VISIBLE);
		customTitle.setTitleText("章节列表");
		customTitle.setTitleOtherText("自测");
	}
	private void initView() {
		mActivity = ChapterListActivity.this;
		list = new ArrayList<Object[]>();
		imageId = R.drawable.img15;
		adapter = new AnswerShowAdapter(mActivity, R.layout.custom_answer_show_bg, list);
		
		customTitle = (Title) findViewById(R.id.id_custom_title);
		testSelf = customTitle.getTitleOther();
		listview = (ListView) findViewById(R.id.id_listview);
	}
	
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ChapterListActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		Util.showToast(mActivity, "自测");
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Util.showToast(mActivity, "选择了第："+position+"个");
	}
}
