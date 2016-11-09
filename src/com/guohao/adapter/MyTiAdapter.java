package com.guohao.adapter;

import java.util.List;

import com.guohao.schoolproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class MyTiAdapter extends BaseAdapter {
	private List<String[]> mList;
	private int mResourceId;
	private Context mContext;
	
	public MyTiAdapter(Context context, int resourceId, List<String[]> list) {
		mContext = context;
		mResourceId = resourceId;
		mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mResourceId, new FrameLayout(mContext));
			holder = new ViewHolder();
			holder.serialTextView = (TextView) convertView.findViewById(R.id.id_textview_serial_num);
			holder.answerTextView = (TextView) convertView.findViewById(R.id.id_textview_exam_ti_item);
			switch (mResourceId) {
			case R.layout.custom_exam_ti_radio_bg:
				holder.radioButton = (RadioButton) convertView.findViewById(R.id.id_radiobutton);
				break;
			case R.layout.custom_exam_ti_checkbox_bg:
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.id_checkbox);
				break;
			}
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.serialTextView.setText((char)(position+65)+". ");
		holder.answerTextView.setText(mList.get(position)[0]);
		String isChecked = mList.get(position)[1];
		switch (mResourceId) {
		case R.layout.custom_exam_ti_radio_bg:
			if (isChecked.equals("1")) {
				holder.radioButton.setChecked(true);
			}else {
				holder.radioButton.setChecked(false);
			}
			break;
		case R.layout.custom_exam_ti_checkbox_bg:
			if (isChecked.equals("1")) {
				holder.checkBox.setChecked(true);
			}else {
				holder.checkBox.setChecked(false);
			}
			break;
		}
		return convertView;
	}
	
	class ViewHolder {
		TextView serialTextView;
		TextView answerTextView;
		RadioButton radioButton;
		CheckBox checkBox;
	}
}
