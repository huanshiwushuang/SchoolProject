package com.guohao.fragment;

import java.util.ArrayList;
import java.util.List;

import com.guohao.adapter.MyTiAdapter;
import com.guohao.schoolproject.R;
import com.guohao.schoolproject.StartExamActivity;
import com.guohao.util.Data;
import com.guohao.util.StringUtil;
import com.guohao.util.Util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseOneTiFragment extends Fragment implements OnItemClickListener {
	private View view;
	private TextView tiContent;
	private ListView listview;
	private List<String[]> list;
	
	private String tiString;
	private String[] answers;
	
	private MyTiAdapter myTiAdapter;
	private SQLiteDatabase db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_choose_one_ti, null);
		initView();
		return view;
	}

	private void initView() {
		tiContent = (TextView) view.findViewById(R.id.id_textview_ti);
		listview = (ListView) view.findViewById(R.id.id_listview);
		listview.setOnItemClickListener(this);
		list = new ArrayList<String[]>();
		db = Util.getDatabase(getActivity());
		myTiAdapter = new MyTiAdapter(getContext(), R.layout.custom_exam_ti_radio_bg, list);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initBaseData();
		//恢复已选中的选项
		initLocalData();
	}
	
	private void initBaseData() {
		tiContent.setText(tiString);
		for (int i = 0; i < answers.length; i++) {
			list.add(new String[]{answers[i],"0"});
		}
		listview.setAdapter(myTiAdapter);
	}
	private void initLocalData() {
		int dianIndex = tiContent.getText().toString().indexOf(".");
		int currentIndex = Integer.valueOf(tiContent.getText().toString().substring(0, dianIndex))-1;
		Cursor cursor = db.query(Data.EXAM_PAPER_TABLE_NAME, new String[]{"chooseAnswer"}, "dataId=?", new String[]{((StartExamActivity)getActivity()).getDataId(currentIndex)+""}, null, null, null);
		String chooseAnswer = "";
		while (cursor.moveToNext()) {
			chooseAnswer = cursor.getString(cursor.getColumnIndex("chooseAnswer"));
		}
		if (!StringUtil.isEmpty(chooseAnswer)) {
			String[] tempArray = chooseAnswer.split("\\|");
			for (int i = 0; i < tempArray.length; i++) {
				list.get(Integer.valueOf(tempArray[i]))[1] = "1";
			}
			myTiAdapter.notifyDataSetChanged();
		}
	}
	public void setTiContent(String content) {
		tiString = content;
	}
	public void setTiList(String[] strings) {
		answers = strings;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i)[1] = "0";
		}
		list.get(position)[1] = "1";
		myTiAdapter.notifyDataSetChanged();
		//保存答案到数据库
		ContentValues values = new ContentValues();
		values.put("chooseAnswer", position);
		int dianIndex = tiContent.getText().toString().indexOf(".");
		int currentIndex = Integer.valueOf(tiContent.getText().toString().substring(0, dianIndex))-1;
		db.update(Data.EXAM_PAPER_TABLE_NAME, values, "dataId=?", new String[]{((StartExamActivity)getActivity()).getDataId(currentIndex)+""});
		//更改切换选项卡的背景图片
		((StartExamActivity)getActivity()).getTextViewList().get(((StartExamActivity)getActivity()).getCurrentPage()).setBackgroundResource(R.drawable.img348);
	}
}
