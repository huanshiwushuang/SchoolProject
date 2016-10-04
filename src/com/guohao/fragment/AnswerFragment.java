package com.guohao.fragment;

import com.guohao.custom.Title;
import com.guohao.schoolproject.MainActivity;
import com.guohao.schoolproject.R;
import com.guohao.util.Util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AnswerFragment extends Fragment {
	private View view;
	private MainActivity mainActivity;
	private Title customTitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_answer, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initView();
		initData();
	}

	private void initData() {
		customTitle.setTitleText("答题");
	}

	private void initView() {
		mainActivity = (MainActivity) getActivity();
		customTitle = mainActivity.getCustomTitle();
	}
	
	
	
	//---事件---
	public void clickExamTest(View view) {
		Util.showToast(getActivity(), "考试测评");
	}
	public void clickBookRead(View view) {
		Util.showToast(getActivity(), "书籍查看");
	}
	public void clickErrorTopic(View view) {
		Util.showToast(getActivity(), "错题书籍");
	}
	public void clickMyRecord(View view) {
		Util.showToast(getActivity(), "我的记录");
	}
}
