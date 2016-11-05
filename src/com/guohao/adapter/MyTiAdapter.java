package com.guohao.adapter;

import java.util.List;

import com.guohao.schoolproject.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyTiAdapter extends BaseAdapter {
	private List<String> mList;
	private int mResourceId;
	private Context mContext;
	
	public MyTiAdapter(Context context, int resourceId, List<String> list) {
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
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.serialTextView.setText((char)(position+65)+". ");
		holder.answerTextView.setText(mList.get(position));
		return convertView;
	}
	
	class ViewHolder {
		TextView serialTextView;
		TextView answerTextView;
	}
}
