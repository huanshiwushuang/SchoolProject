package com.guohao.fragment;

import java.util.ArrayList;
import java.util.List;

import com.guohao.adapter.MyTiAdapter;
import com.guohao.schoolproject.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseMoreTiFragment extends Fragment {
	private View view;
	private TextView tiContent;
	private ListView listview;
	private List<String> list;
	
	private String tiString;
	private String[] answers;
	
	private MyTiAdapter myTiAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_choose_more_ti, null);
		tiContent = (TextView) view.findViewById(R.id.id_textview_ti);
		listview = (ListView) view.findViewById(R.id.id_listview);
		list = new ArrayList<String>();
		myTiAdapter = new com.guohao.adapter.MyTiAdapter(getContext(), R.layout.custom_exam_ti_checkbox_bg, list);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tiContent.setText(tiString);
		for (int i = 0; i < answers.length; i++) {
			list.add(answers[i]);
		}
		listview.setAdapter(myTiAdapter);
	}
	
	public void setTiContent(String content) {
		tiString = content;
	}
	public void setTiList(String[] strings) {
		answers = strings;
	}
	
}
