package com.guohao.schoolproject;

import java.util.ArrayList;
import java.util.List;

import com.guohao.custom.MyViewPager;
import com.guohao.custom.Title;
import com.guohao.fragment.AnswerFragment;
import com.guohao.fragment.MeFragment;
import com.guohao.fragment.SettingFragment;
import com.guohao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener {
	
	
	private Activity activity;
	private Title customTitle;
	private MyViewPager myViewPager;
	private LinearLayout tab01,tab02,tab03;
	
	private AnswerFragment answerFragment;
	private MeFragment meFragment;
	private SettingFragment settingFragment;
	private int TAB_COUNT = 3;
	private List<LinearLayout> layouts;
	
	private int[] beforeChecked;
	private int[] afterChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initData();
		initAdapter();
	}
	
	private void initAdapter() {
		myViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return TAB_COUNT;
			}
			
			@Override
			public Fragment getItem(int index) {
				switch (index) {
				case 0:
					if (answerFragment == null) {
						answerFragment = new AnswerFragment();
					}
					return answerFragment;
				case 1:
					if (meFragment == null) {
						meFragment = new MeFragment();
					}
					return meFragment;
				case 2:
					if (settingFragment == null) {
						settingFragment = new SettingFragment();
					}
					return settingFragment;
				}
				return null;
			}
		});
	}
	private void initData() {
		customTitle.setTitleText("答题");
	}
	private void initView() {
		layouts = new ArrayList<LinearLayout>();
		activity = MainActivity.this;
		
		customTitle = (Title) findViewById(R.id.id_custom_title);
		myViewPager = (MyViewPager) findViewById(R.id.id_myviewpager);
		myViewPager.addOnPageChangeListener(this);
		tab01 = (LinearLayout) findViewById(R.id.id_linearlayout_tab01);
		tab02 = (LinearLayout) findViewById(R.id.id_linearlayout_tab02);
		tab03 = (LinearLayout) findViewById(R.id.id_linearlayout_tab03);
		tab01.setOnClickListener(this);
		tab02.setOnClickListener(this);
		tab03.setOnClickListener(this);
		layouts.add(tab01);
		layouts.add(tab02);
		layouts.add(tab03);
		
		beforeChecked = new int[]{R.drawable.img310,R.drawable.img80,R.drawable.img98};
		afterChecked = new int[]{R.drawable.img311,R.drawable.img81,R.drawable.img100};
	}


	public static void actionStart(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}
	
	//---事件---
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_linearlayout_tab01:
			myViewPager.setCurrentItem(0, false);
			customTitle.setTitleText("答题");
			break;
		case R.id.id_linearlayout_tab02:
			myViewPager.setCurrentItem(1, false);
			customTitle.setTitleText("个人中心");
			break;
		case R.id.id_linearlayout_tab03:
			myViewPager.setCurrentItem(2, false);
			customTitle.setTitleText("设置");
			break;
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int arg0) {
		ImageView img;
		TextView text;
		for (int i = 0; i < layouts.size(); i++) {
			img = (ImageView) layouts.get(i).getChildAt(0);
			text = (TextView) layouts.get(i).getChildAt(1);
			img.setImageResource(beforeChecked[i]);
			text.setTextColor(Color.parseColor("#666666"));
		}
		img = (ImageView) layouts.get(arg0).getChildAt(0);
		text = (TextView) layouts.get(arg0).getChildAt(1);
		img.setImageResource(afterChecked[arg0]);
		text.setTextColor(Color.parseColor("#30BAE9"));
		
	}
	//---点击事件---碎片01---写在 xml 文件里
	public void clickExamTest(View view) {
		AnswerShowActivity.actionStart(activity, AnswerShowActivity.Exam_Test);
	}
	public void clickBookRead(View view) {
		AnswerShowActivity.actionStart(activity, AnswerShowActivity.Exam_Record);
	}
	public void clickMyRecord(View view) {
		AnswerShowActivity.actionStart(activity, AnswerShowActivity.My_Course);
	}

	private long lastTime = 0;
	@Override
	public void onBackPressed() {
		long thisTime = System.currentTimeMillis();
		if ((thisTime -lastTime) > 2*1000 || lastTime == 0) {
			Util.showToast(activity, "再次点击退出");
		}else {
			finish();
		}
		lastTime = thisTime;
	}
}
