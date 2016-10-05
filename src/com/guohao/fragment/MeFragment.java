package com.guohao.fragment;

import com.guohao.custom.MeBg;
import com.guohao.schoolproject.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MeFragment extends Fragment {
	private View view;
	private MeBg meBg01,meBg02,meBg03,meBg04,meBg05,meBg06;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_me, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initView();
	}

	private void initView() {
		meBg01 = (MeBg) view.findViewById(R.id.id_mebg01);
		meBg02 = (MeBg) view.findViewById(R.id.id_mebg02);
		meBg03 = (MeBg) view.findViewById(R.id.id_mebg03);
		meBg04 = (MeBg) view.findViewById(R.id.id_mebg04);
		meBg05 = (MeBg) view.findViewById(R.id.id_mebg05);
		meBg06 = (MeBg) view.findViewById(R.id.id_mebg06);
	}
}
