package com.guohao.fragment;

import com.guohao.custom.Title;
import com.guohao.schoolproject.MainActivity;
import com.guohao.schoolproject.R;

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
		customTitle.setTitleText("´ðÌâ");
	}

	private void initView() {
		mainActivity = (MainActivity) getActivity();
		customTitle = mainActivity.getCustomTitle();
	}
	
}
