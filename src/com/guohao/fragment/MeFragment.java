package com.guohao.fragment;

import com.guohao.custom.MeBg;
import com.guohao.schoolproject.R;
import com.guohao.util.Data;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MeFragment extends Fragment {
	private View view;
	private MeBg meBg01,meBg02,meBg03;
	
	private String name,stuNumber,grade;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_me, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initView();
		initData();
		setData();
	}

	private void setData() {
		meBg01.setTextViewText02(name);
		meBg02.setTextViewText02(stuNumber);
		meBg03.setTextViewText02(grade);
	}

	private void initData() {
		SharedPreferences p = Util.getPreference(getActivity());
		name = p.getString(Data.NAME, "");
		stuNumber = p.getString(Data.STU_NUMBER, "");
		grade = p.getString(Data.GRADE, "");
	}

	private void initView() {
		meBg01 = (MeBg) view.findViewById(R.id.id_mebg01);
		meBg02 = (MeBg) view.findViewById(R.id.id_mebg02);
		meBg03 = (MeBg) view.findViewById(R.id.id_mebg03);
//		meBg04 = (MeBg) view.findViewById(R.id.id_mebg04);
//		meBg05 = (MeBg) view.findViewById(R.id.id_mebg05);
//		meBg06 = (MeBg) view.findViewById(R.id.id_mebg06);
	}
}
