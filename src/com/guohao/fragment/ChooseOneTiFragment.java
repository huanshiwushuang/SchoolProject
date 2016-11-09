package com.guohao.fragment;

import java.util.ArrayList;
import java.util.List;

import com.guohao.adapter.MyTiAdapter;
import com.guohao.schoolproject.R;
import com.guohao.util.Util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		myTiAdapter = new MyTiAdapter(getContext(), R.layout.custom_exam_ti_radio_bg, list);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initBaseData();
	}
	
	private void initBaseData() {
		tiContent.setText(tiString);
		for (int i = 0; i < answers.length; i++) {
			list.add(new String[]{answers[i],"0"});
		}
		listview.setAdapter(myTiAdapter);
	}

	public void setTiContent(String content) {
		tiString = content;
	}
	public void setTiList(String[] strings) {
		answers = strings;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Util.showToast(getContext(), "µ¯³ö");
		for (int i = 0; i < list.size(); i++) {
			list.get(i)[1] = "0";
		}
		list.get(position)[1] = "1";
		myTiAdapter.notifyDataSetChanged();
	}
}
